package com.agencia.viagens.repository;

import com.agencia.viagens.model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Long> {

    List<Destino> findByNomeContainingIgnoreCase(String nome);

    List<Destino> findByLocalizacaoContainingIgnoreCase(String localizacao);

    @Query("SELECT d FROM Destino d WHERE " +
           "LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(d.localizacao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Destino> buscarPorNomeOuLocalizacao(@Param("termo") String termo);

    List<Destino> findByDisponivelTrue();

    List<Destino> findByAvaliacaoMediaGreaterThanEqual(Double avaliacaoMinima);
}
