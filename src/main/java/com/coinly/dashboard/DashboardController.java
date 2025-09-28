package com.coinly.dashboard;

import com.coinly.helper.BaseController;
import com.coinly.trasacoes.Transacoes;
import com.coinly.trasacoes.TransacoesDTO;
import com.coinly.trasacoes.TransacoesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DashboardController extends BaseController {

    private final TransacoesService transacoesService;

    @GetMapping("/index")
    public String index(Model model,
                        Authentication authentication,
                        @RequestParam(name = "tipo", required = false) Transacoes.TipoTransacao tipo,
                        @RequestParam(name = "status", required = false) Transacoes.StatusTransacao status,
                        @RequestParam(name = "dataInicio", required = false) String dataInicio,
                        @RequestParam(name = "dataFim", required = false) String dataFim) {

        // Usuário autenticado
        addPrincipal(model, authentication);

        // Carrega e filtra (com defaults seguros)
        List<Transacoes> todas = transacoesService.findAllEntities();

        LocalDate di = parseDateOrNull(dataInicio);
        LocalDate df = parseDateOrNull(dataFim);

        List<Transacoes> filtradas = todas.stream()
                .filter(t -> tipo == null || t.getTipo() == tipo)
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> {
                    if (di == null && df == null) return true;
                    if (t.getDataHora() == null) return false;
                    LocalDate d = t.getDataHora().toLocalDate();
                    boolean okIni = (di == null) || !d.isBefore(di);
                    boolean okFim = (df == null) || !d.isAfter(df);
                    return okIni && okFim;
                })
                .sorted(Comparator.comparing(Transacoes::getDataHora,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());

        // Agregados (com null-safety)
        BigDecimal totalCredito = sumByTipoSafe(filtradas, Transacoes.TipoTransacao.CREDITO);
        BigDecimal totalDebito  = sumByTipoSafe(filtradas, Transacoes.TipoTransacao.DEBITO);
        BigDecimal saldo        = (totalCredito != null ? totalCredito : BigDecimal.ZERO)
                                .subtract(totalDebito != null ? totalDebito : BigDecimal.ZERO);

        Map<Transacoes.TipoTransacao, Long> countByTipo = filtradas.stream()
                .collect(Collectors.groupingBy(Transacoes::getTipo, Collectors.counting()));

        Map<Transacoes.StatusTransacao, Long> countByStatus = filtradas.stream()
                .collect(Collectors.groupingBy(Transacoes::getStatus, Collectors.counting()));

        long qtCredito = countByTipo.getOrDefault(Transacoes.TipoTransacao.CREDITO, 0L);
        long qtDebito  = countByTipo.getOrDefault(Transacoes.TipoTransacao.DEBITO,  0L);
        long qtPend    = countByStatus.getOrDefault(Transacoes.StatusTransacao.PENDENTE,  0L);
        long qtEfet    = countByStatus.getOrDefault(Transacoes.StatusTransacao.EFETIVADA, 0L);
        long qtCanc    = countByStatus.getOrDefault(Transacoes.StatusTransacao.CANCELADA, 0L);

        // Recentes
        List<Transacoes> recentes = filtradas.stream().limit(10).collect(Collectors.toList());

        // Atributos p/ view (NOMES SIMPLES)
        model.addAttribute("transacoes", filtradas);
        model.addAttribute("totalCredito", orZero(totalCredito));
        model.addAttribute("totalDebito",  orZero(totalDebito));
        model.addAttribute("saldo",        orZero(saldo));
        model.addAttribute("qtCredito", qtCredito);
        model.addAttribute("qtDebito",  qtDebito);
        model.addAttribute("qtPend",    qtPend);
        model.addAttribute("qtEfet",    qtEfet);
        model.addAttribute("qtCanc",    qtCanc);
        model.addAttribute("recentes",  recentes);

        // Filtros escolhidos (repassa os mesmos nomes que o form usa)
        model.addAttribute("filtroTipo", tipo);
        model.addAttribute("filtroStatus", status);
        model.addAttribute("filtroDataInicio", dataInicio);
        model.addAttribute("filtroDataFim", dataFim);

        // Objeto do modal (evita NPE)
        model.addAttribute("transacao", new TransacoesDTO());

        // DEBUG opcional: ver no log o que está indo pra view
        // model.asMap().forEach((k,v) -> System.out.println("MODEL -> " + k + " = " + (v==null?"null":v.getClass().getSimpleName())));

        return "index"; // use literal para garantir que está apontando para templates/index.html
    }

    private static LocalDate parseDateOrNull(String iso) {
        try {
            return iso != null && !iso.isBlank() ? LocalDate.parse(iso) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static BigDecimal orZero(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private static BigDecimal sumByTipoSafe(List<Transacoes> list, Transacoes.TipoTransacao tipo) {
        return list.stream()
                .filter(t -> t.getTipo() == tipo)
                .map(Transacoes::getValor)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
