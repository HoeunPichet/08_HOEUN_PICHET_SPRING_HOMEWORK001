package com.manage.ticket.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Ticketing System for Public Transport",
        version = "1.0.0",
        description = "The Ticketing System for Public Transport is a Spring Boot application for managing ticket bookings, payments, and scheduling.",
        contact = @Contact(
            name = "GitHub",
            url = "https://github.com/HoeunPichet"
        )

    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Server"),
        @Server(url = "http://110.74.194.124:9090", description = "Homework Server"),
    }
)
public class ConfigOpenAPI {

}