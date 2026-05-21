package com.quadrafast.exception;

/**
 * Hierarquia de exceções de domínio do QuadraFast.
 *
 * Cada exceção carrega significado semântico, permitindo que o
 * GlobalExceptionHandler mapeie para o HTTP status correto sem
 * lógica de if/else espalhada pelo código.
 */
public class DomainException {

    /** Base — nunca instanciar diretamente */
    public static abstract class AppException extends RuntimeException {
        protected AppException(String message) { super(message); }
    }

    /** 404 — recurso não encontrado */
    public static class RecursoNaoEncontradoException extends AppException {
        public RecursoNaoEncontradoException(String recurso, Long id) {
            super(recurso + " com id " + id + " não encontrado.");
        }
    }

    /** 409 — conflito de horário */
    public static class ConflitoDeHorarioException extends AppException {
        public ConflitoDeHorarioException() {
            super("Já existe uma reserva confirmada para essa quadra no horário solicitado.");
        }
    }

    /** 422 — quadra inativa / indisponível */
    public static class QuadraInativaException extends AppException {
        public QuadraInativaException(Long quadraId) {
            super("A quadra com id " + quadraId + " está inativa e não aceita reservas.");
        }
    }
}
