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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) {
        logger.info("Tentativa de registro de usuário: {}", registroDTO.getUsername());
        return criarUsuario(registroDTO, Roles.USER);
    }

    @Transactional
    public UsuarioDTO registrarAdmin(RegistroDTO registroDTO) {
        logger.info("Tentativa de registro de administrador: {}", registroDTO.getUsername());
        return criarUsuario(registroDTO, Roles.ADMIN);
    }

    private UsuarioDTO criarUsuario(RegistroDTO registroDTO, String roleNome) {
        if (usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            logger.warn("Tentativa de registro com username já existente: {}", registroDTO.getUsername());
            throw new UsernameAlreadyExistsException("Nome de usuário já está em uso");
        }

        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            logger.warn("Tentativa de registro com email já existente: {}", registroDTO.getEmail());
            throw new EmailAlreadyExistsException("Email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registroDTO.getUsername());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Role role = roleRepository.findByNome(roleNome)
                .orElseGet(() -> {
                    logger.info("Criando nova role: {}", roleNome);
                    Role novaRole = new Role(roleNome);
                    return roleRepository.save(novaRole);
                });

        usuario.addRole(role);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        logger.info("Usuário registrado com sucesso: {} com role {}", usuarioSalvo.getUsername(), roleNome);
        
        return new UsuarioDTO(usuarioSalvo);
    }
}

