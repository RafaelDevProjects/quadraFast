package com.quadrafast.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI quadraFastOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QuadraFast API")
                        .description("""
                                API REST para reserva de quadras esportivas.
                                
                                **CP3 — Diagnóstico Arquitetural e Evolução de APIs**
                                FIAP · 3ESPR · 2026 · Profa. Damiana Costa
                                
                                ### Códigos de resposta padrão
                                | Status | Significado |
                                |--------|-------------|
                                | 201 | Reserva criada com sucesso |
                                | 400 | Dados de entrada inválidos |
                                | 404 | Quadra ou usuário não encontrado |
                                | 409 | Conflito de horário |
                                | 422 | Quadra inativa |
                                | 500 | Erro interno |
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipe QuadraFast")
                                .email("quadrafast@fiap.com.br")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ));
    }
}
