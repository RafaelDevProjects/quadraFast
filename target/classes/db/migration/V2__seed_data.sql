-- ============================================================
-- V2__seed_data.sql
-- Dados iniciais para demonstração e testes manuais
-- ============================================================

INSERT INTO quadras (nome, tipo, ativa) VALUES
    ('Quadra A', 'FUTEBOL',     TRUE),
    ('Quadra B', 'TENIS',       TRUE),
    ('Quadra C', 'BEACH_VOLEI', TRUE);

INSERT INTO usuarios (nome, email) VALUES
    ('João Silva',   'joao@email.com'),
    ('Maria Santos', 'maria@email.com');
