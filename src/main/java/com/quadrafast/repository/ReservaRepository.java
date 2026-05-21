package com.quadrafast.repository;

import com.quadrafast.model.Reserva;
import com.quadrafast.model.Reserva.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Verifica conflito de horário para a quadra.
     *
     * Sobreposição ocorre quando:
     *   novoInicio < fimExistente  E  novoFim > inicioExistente
     *
     * O status é passado como parâmetro tipado (:status) para evitar
     * referência a classe interna com $ no JPQL, que causa erro em runtime.
     */
    @Query("""
            SELECT COUNT(r) > 0
            FROM Reserva r
            WHERE r.quadra.id      = :quadraId
              AND r.status         = :status
              AND r.dataHoraInicio < :fim
              AND r.dataHoraFim    > :inicio
            """)
    boolean existeConflito(
            @Param("quadraId") Long quadraId,
            @Param("inicio")   LocalDateTime inicio,
            @Param("fim")      LocalDateTime fim,
            @Param("status")   StatusReserva status
    );

    List<Reserva> findByQuadraIdOrderByDataHoraInicioAsc(Long quadraId);
}
