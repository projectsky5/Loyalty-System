package com.projectsky.loyaltysystem.service;

import com.projectsky.loyaltysystem.dto.BalanceDto;
import com.projectsky.loyaltysystem.dto.IdDto;
import com.projectsky.loyaltysystem.dto.PurchaseCreateDto;
import com.projectsky.loyaltysystem.dto.PurchaseDto;
import com.projectsky.loyaltysystem.enums.PurchaseStatus;
import com.projectsky.loyaltysystem.exception.ClientNotFoundException;
import com.projectsky.loyaltysystem.exception.NotEnoughBalanceException;
import com.projectsky.loyaltysystem.exception.PurchaseAlreadyRefundedException;
import com.projectsky.loyaltysystem.exception.PurchaseNotFoundException;
import com.projectsky.loyaltysystem.model.Client;
import com.projectsky.loyaltysystem.model.Purchase;
import com.projectsky.loyaltysystem.repository.ClientRepository;
import com.projectsky.loyaltysystem.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Override
    @Transactional
    public IdDto addPurchase(PurchaseCreateDto dto, Long clientId) {
        log.debug("Попытка совершить покупку пользователем id={}", clientId);
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->{
                    log.warn("Пользователь с id={} не найден", clientId);
                    return new ClientNotFoundException("Пользователь не найден");
                });

        if(client.getBalance().compareTo(dto.price()) < 0){
            log.warn("У пользователя с id={} не хватает средств", clientId);
            throw new NotEnoughBalanceException("Не хватает средств для совершения покупки");
        }

        BigDecimal price = dto.price();

        client.setBalance(client.getBalance().subtract(price));

        Purchase purchase = Purchase.builder()
                .name(dto.name())
                .client(client)
                .purchaseDate(LocalDateTime.now())
                .price(price)
                .status(PurchaseStatus.SUCCESSFUL)
                .build();

        Purchase saved = purchaseRepository.save(purchase);
        log.info("Покупка товара={} пользователем id={} прошла успешно", dto.name(), saved.getId());

        Integer points = calculatePoints(price);

        clientService.addPoints(clientId, points);

        return new IdDto(saved.getId());
    }

    @Override
    public PurchaseDto getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException("Операция не найдена"));

        return buildDto(purchase);
    }

    @Override
    public List<PurchaseDto> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(this::buildDto)
                .toList();
    }

    @Override
    @Transactional
    public void refundPurchase(Long id) {
        log.debug("Попытка вернуть средства за покупку id={}", id);
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Операция с id={} не найдена", id);
                    return new PurchaseNotFoundException("Операция не найдена");
                });

        if(purchase.getStatus() != PurchaseStatus.SUCCESSFUL){
            log.warn("Произошел возврат уже отмененной операции id={}", id);
            throw new PurchaseAlreadyRefundedException("Операция уже отменена");
        }

        clientService.topUpBalance(new BalanceDto(
                purchase.getPrice()),
                purchase.getClient().getId()
        );
        log.debug("Возврат средств за покупку с id = {} прошел успешно", id);

        Integer points = calculatePoints(purchase.getPrice());

        clientService.takePoints(purchase.getClient().getId(), points);
        log.debug("Баллы за покупку id={} успешно списаны", id);

        purchase.setStatus(PurchaseStatus.REFUNDED);

        purchaseRepository.save(purchase);
        log.info("Возврат средств за покупку id={} прошел успешно", id);
    }

    private PurchaseDto buildDto(Purchase purchase) {
        return PurchaseDto.builder()
                .id(purchase.getId())
                .clientId(purchase.getClient().getId())
                .name(purchase.getName())
                .purchaseDate(purchase.getPurchaseDate())
                .price(purchase.getPrice())
                .build();
    }

    private Integer calculatePoints(BigDecimal price) {
        return (int) Math.ceil(price.doubleValue() * 0.05);
    }
}
