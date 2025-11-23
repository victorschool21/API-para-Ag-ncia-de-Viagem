package com.agencia.viagens.config;

import com.agencia.viagens.constants.Roles;
import com.agencia.viagens.model.Role;
import com.agencia.viagens.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByNome(Roles.USER)) {
            Role roleUser = new Role(Roles.USER);
            roleRepository.save(roleUser);
            logger.info("Role {} criado com sucesso", Roles.USER);
        }

        if (!roleRepository.existsByNome(Roles.ADMIN)) {
            Role roleAdmin = new Role(Roles.ADMIN);
            roleRepository.save(roleAdmin);
            logger.info("Role {} criado com sucesso", Roles.ADMIN);
        }
    }
}

