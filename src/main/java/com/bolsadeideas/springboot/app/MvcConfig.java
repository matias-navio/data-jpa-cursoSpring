package com.bolsadeideas.springboot.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        log.info(resourcePath);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath );
    }

    /*
        sirve para manerjar el error 403 que muestra el navegador cuando intentamos acceder a funciones
        que no se pueden como usuario por no tener permisos
    */
    public void addViewControllers(ViewControllerRegistry registery){

        registery.addViewController("/error_403").setViewName("error_403");
    }

    // este metodo crea un componente BCryptPasswordEncode que sirve para cifrar y descifrar contraseñas
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
