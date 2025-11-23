package com.agencia.viagens.controller;

import com.agencia.viagens.dto.ApiResponse;
import com.agencia.viagens.dto.JwtResponse;
import com.agencia.viagens.dto.LoginDTO;
import com.agencia.viagens.dto.RegistroDTO;
import com.agencia.viagens.dto.UsuarioDTO;
import com.agencia.viagens.model.Usuario;
import com.agencia.viagens.security.JwtTokenProvider;
import com.agencia.viagens.security.SecurityAuditLogger;
import com.agencia.viagens.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private SecurityAuditLogger auditLogger;

    @Test
    void testRegistrarUsuario_Success() throws Exception {
        // Arrange
        RegistroDTO registroDTO = new RegistroDTO();
        registroDTO.setUsername("testuser");
        registroDTO.setEmail("test@email.com");
        registroDTO.setPassword("Test123!@#");

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("testuser");
        usuarioDTO.setEmail("test@email.com");

        when(authService.registrarUsuario(any(RegistroDTO.class))).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("Test123!@#");

        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@email.com");

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("test-jwt-token");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"));
    }
}

