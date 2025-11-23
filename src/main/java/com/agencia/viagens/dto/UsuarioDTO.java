package com.agencia.viagens.dto;

import com.agencia.viagens.model.Usuario;
import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioDTO {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
        this.roles = usuario.getRoles().stream()
                .map(role -> role.getNome())
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

