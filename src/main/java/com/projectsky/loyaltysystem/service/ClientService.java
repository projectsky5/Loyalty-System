package com.projectsky.loyaltysystem.service;

import com.projectsky.loyaltysystem.dto.*;

import java.util.List;

public interface ClientService {

    ClientIdDto addClient(ClientCreateDto dto);
    ClientFullDto getClientById(Long id);
    List<ClientDto> getAllClients();
    ClientFullDto updateClient(ClientUpdateDto dto, Long id);
    void deleteClientById(Long id);
}
