package com.projectsky.loyaltysystem.controller;

import com.projectsky.loyaltysystem.dto.*;
import com.projectsky.loyaltysystem.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> allClients = clientService.getAllClients();
        return allClients.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(allClients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientFullDto> getClientById(
            @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

    @PostMapping
    public ResponseEntity<IdDto> createClient(
            @Valid @RequestBody ClientCreateDto dto
    ) {
        IdDto idDto = clientService.addClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(idDto);
    }

    @PatchMapping("/{clientId}")
    public ResponseEntity<ClientFullDto> updateClient(
            @RequestBody ClientUpdateDto dto,
            @PathVariable Long clientId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateClient(dto, clientId));
    }

    @PatchMapping("/{clientId}/balance")
    public ResponseEntity<ClientDto> updateBalance(
            @PathVariable Long clientId,
            @RequestBody @Valid BalanceDto dto
    ) {
        return ResponseEntity.ok(clientService.topUpBalance(dto, clientId));
    }


    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long clientId
    ) {
        clientService.deleteClientById(clientId);
        return ResponseEntity.noContent().build();
    }
}
