package com.agencia.viagens.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AvaliacaoDTO {

    @NotNull(message = "A nota é obrigatória")
    @DecimalMin(value = "1.0", message = "A nota mínima é 1")
    @DecimalMax(value = "10.0", message = "A nota máxima é 10")
    private Double nota;

    public AvaliacaoDTO() {
    }

    public AvaliacaoDTO(Double nota) {
        this.nota = nota;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
