package com.coinly.trasacoes;

import com.coinly.helper.AppConstants;
import com.coinly.helper.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de transações.
 * Implementa BaseService para padronização de operações CRUD.
 */
@Service
@RequiredArgsConstructor
public class TransacoesService implements BaseService<Transacoes, TransacoesDTO> {

    private final TransacoesRepository transacoesRepository;

    @Override
    public List<TransacoesDTO> findAll() {
        return transacoesRepository.findAll()
                .stream()
                .map(TransacoesDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TransacoesDTO findById(Long id) {
        return transacoesRepository.findById(id)
                .map(TransacoesDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException(AppConstants.ERR_TRANSACAO_NAO_ENCONTRADA));
    }

    @Override
    @Transactional
    public TransacoesDTO save(TransacoesDTO dto) {
        Transacoes entity;
        if (dto.getId() != null) {
            // Atualização - busca entidade existente
            entity = transacoesRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException(AppConstants.ERR_TRANSACAO_NAO_ENCONTRADA));
            applyDtoToEntity(dto, entity);
        } else {
            // Criação - nova entidade
            entity = toEntity(dto);
        }

        Transacoes saved = transacoesRepository.save(entity);
        return TransacoesDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!transacoesRepository.existsById(id)) {
            throw new RuntimeException(AppConstants.ERR_TRANSACAO_NAO_ENCONTRADA);
        }
        transacoesRepository.deleteById(id);
    }

    @Override
    public TransacoesDTO toDTO(Transacoes entity) {
        return TransacoesDTO.fromEntity(entity);
    }

    @Override
    public Transacoes toEntity(TransacoesDTO dto) {
        Transacoes t = new Transacoes();
        t.setId(dto.getId());
        t.setDescricao(dto.getDescricao());
        t.setValor(dto.getValor());
        t.setTipo(dto.getTipo());
        t.setStatus(dto.getStatus());
        t.setDataHora(dto.getDataHora());
        t.setCategoria(dto.getCategoria());
        t.setMoeda(dto.getMoeda());
        t.setObservacao(dto.getObservacao());
        return t;
    }

    private void applyDtoToEntity(TransacoesDTO dto, Transacoes t) {
        t.setDescricao(dto.getDescricao());
        t.setValor(dto.getValor());
        t.setTipo(dto.getTipo());
        t.setStatus(dto.getStatus());
        t.setDataHora(dto.getDataHora());
        t.setCategoria(dto.getCategoria());
        t.setMoeda(dto.getMoeda());
        t.setObservacao(dto.getObservacao());
    }

    // Métodos adicionais para compatibilidade com DashboardController

    /** Busca todas as transações como entidades (para o dashboard). */
    public List<Transacoes> findAllEntities() {
        return transacoesRepository.findAll();
    }

    /** Busca transação por ID como entidade (para o dashboard). */
    public Transacoes findByIdEntity(Long id) {
        return transacoesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(AppConstants.ERR_TRANSACAO_NAO_ENCONTRADA));
    }
}
