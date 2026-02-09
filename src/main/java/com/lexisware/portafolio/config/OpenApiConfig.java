package com.lexisware.portafolio.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuración de Swagger/OpenAPI para la documentación de la API
@Configuration
public class OpenApiConfig {

        // Define la información general y el esquema de seguridad JWT para la
        // documentación
        @Bean
        public OpenAPI myOpenAPI() {
                Contact contact = new Contact();
                contact.setEmail("pallchizacaalexis@gmail.com");
                contact.setName("LEXISWARE");
                contact.setUrl("https://lexisware.vercel.app");

                License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

                Info info = new Info()
                                .title("LEXISWARE Portafolio API")
                                .version("1.0.0")
                                .contact(contact)
                                .description("API REST para gestión de portafolios, proyectos y asesorías.")
                                .license(mitLicense);

                SecurityScheme securityScheme = new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .name("JWT Authentication");

                Components components = new Components()
                                .addSecuritySchemes("bearerAuth", securityScheme);

                SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

                return new OpenAPI().info(info).components(components).addSecurityItem(securityRequirement);
        }
}
