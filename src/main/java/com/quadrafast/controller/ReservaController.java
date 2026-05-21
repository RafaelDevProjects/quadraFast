package com.quadrafast.controller;

import com.quadrafast.dto.ApiResponse;
import com.quadrafast.dto.ReservaDTO;
import com.quadrafast.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ReservaController — responsável apenas por:
 *   • Receber a requisição HTTP
 *   • Delegar ao Service
 *   • Devolver a resposta padronizada
 *
 * Nenhuma regra de negócio aqui. Controller gordo = acoplamento = dívida técnica.
 */
@Slf4j
@RestController
@RequestMapping("/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    /**
     * POST /v1/reservas
     *
     * Cria uma nova reserva de quadra esportiva.
     *
     * @param request DTO validado pelo Bean Validation
     * @return 201 Created com a reserva criada no envelope ApiResponse
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReservaDTO.Response>> criar(
            @Valid @RequestBody ReservaDTO.Request request) {

        log.info("[ReservaController] POST /v1/reservas — quadraId={}", request.getQuadraId());

        ReservaDTO.Response response = reservaService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso("Reserva criada com sucesso.", response));
    }
}
