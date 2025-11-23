package com.agencia.viagens.controller;

import com.agencia.viagens.dto.ApiResponse;
import com.agencia.viagens.dto.AvaliacaoDTO;
import com.agencia.viagens.dto.DestinoDTO;
import com.agencia.viagens.service.DestinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    @Autowired
    private DestinoService destinoService;

    @PostMapping
    public ResponseEntity<ApiResponse<DestinoDTO>> cadastrarDestino(@Valid @RequestBody DestinoDTO destinoDTO) {
        DestinoDTO novoDestino = destinoService.cadastrarDestino(destinoDTO);
        ApiResponse<DestinoDTO> response = ApiResponse.success("Destino cadastrado com sucesso", novoDestino);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DestinoDTO>>> listarDestinos(
            @RequestParam(required = false) Boolean disponiveis) {
        List<DestinoDTO> destinos;
        
        if (disponiveis != null && disponiveis) {
            destinos = destinoService.listarDestinosDisponiveis();
        } else {
            destinos = destinoService.listarTodosDestinos();
        }
        
        ApiResponse<List<DestinoDTO>> response = ApiResponse.success(destinos);
        HttpStatus status = destinos.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<List<DestinoDTO>>> pesquisarDestinos(
            @RequestParam(required = false) String termo) {
        List<DestinoDTO> destinos = destinoService.pesquisarDestinos(termo);
        
        ApiResponse<List<DestinoDTO>> response = ApiResponse.success(destinos);
        HttpStatus status = destinos.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinoDTO>> buscarDestinoPorId(@PathVariable Long id) {
        DestinoDTO destino = destinoService.buscarDestinoPorId(id);
        ApiResponse<DestinoDTO> response = ApiResponse.success(destino);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<ApiResponse<DestinoDTO>> avaliarDestino(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
        DestinoDTO destinoAtualizado = destinoService.avaliarDestino(id, avaliacaoDTO);
        ApiResponse<DestinoDTO> response = ApiResponse.success("Avaliação registrada com sucesso", destinoAtualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinoDTO>> atualizarDestino(
            @PathVariable Long id,
            @Valid @RequestBody DestinoDTO destinoDTO) {
        DestinoDTO destinoAtualizado = destinoService.atualizarDestino(id, destinoDTO);
        ApiResponse<DestinoDTO> response = ApiResponse.success("Destino atualizado com sucesso", destinoAtualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirDestino(@PathVariable Long id) {
        destinoService.excluirDestino(id);
        ApiResponse<Void> response = ApiResponse.success("Destino excluído com sucesso", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/avaliacao")
    public ResponseEntity<ApiResponse<List<DestinoDTO>>> buscarPorAvaliacaoMinima(
            @RequestParam Double minima) {
        List<DestinoDTO> destinos = destinoService.buscarPorAvaliacaoMinima(minima);
        
        ApiResponse<List<DestinoDTO>> response = ApiResponse.success(destinos);
        HttpStatus status = destinos.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }
}
