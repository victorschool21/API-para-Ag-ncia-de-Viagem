package com.agencia.viagens.controller;

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
@CrossOrigin(origins = "*")
public class DestinoController {

    @Autowired
    private DestinoService destinoService;

    @PostMapping
    public ResponseEntity<DestinoDTO> cadastrarDestino(@Valid @RequestBody DestinoDTO destinoDTO) {
        try {
            DestinoDTO novoDestino = destinoService.cadastrarDestino(destinoDTO);
            return new ResponseEntity<>(novoDestino, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<DestinoDTO>> listarDestinos(
            @RequestParam(required = false) Boolean disponiveis) {
        try {
            List<DestinoDTO> destinos;
            
            if (disponiveis != null && disponiveis) {
                destinos = destinoService.listarDestinosDisponiveis();
            } else {
                destinos = destinoService.listarTodosDestinos();
            }
            
            if (destinos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(destinos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<DestinoDTO>> pesquisarDestinos(
            @RequestParam(required = false) String termo) {
        try {
            List<DestinoDTO> destinos = destinoService.pesquisarDestinos(termo);
            
            if (destinos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(destinos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinoDTO> buscarDestinoPorId(@PathVariable Long id) {
        try {
            DestinoDTO destino = destinoService.buscarDestinoPorId(id);
            return new ResponseEntity<>(destino, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<DestinoDTO> avaliarDestino(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
        try {
            DestinoDTO destinoAtualizado = destinoService.avaliarDestino(id, avaliacaoDTO);
            return new ResponseEntity<>(destinoAtualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinoDTO> atualizarDestino(
            @PathVariable Long id,
            @Valid @RequestBody DestinoDTO destinoDTO) {
        try {
            DestinoDTO destinoAtualizado = destinoService.atualizarDestino(id, destinoDTO);
            return new ResponseEntity<>(destinoAtualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> excluirDestino(@PathVariable Long id) {
        try {
            destinoService.excluirDestino(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/avaliacao")
    public ResponseEntity<List<DestinoDTO>> buscarPorAvaliacaoMinima(
            @RequestParam Double minima) {
        try {
            List<DestinoDTO> destinos = destinoService.buscarPorAvaliacaoMinima(minima);
            
            if (destinos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(destinos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
