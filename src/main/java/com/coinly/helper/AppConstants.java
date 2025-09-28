package com.coinly.helper;

/**
 * Constantes da aplicação (domínio: Transações).
 * Centraliza mensagens, redirects, views e atributos.
 */
public final class AppConstants {

    // ===== Mensagens de sucesso =====
    public static final String MSG_TRANSACAO_SALVA       = "Transação salva com sucesso!";
    public static final String MSG_TRANSACAO_ATUALIZADA  = "Transação atualizada com sucesso!";
    public static final String MSG_TRANSACAO_REMOVIDA    = "Transação removida com sucesso!";

    // ===== Mensagens de erro =====
    public static final String ERR_TRANSACAO_NAO_ENCONTRADA = "Transação não encontrada";

    // ===== Redirects =====
    public static final String REDIRECT_TRANSACOES = "redirect:/transacoes";
    public static final String REDIRECT_INDEX      = "redirect:/index";

    // ===== Views =====
    public static final String VIEW_TRANSACAO_INDEX = "transacoes/index";
    public static final String VIEW_TRANSACAO_FORM  = "transacoes/form";
    public static final String VIEW_INDEX           = "index";

    // ===== Atributos do modelo =====
    public static final String ATTR_MESSAGE     = "message";
    public static final String ATTR_TRANSACOES  = "transacoes";
    public static final String ATTR_TRANSACAO   = "transacao";

    private AppConstants() {
    }
}
