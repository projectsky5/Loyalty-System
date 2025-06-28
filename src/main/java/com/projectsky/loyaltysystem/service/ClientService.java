package com.projectsky.loyaltysystem.service;

import com.projectsky.loyaltysystem.dto.*;

import java.util.List;

public interface ClientService {

    IdDto addClient(ClientCreateDto dto);
    ClientDto topUpBalance(BalanceDto dto, Long id);
    ClientFullDto getClientById(Long id);
    List<ClientDto> getAllClients();
    ClientFullDto updateClient(ClientUpdateDto dto, Long id);
    void deleteClientById(Long id);

    Integer addPoints(Long id, Integer points);
    Integer takePoints(Long id, Integer points);
}
