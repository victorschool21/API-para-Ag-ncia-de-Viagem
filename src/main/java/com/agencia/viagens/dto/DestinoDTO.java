package com.agencia.viagens.dto;

import com.agencia.viagens.model.Destino;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class DestinoDTO {

    private Long id;

    @NotBlank(message = "O nome do destino é obrigatório")
    private String nome;

    @NotBlank(message = "A localização é obrigatória")
    private String localizacao;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @DecimalMin(value = "0.0", message = "O preço deve ser positivo")
    private Double preco;

    private Double avaliacaoMedia;
    private Integer totalAvaliacoes;
    private String foto;
    private Boolean disponivel;

    public DestinoDTO() {
    }

    public DestinoDTO(Destino destino) {
        this.id = destino.getId();
        this.nome = destino.getNome();
        this.localizacao = destino.getLocalizacao();
        this.descricao = destino.getDescricao();
        this.preco = destino.getPreco();
        this.avaliacaoMedia = destino.getAvaliacaoMedia();
        this.totalAvaliacoes = destino.getTotalAvaliacoes();
        this.foto = destino.getFoto();
        this.disponivel = destino.getDisponivel();
    }

    public Destino toEntity() {
        Destino destino = new Destino();
        destino.setNome(this.nome);
        destino.setLocalizacao(this.localizacao);
        destino.setDescricao(this.descricao);
        destino.setPreco(this.preco);
        destino.setFoto(this.foto);
        if (this.disponivel != null) {
            destino.setDisponivel(this.disponivel);
        }
        return destino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}
