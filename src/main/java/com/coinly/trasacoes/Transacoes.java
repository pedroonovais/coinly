package com.coinly.trasacoes;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes", indexes = {
        @Index(name = "idx_transacoes_data", columnList = "dataHora"),
        @Index(name = "idx_transacoes_tipo", columnList = "tipo")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String descricao;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoTransacao tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private StatusTransacao status;

    @NotNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Size(max = 60)
    @Column(length = 60)
    private String categoria;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Use o código de moeda ISO 4217 (ex.: BRL, USD)")
    @Column(length = 3)
    private String moeda;

    @Size(max = 255)
    @Column(length = 255)
    private String observacao;

    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        final var now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.dataHora == null)
            this.dataHora = now;
        if (this.status == null)
            this.status = StatusTransacao.PENDENTE;
        if (this.moeda == null)
            this.moeda = "BRL";
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum TipoTransacao {
        CREDITO, DEBITO
    }

    public enum StatusTransacao {
        PENDENTE, EFETIVADA, CANCELADA
    }
}
