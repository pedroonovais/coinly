-- V2__create_table_transacoes.sql

CREATE TABLE IF NOT EXISTS transacoes (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(120) NOT NULL,
    valor NUMERIC(19,4) NOT NULL CHECK (valor > 0),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('CREDITO','DEBITO')),
    status VARCHAR(12) NOT NULL DEFAULT 'PENDENTE'
        CHECK (status IN ('PENDENTE','EFETIVADA','CANCELADA')),
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    categoria VARCHAR(60),
    moeda CHAR(3) NOT NULL DEFAULT 'BRL'
        CHECK (char_length(moeda) = 3 AND moeda = upper(moeda)),
    observacao VARCHAR(255),
    version BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_transacoes_data ON transacoes (data_hora);
CREATE INDEX IF NOT EXISTS idx_transacoes_tipo ON transacoes (tipo);
