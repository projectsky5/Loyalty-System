package com.projectsky.loyaltysystem.service;

import com.projectsky.loyaltysystem.dto.IdDto;
import com.projectsky.loyaltysystem.dto.PurchaseCreateDto;
import com.projectsky.loyaltysystem.dto.PurchaseDto;

import java.util.List;

public interface PurchaseService {

    IdDto addPurchase(PurchaseCreateDto dto, Long clientId);
    PurchaseDto getPurchaseById(Long id);
    List<PurchaseDto> getAllPurchases();

    void refundPurchase(Long id);
}
