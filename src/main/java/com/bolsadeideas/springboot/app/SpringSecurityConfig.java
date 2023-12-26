package com.bolsadeideas.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityConfig {


    // este metodo crea una componente BCryptPasswordEncode que sirve para cifrar y descifrar contraseñas
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // este metodo gestiona los detalles del usuario
    @Bean
    public UserDetailsService userDetailsService() throws Exception {

        // Se usa para almacenar los detalles del usuario en la memoria
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // crea un usuario con el rol de "user" y se codifica con contraseña con paswordEncoder
        manager.createUser(User.withUsername("Matias")
                .password(passwordEncoder().encode("54321"))
                .roles("USER").build());

        // crea un usuario con rol de "user" y "admin" y se codifica con contraseña con paswordEncoder
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("12345"))
                .roles("ADMIN", "USER").build());

        // retorna el objeto manager que contiene los usuario con sus roles y contraseñas
        return manager;
    }

    // este método define los filtros de seguridad de la aplicacion
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // está solicitud permite acceso a todos a ciertas rutas, y otras las protege
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/js/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/images/**"),
                        new AntPathRequestMatcher("/listar")).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/ver/**"),
                        new AntPathRequestMatcher("/uploads/**")
                        ).hasRole("USER")
                .requestMatchers(
                        new AntPathRequestMatcher("/form/**"),
                        new AntPathRequestMatcher("/eliminar/**"),
                        new AntPathRequestMatcher("/factura/**")
                ).hasRole("ADMIN")
                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error_403")
                );



        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}