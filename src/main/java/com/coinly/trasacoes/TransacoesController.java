package com.coinly.trasacoes;

import com.coinly.helper.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacoesController extends BaseController {

    private final TransacoesService transacoesService;

    // Sempre redireciona a listagem para o dashboard
    @GetMapping
    public String listRedirect() {
        return "redirect:/index";
    }

    @GetMapping("/form")
    public String form(TransacoesDTO dto, Model model, Authentication auth) {
        addPrincipal(model, auth);
        model.addAttribute("transacao", dto != null ? dto : new TransacoesDTO());
        return "transacoes/form";
    }

    @PostMapping("/form")
    public String create(@Valid TransacoesDTO dto,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Model model,
                         Authentication auth) {
        if (result.hasErrors()) {
            addPrincipal(model, auth);
            model.addAttribute("transacao", dto);
            return "transacoes/form";
        }
        transacoesService.save(dto);
        redirect.addFlashAttribute("message", "Transação salva com sucesso!");
        return "redirect:/index";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication auth) {
        addPrincipal(model, auth);
        model.addAttribute("transacao", transacoesService.findById(id));
        return "transacoes/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid TransacoesDTO dto,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Model model,
                         Authentication auth) {
        if (result.hasErrors()) {
            addPrincipal(model, auth);
            model.addAttribute("transacao", dto);
            return "transacoes/form";
        }
        dto.setId(id);
        transacoesService.save(dto);
        redirect.addFlashAttribute("message", "Transação atualizada com sucesso!");
        return "redirect:/index";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        transacoesService.delete(id);
        redirect.addFlashAttribute("message", "Transação removida com sucesso!");
        return "redirect:/index";
    }
}
