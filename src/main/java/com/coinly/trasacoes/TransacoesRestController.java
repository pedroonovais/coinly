package com.coinly.trasacoes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacoesRestController {

    private final TransacoesService transacoesService;

    @GetMapping
    public List<TransacoesDTO> listar() {
        return transacoesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacoesDTO> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(transacoesService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TransacoesDTO> criar(@Valid @RequestBody TransacoesDTO dto) {
        TransacoesDTO salvo = transacoesService.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacoesDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody TransacoesDTO dto) {
        dto.setId(id);
        try {
            return ResponseEntity.ok(transacoesService.save(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            transacoesService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
