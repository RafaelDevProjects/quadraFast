package com.quadrafast.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ApiResponse — envelope padrão de todas as respostas da API.
 *
 * Garante contrato consistente: o cliente sempre recebe a mesma estrutura,
 * seja em caso de sucesso ou erro.
 *
 * {
 *   "sucesso": true,
 *   "mensagem": "Reserva criada com sucesso.",
 *   "dados": { ... },          // presente em sucesso
 *   "erros": null,             // presente em erro de validação
 *   "timestamp": "..."
 * }
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean sucesso;
    private final String mensagem;
    private final T dados;
    private final List<String> erros;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    private ApiResponse(boolean sucesso, String mensagem, T dados, List<String> erros) {
        this.sucesso   = sucesso;
        this.mensagem  = mensagem;
        this.dados     = dados;
        this.erros     = erros;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> sucesso(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados, null);
    }

    public static <T> ApiResponse<T> erro(String mensagem, List<String> erros) {
        return new ApiResponse<>(false, mensagem, null, erros);
    }

    public static <T> ApiResponse<T> erro(String mensagem) {
        return new ApiResponse<>(false, mensagem, null, null);
    }
}
