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
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private SecurityAuditLogger auditLogger;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<UsuarioDTO>> registrarUsuario(
            @Valid @RequestBody RegistroDTO registroDTO,
            HttpServletRequest request) {
        logger.info("Requisição de registro recebida para usuário: {}", registroDTO.getUsername());
        UsuarioDTO usuarioDTO = authService.registrarUsuario(registroDTO);
        auditLogger.logUserCreated(usuarioDTO.getUsername(), "SELF_REGISTRATION");
        ApiResponse<UsuarioDTO> response = ApiResponse.success("Usuário registrado com sucesso", usuarioDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginDTO loginDTO,
            HttpServletRequest request) {
        logger.info("Tentativa de login para usuário: {}", loginDTO.getUsername());
        String ipAddress = request.getRemoteAddr();
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String jwt = tokenProvider.generateToken(authentication);
            UsuarioDTO usuarioDTO = new UsuarioDTO((Usuario) authentication.getPrincipal());
            JwtResponse jwtResponse = new JwtResponse(jwt, usuarioDTO);
            
            auditLogger.logSuccessfulLogin(loginDTO.getUsername(), ipAddress);
            logger.info("Login bem-sucedido para usuário: {}", loginDTO.getUsername());
            
            ApiResponse<JwtResponse> response = ApiResponse.success("Login realizado com sucesso", jwtResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            auditLogger.logFailedLogin(loginDTO.getUsername(), ipAddress, "Credenciais inválidas");
            logger.warn("Tentativa de login com credenciais inválidas para usuário: {}", loginDTO.getUsername());
            throw e;
        }
    }

    @GetMapping("/usuario-atual")
    public ResponseEntity<ApiResponse<UsuarioDTO>> getUsuarioAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() 
                && authentication.getPrincipal() instanceof Usuario) {
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    (Usuario) authentication.getPrincipal()
            );
            ApiResponse<UsuarioDTO> response = ApiResponse.success(usuarioDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        ApiResponse<UsuarioDTO> response = ApiResponse.error("Usuário não autenticado");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}

