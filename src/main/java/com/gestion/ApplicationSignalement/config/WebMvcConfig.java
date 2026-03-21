package com.gestion.ApplicationSignalement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Web MVC :
 * 1. Enregistre l'intercepteur global qui injecte le rôle dans chaque vue
 * 2. Configure le serveur de fichiers statiques (photos uploadées)
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalModelInterceptor globalModelInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalModelInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/js/**", "/images/**",
                        "/webjars/**", "/favicon.ico"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Accès aux photos uploadées via /uploads/...
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}