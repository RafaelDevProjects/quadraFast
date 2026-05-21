package com.quadrafast.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quadrafast.model.Reserva;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ReservaDTO — isola o contrato da API do modelo de domínio (JPA).
 *
 * Padrão adotado:
 *   Request  → dados enviados pelo cliente (entrada)
 *   Response → dados devolvidos ao cliente (saída) — jamais expõe a entidade diretamente
 */
public class ReservaDTO {

    // ─── REQUEST ─────────────────────────────────────────────────────────────

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "quadraId é obrigatório.")
        private Long quadraId;

        @NotNull(message = "usuarioId é obrigatório.")
        private Long usuarioId;

        @NotNull(message = "dataHoraInicio é obrigatório.")
        @Future(message = "dataHoraInicio deve ser uma data futura.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dataHoraInicio;

        @NotNull(message = "dataHoraFim é obrigatório.")
        @Future(message = "dataHoraFim deve ser uma data futura.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dataHoraFim;
    }

    // ─── RESPONSE ────────────────────────────────────────────────────────────

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private Long quadraId;
        private String quadraNome;
        private Long usuarioId;
        private String usuarioNome;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dataHoraInicio;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dataHoraFim;

        private String status;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime criadoEm;

        /** Converte entidade → DTO de resposta sem expor JPA ao cliente */
        public static Response from(Reserva reserva) {
            return Response.builder()
                    .id(reserva.getId())
                    .quadraId(reserva.getQuadra().getId())
                    .quadraNome(reserva.getQuadra().getNome())
                    .usuarioId(reserva.getUsuario().getId())
                    .usuarioNome(reserva.getUsuario().getNome())
                    .dataHoraInicio(reserva.getDataHoraInicio())
                    .dataHoraFim(reserva.getDataHoraFim())
                    .status(reserva.getStatus().name())
                    .criadoEm(reserva.getCriadoEm())
                    .build();
        }
    }
}
