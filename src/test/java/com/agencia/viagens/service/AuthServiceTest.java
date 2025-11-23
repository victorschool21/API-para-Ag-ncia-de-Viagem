package com.agencia.viagens.service;

import com.agencia.viagens.constants.Roles;
import com.agencia.viagens.dto.RegistroDTO;
import com.agencia.viagens.dto.UsuarioDTO;
import com.agencia.viagens.exception.EmailAlreadyExistsException;
import com.agencia.viagens.exception.UsernameAlreadyExistsException;
import com.agencia.viagens.model.Role;
import com.agencia.viagens.model.Usuario;
import com.agencia.viagens.repository.RoleRepository;
import com.agencia.viagens.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegistroDTO registroDTO;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        registroDTO = new RegistroDTO();
        registroDTO.setUsername("testuser");
        registroDTO.setEmail("test@email.com");
        registroDTO.setPassword("Test123!@#");

        roleUser = new Role(Roles.USER);
        roleUser.setId(1L);
    }

    @Test
    void testRegistrarUsuario_Success() {
        // Arrange
        when(usuarioRepository.existsByUsername(registroDTO.getUsername())).thenReturn(false);
        when(usuarioRepository.existsByEmail(registroDTO.getEmail())).thenReturn(false);
        when(roleRepository.findByNome(Roles.USER)).thenReturn(Optional.of(roleUser));
        when(passwordEncoder.encode(registroDTO.getPassword())).thenReturn("encodedPassword");
        
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setUsername(registroDTO.getUsername());
        usuarioSalvo.setEmail(registroDTO.getEmail());
        usuarioSalvo.addRole(roleUser);
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        UsuarioDTO result = authService.registrarUsuario(registroDTO);

        // Assert
        assertNotNull(result);
        assertEquals(registroDTO.getUsername(), result.getUsername());
        assertEquals(registroDTO.getEmail(), result.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_UsernameAlreadyExists() {
        // Arrange
        when(usuarioRepository.existsByUsername(registroDTO.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            authService.registrarUsuario(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_EmailAlreadyExists() {
        // Arrange
        when(usuarioRepository.existsByUsername(registroDTO.getUsername())).thenReturn(false);
        when(usuarioRepository.existsByEmail(registroDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.registrarUsuario(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}

