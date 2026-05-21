package com.quadrafast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadrafast.dto.ReservaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservaControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    private static final String URL = "/v1/reservas";
    private static final LocalDateTime INICIO = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0);
    private static final LocalDateTime FIM    = INICIO.plusHours(2);

    @Test
    @DisplayName("POST /v1/reservas — deve retornar 201 com reserva criada")
    void deveCriarReservaComSucesso() throws Exception {
        ReservaDTO.Request request = new ReservaDTO.Request(1L, 1L, INICIO, FIM);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sucesso").value(true))
                .andExpect(jsonPath("$.dados.id").exists())
                .andExpect(jsonPath("$.dados.status").value("CONFIRMADA"));
    }

    @Test
    @DisplayName("POST /v1/reservas — deve retornar 404 para quadra inexistente")
    void deveRetornar404ParaQuadraInexistente() throws Exception {
        ReservaDTO.Request request = new ReservaDTO.Request(999L, 1L, INICIO, FIM);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.sucesso").value(false));
    }

    @Test
    @DisplayName("POST /v1/reservas — deve retornar 409 em conflito de horário")
    void deveRetornar409EmConflito() throws Exception {
        ReservaDTO.Request request = new ReservaDTO.Request(1L, 1L, INICIO, FIM);

        // Primeira reserva — deve funcionar
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Segunda reserva no mesmo horário — conflito
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.sucesso").value(false));
    }

    @Test
    @DisplayName("POST /v1/reservas — deve retornar 400 para campos obrigatórios ausentes")
    void deveRetornar400ParaCamposAusentes() throws Exception {
        String json = "{}";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso").value(false))
                .andExpect(jsonPath("$.erros").isArray());
    }
}
