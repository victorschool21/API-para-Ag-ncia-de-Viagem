package com.agencia.viagens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//Classe principal da aplicação Spring Boot

@SpringBootApplication
public class ViagensApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViagensApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("API de Viagens está rodando!");
        System.out.println("Acesse: http://localhost:8080/api/destinos");
        System.out.println("Console H2: http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}

