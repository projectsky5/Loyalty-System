package com.projectsky.loyaltysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsky.loyaltysystem.dto.*;
import com.projectsky.loyaltysystem.enums.Category;
import com.projectsky.loyaltysystem.exception.ClientAlreadyExistsException;
import com.projectsky.loyaltysystem.exception.ClientNotFoundException;
import com.projectsky.loyaltysystem.exception.UsernameAlreadyExistsException;
import com.projectsky.loyaltysystem.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @MockitoBean
    ClientService clientService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnNotEmptyListAndOk() throws Exception {
        ClientDto dto = ClientDto.builder()
                .username("akimara")
                .email("mackvej@gmail.com")
                .balance(BigDecimal.valueOf(100))
                .points(100)
                .category(Category.BASIC.name())
                .build();

        when(clientService.getAllClients()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("akimara"))
                .andExpect(jsonPath("$[0].category").value("BASIC"))
                .andExpect(jsonPath("$[0].email").value("mackvej@gmail.com"))
                .andExpect(jsonPath("$[0].balance").value(100))
                .andExpect(jsonPath("$[0].points").value(100));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void shouldReturnNoContentIfEmptyList() throws Exception {
        when(clientService.getAllClients()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/client"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void shouldReturnOkAndClientFullDto() throws Exception {
        LocalDateTime now = LocalDateTime.parse("2025-06-27T15:00:58.092813");

        ClientFullDto dto = new ClientFullDto(
                1L,
                "akimara",
                "mackvej@gmail.com",
                BigDecimal.TEN,
                100,
                Category.BASIC,
                now,
                1
        );

        when(clientService.getClientById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/client/{clientId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("akimara"))
                .andExpect(jsonPath("$.category").value("BASIC"))
                .andExpect(jsonPath("$.balance").value(10))
                .andExpect(jsonPath("$.points").value(100))
                .andExpect(jsonPath("$.totalPurchases").value(1))
                .andExpect(jsonPath("$.lastPurchase").value(now.toString()));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void shouldReturn404IfClientNotFound() throws Exception {
        when(clientService.getClientById(1L)).thenThrow(ClientNotFoundException.class);

        mockMvc.perform(get("/api/client/{clientId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Client not found"));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void shouldReturnOkAndId_IfClientAdded() throws Exception {
        ClientCreateDto dto = ClientCreateDto.builder()
                .username("akimara")
                .email("mackvej@gmail.com")
                .build();

        IdDto idDto = new IdDto(1L);

        when(clientService.addClient(dto)).thenReturn(idDto);

        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(clientService, times(1)).addClient(dto);
    }

    @ParameterizedTest
    @MethodSource("getInvalidDto")
    void shouldReturnBadRequestIfInvalidDto(ClientCreateDto dto) throws Exception {
        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Invalid request"));
        verify(clientService, never()).addClient(dto);
    }

    static Stream<ClientCreateDto> getInvalidDto(){
        return Stream.of(
                new ClientCreateDto("alex", "mackvej@gmail.com"),
                new ClientCreateDto("akimara", "ss$3*@test.ru"),
                new ClientCreateDto("alex", "sss@test.ru")
        );
    }

    @Test
    void shouldReturnConflictIfClientAlreadyExists() throws Exception {
        ClientCreateDto dto = ClientCreateDto.builder()
                .username("akimara")
                .email("mackvej@gmail.com")
                .build();

        when(clientService.addClient(dto)).thenThrow(ClientAlreadyExistsException.class);

        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.error").value("Client already exists"));

        verify(clientService, times(1)).addClient(dto);
    }

    @Test
    void shouldReturnOkIfUpdated() throws Exception {
        LocalDateTime now = LocalDateTime.parse("2025-06-27T15:00:58.092813");
        ClientUpdateDto dto = ClientUpdateDto.builder()
                .username("akimaranew")
                .email("mackvej@gmail.com")
                .build();

        ClientFullDto dtoFull = new ClientFullDto(
                1L,
                "akimaranew",
                "mackvej@gmail.com",
                BigDecimal.TEN,
                100,
                Category.BASIC,
                now,
                1
        );

        when(clientService.updateClient(dto, 1L)).thenReturn(dtoFull);

        mockMvc.perform(patch("/api/client/{clientId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("akimaranew"));

        verify(clientService, times(1)).updateClient(dto, 1L);
    }

    @Test
    void shouldReturnConflictIfUpdateFailed_UsernameAlreadyExists() throws Exception {
        ClientUpdateDto dto = ClientUpdateDto.builder()
                .username("akimaranew")
                .email("mackvej@gmail.com")
                .build();

        when(clientService.updateClient(any(ClientUpdateDto.class), anyLong())).thenThrow(UsernameAlreadyExistsException.class);

        mockMvc.perform(patch("/api/client/{clientId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Username already exists"));

        verify(clientService, times(1)).updateClient(dto, 1L);
    }

    @Test
    void shouldReturnOkAndTopUpBalance() throws Exception {
        BalanceDto balanceDto = new BalanceDto(BigDecimal.valueOf(100));
        ClientDto dto = ClientDto.builder()
                .username("akimara")
                .email("mackvej@gmail.com")
                .balance(BigDecimal.valueOf(100))
                .points(100)
                .category(Category.BASIC.name())
                .build();

        when(clientService.topUpBalance(balanceDto, 1L)).thenReturn(dto);

        mockMvc.perform(patch("/api/client/{clientId}/balance", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(balanceDto)))
                .andExpect(status().isOk());

        verify(clientService, times(1)).topUpBalance(balanceDto, 1L);
    }

    @Test
    void shouldReturnOkWhenDeleteDoneSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/client/{clientId}", 1L))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClientById(1L);
    }

    @Test
    void shouldReturnNotFoundIfClientNotFound() throws Exception {
        doThrow(ClientNotFoundException.class).when(clientService).deleteClientById(1L);

        mockMvc.perform(delete("/api/client/{clientId}", 1L))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).deleteClientById(1L);
    }

}