package com.projectsky.loyaltysystem.service;

import com.projectsky.loyaltysystem.dto.*;
import com.projectsky.loyaltysystem.enums.Category;
import com.projectsky.loyaltysystem.exception.ClientAlreadyExistsException;
import com.projectsky.loyaltysystem.exception.ClientNotFoundException;
import com.projectsky.loyaltysystem.model.Client;
import com.projectsky.loyaltysystem.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientIdDto addClient(ClientCreateDto dto) {
        if(clientRepository.existsByUsername(dto.username())){
            log.warn("Попытка создать пользователя с существующим username={}", dto.username());
            throw new ClientAlreadyExistsException("Пользователь уже существует");
        }

        Client client = Client.builder()
                .username(dto.username())
                .email(dto.email())
                .category(Category.BASIC)
                .bonusPoints(0)
                .build();

        Client saved = clientRepository.save(client);
        Long id = saved.getId();

        log.info("Добавлен пользователь id={}, username={}", id, dto.username());

        return new ClientIdDto(id);
    }

    @Override
    public ClientFullDto getClientById(Long id) {
        return clientRepository.findClientSummaryById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент не найден"));
    }

    @Override
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::buildClientDto)
                .toList();
    }

    @Override
    @Transactional
    public ClientFullDto updateClient(ClientUpdateDto dto, Long id) {
        log.debug("Попытка обновить информацию о пользователе с id={}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Клиент с id={} не найден", id);
                    return new ClientNotFoundException("Пользователь не найден");
                });

        // Проверка на то, что username не пустой, и на то, что новый username не занят
        if(dto.username() != null && !dto.username().isBlank()){
            if(!client.getUsername().equals(dto.username())
                    && clientRepository.existsByUsername(dto.username())){
                log.warn("Попытка использовать занятый username={}", dto.username());
                throw new ClientAlreadyExistsException("Имя уже занято");
            }
            client.setUsername(dto.username());
        }
        if(dto.email() != null && !dto.email().isBlank()){
            client.setEmail(dto.email());
        }

        Client saved = clientRepository.save(client);

        log.debug("Обновление информации о пользователе с id={} успешно, новый username={}", id, dto.username());

        return getClientById(saved.getId());
    }

    @Override
    @Transactional
    public void deleteClientById(Long id) {
        log.debug("Попытка удалить пользователя с id={}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Попытка удалить несуществующего пользователя с id={} ", id);
                    return new ClientNotFoundException("Пользователь не найден");
                });

        clientRepository.delete(client);
        log.info("Удален пользователь с id={}", id);
    }

    private ClientDto buildClientDto(Client client) {
        return ClientDto.builder()
                .username(client.getUsername())
                .email(client.getEmail())
                .category(client.getCategory().name())
                .points(client.getBonusPoints())
                .build();
    }

}
