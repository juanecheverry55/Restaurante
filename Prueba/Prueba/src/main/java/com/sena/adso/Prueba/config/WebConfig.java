package com.sena.adso.Prueba.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtiene la ruta absoluta de la carpeta 'uploads' dentro del proyecto
        String uploadDir = Paths.get("uploads").toAbsolutePath().toUri().toString();

        // Expone http://localhost:8080/uploads/** apuntando a esa carpeta física
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadDir);
    }
}