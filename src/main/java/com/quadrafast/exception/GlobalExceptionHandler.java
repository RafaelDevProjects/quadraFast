package com.quadrafast.exception;

import com.quadrafast.dto.ApiResponse;
import com.quadrafast.exception.DomainException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * GlobalExceptionHandler — intercepta todas as exceções da aplicação
 * e devolve respostas padronizadas via ApiResponse.
 *
 * Benefício: nenhum controller precisa de try/catch; a lógica de
 * mapeamento erro → HTTP fica num único lugar, fácil de manter.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400 — erros de validação do Bean Validation (@NotNull, @Future, etc.) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        log.warn("[Handler] Validação falhou: {}", erros);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.erro("Dados de entrada inválidos.", erros));
    }

    /** 404 */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RecursoNaoEncontradoException ex) {
        log.warn("[Handler] Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.erro(ex.getMessage()));
    }

    /** 409 — conflito de horário */
    @ExceptionHandler(ConflitoDeHorarioException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflito(ConflitoDeHorarioException ex) {
        log.warn("[Handler] Conflito de horário: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.erro(ex.getMessage()));
    }

    /** 422 — quadra inativa */
    @ExceptionHandler(QuadraInativaException.class)
    public ResponseEntity<ApiResponse<Void>> handleQuadraInativa(QuadraInativaException ex) {
        log.warn("[Handler] Quadra inativa: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.erro(ex.getMessage()));
    }

    /** 500 — fallback: loga a causa real para facilitar depuração */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenerico(Exception ex) {
        log.error("[Handler] Erro inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.erro("Erro interno. Tente novamente mais tarde."));
    }
}
