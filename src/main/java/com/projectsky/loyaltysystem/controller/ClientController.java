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

    @GetMapping("/{id}")
    public ResponseEntity<ClientFullDto> getClientById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PostMapping
    public ResponseEntity<ClientIdDto> createClient(
            @Valid @RequestBody ClientCreateDto dto
    ) {
        clientService.addClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientFullDto> updateClient(
            @RequestBody ClientUpdateDto dto,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateClient(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id
    ) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }
}
