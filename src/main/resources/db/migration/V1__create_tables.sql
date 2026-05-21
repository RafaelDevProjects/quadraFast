-- ============================================================
-- V1__create_tables.sql
-- Migration inicial: criação do schema do QuadraFast
-- Flyway garante rastreabilidade e execução ordenada.
-- ============================================================

CREATE TABLE quadras (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    tipo       VARCHAR(50)  NOT NULL,
    ativa      BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuarios (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    email     VARCHAR(150) NOT NULL UNIQUE,
    criado_em TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservas (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    quadra_id        BIGINT      NOT NULL,
    usuario_id       BIGINT      NOT NULL,
    data_hora_inicio TIMESTAMP   NOT NULL,
    data_hora_fim    TIMESTAMP   NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
    criado_em        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reserva_quadra   FOREIGN KEY (quadra_id)  REFERENCES quadras(id),
    CONSTRAINT fk_reserva_usuario  FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT chk_horario         CHECK (data_hora_fim > data_hora_inicio)
);
