package com.coinly.trasacoes;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacoesDTO {
        private Long id;

        @NotBlank
        private String descricao;

        @NotNull
        @Positive
        private BigDecimal valor;

        @NotNull
        private Transacoes.TipoTransacao tipo;

        @NotNull
        private Transacoes.StatusTransacao status;

        @NotNull
        private LocalDateTime dataHora;

        private String categoria;
        private String moeda;
        private String observacao;

        public static TransacoesDTO fromEntity(Transacoes t) {
                return TransacoesDTO.builder()
                                .id(t.getId())
                                .descricao(t.getDescricao())
                                .valor(t.getValor())
                                .tipo(t.getTipo())
                                .status(t.getStatus())
                                .dataHora(t.getDataHora())
                                .categoria(t.getCategoria())
                                .moeda(t.getMoeda())
                                .observacao(t.getObservacao())
                                .build();
        }
}
