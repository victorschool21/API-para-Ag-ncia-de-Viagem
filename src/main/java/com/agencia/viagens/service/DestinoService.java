package com.agencia.viagens.service;

import com.agencia.viagens.dto.AvaliacaoDTO;
import com.agencia.viagens.dto.DestinoDTO;
import com.agencia.viagens.model.Destino;
import com.agencia.viagens.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinoService {

    @Autowired
    private DestinoRepository destinoRepository;

    @Transactional
    public DestinoDTO cadastrarDestino(DestinoDTO destinoDTO) {
        Destino destino = destinoDTO.toEntity();
        Destino destinoSalvo = destinoRepository.save(destino);
        return new DestinoDTO(destinoSalvo);
    }

    @Transactional(readOnly = true)
    public List<DestinoDTO> listarTodosDestinos() {
        return destinoRepository.findAll()
                .stream()
                .map(DestinoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DestinoDTO> pesquisarDestinos(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodosDestinos();
        }
        
        return destinoRepository.buscarPorNomeOuLocalizacao(termo)
                .stream()
                .map(DestinoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DestinoDTO buscarDestinoPorId(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destino não encontrado com ID: " + id));
        return new DestinoDTO(destino);
    }

    @Transactional
    public DestinoDTO avaliarDestino(Long id, AvaliacaoDTO avaliacaoDTO) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destino não encontrado com ID: " + id));

        Double somaAvaliacoes = destino.getAvaliacaoMedia() * destino.getTotalAvaliacoes();
        somaAvaliacoes += avaliacaoDTO.getNota();
        
        Integer novoTotal = destino.getTotalAvaliacoes() + 1;
        Double novaMedia = somaAvaliacoes / novoTotal;

        destino.setAvaliacaoMedia(Math.round(novaMedia * 100.0) / 100.0);
        destino.setTotalAvaliacoes(novoTotal);

        Destino destinoAtualizado = destinoRepository.save(destino);
        return new DestinoDTO(destinoAtualizado);
    }

    @Transactional
    public DestinoDTO atualizarDestino(Long id, DestinoDTO destinoDTO) {
        Destino destinoExistente = destinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destino não encontrado com ID: " + id));

        destinoExistente.setNome(destinoDTO.getNome());
        destinoExistente.setLocalizacao(destinoDTO.getLocalizacao());
        destinoExistente.setDescricao(destinoDTO.getDescricao());
        destinoExistente.setPreco(destinoDTO.getPreco());
        
        if (destinoDTO.getFoto() != null) {
            destinoExistente.setFoto(destinoDTO.getFoto());
        }
        
        if (destinoDTO.getDisponivel() != null) {
            destinoExistente.setDisponivel(destinoDTO.getDisponivel());
        }

        Destino destinoAtualizado = destinoRepository.save(destinoExistente);
        return new DestinoDTO(destinoAtualizado);
    }

    @Transactional
    public void excluirDestino(Long id) {
        if (!destinoRepository.existsById(id)) {
            throw new RuntimeException("Destino não encontrado com ID: " + id);
        }
        destinoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DestinoDTO> listarDestinosDisponiveis() {
        return destinoRepository.findByDisponivelTrue()
                .stream()
                .map(DestinoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DestinoDTO> buscarPorAvaliacaoMinima(Double avaliacaoMinima) {
        return destinoRepository.findByAvaliacaoMediaGreaterThanEqual(avaliacaoMinima)
                .stream()
                .map(DestinoDTO::new)
                .collect(Collectors.toList());
    }
}
