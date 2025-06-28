package com.projectsky.loyaltysystem.controller;

import com.projectsky.loyaltysystem.dto.IdDto;
import com.projectsky.loyaltysystem.dto.PurchaseCreateDto;
import com.projectsky.loyaltysystem.dto.PurchaseDto;
import com.projectsky.loyaltysystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseDto>> getAllPurchases() {
        List<PurchaseDto> allPurchases = purchaseService.getAllPurchases();
        return allPurchases.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(allPurchases);
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDto> getPurchaseById(
            @PathVariable Long purchaseId
    ) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(purchaseId));
    }

    @PostMapping("/{clientId}")
    public ResponseEntity<IdDto> createPurchase(
            @PathVariable Long clientId,
            @RequestBody PurchaseCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(purchaseService.addPurchase(dto, clientId));
    }

    @PatchMapping("/{purchaseId}/refund")
    public ResponseEntity<Void> refundPurchase(
            @PathVariable Long purchaseId
    ) {
        purchaseService.refundPurchase(purchaseId);
        return ResponseEntity.ok().build();
    }
}
