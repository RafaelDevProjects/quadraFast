package com.quadrafast.service;

import com.quadrafast.dto.ReservaDTO;
import com.quadrafast.exception.DomainException.*;
import com.quadrafast.model.Quadra;
import com.quadrafast.model.Reserva;
import com.quadrafast.model.Reserva.StatusReserva;
import com.quadrafast.model.Usuario;
import com.quadrafast.repository.QuadraRepository;
import com.quadrafast.repository.ReservaRepository;
import com.quadrafast.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReservaService — contém exclusivamente a regra de negócio.
 *
 * Responsabilidades:
 *  1. Validar existência da quadra e do usuário
 *  2. Validar que a quadra está ativa
 *  3. Validar conflito de horário
 *  4. Persistir a reserva
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final QuadraRepository  quadraRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ReservaDTO.Response criar(ReservaDTO.Request request) {

        log.info("[ReservaService] Criando reserva — quadraId={} usuarioId={} inicio={} fim={}",
                request.getQuadraId(), request.getUsuarioId(),
                request.getDataHoraInicio(), request.getDataHoraFim());

        // 1. Quadra existe?
        Quadra quadra = quadraRepository.findById(request.getQuadraId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quadra", request.getQuadraId()));

        // 2. Quadra está ativa?
        if (!quadra.getAtiva()) {
            throw new QuadraInativaException(quadra.getId());
        }

        // 3. Usuário existe?
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", request.getUsuarioId()));

        // 4. Conflito de horário? — status passado como parâmetro tipado
        boolean conflito = reservaRepository.existeConflito(
                quadra.getId(),
                request.getDataHoraInicio(),
                request.getDataHoraFim(),
                StatusReserva.CONFIRMADA
        );

        if (conflito) {
            throw new ConflitoDeHorarioException();
        }

        // 5. Persistir
        Reserva reserva = Reserva.builder()
                .quadra(quadra)
                .usuario(usuario)
                .dataHoraInicio(request.getDataHoraInicio())
                .dataHoraFim(request.getDataHoraFim())
                .status(StatusReserva.CONFIRMADA)
                .build();

        Reserva salva = reservaRepository.save(reserva);

        log.info("[ReservaService] Reserva criada com sucesso — id={}", salva.getId());

        return ReservaDTO.Response.from(salva);
    }
}
