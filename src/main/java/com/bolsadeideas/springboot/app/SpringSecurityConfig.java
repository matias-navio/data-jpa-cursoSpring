package com.bolsadeideas.springboot.app;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.services.JpaUserDatailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
     private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserDatailService userDatailService;

    // este metodo gestiona los detalles del usuario
    /*@Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder build) throws Exception {

        // Se usa para almacenar los detalles del usuario en la memoria
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // crea un usuario con el rol de "user" y se codifica con contraseña con paswordEncoder
        manager.createUser(User.withUsername("Matias2")
                .password(passwordEncoder.encode("12345"))
                .roles("USER").build());

        // crea un usuario con rol de "user" y "admin" y se codifica con contraseña con paswordEncoder
        manager.createUser(User.withUsername("Matias")
                .password(passwordEncoder.encode("12345"))
                .roles("ADMIN").build());

        // retorna el objeto manager que contiene los usuario con sus roles y contraseñas
        return manager;
    }*/

    // metodo para configurar la autenticacion de la app, mediante JDBC
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {

        // indica que se utiliza autenticacion manejada con JDBC
        build.userDetailsService(userDatailService)
                .passwordEncoder(passwordEncoder); // codificador de contraseñas, verifica las contraseñas de la base de datos

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
                        new AntPathRequestMatcher("/listar"),
                        new AntPathRequestMatcher("/locale"))
                        .permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/ver/**"),
                        new AntPathRequestMatcher("/uploads/**")
                        ).hasRole("USER")
                .requestMatchers(
                        new AntPathRequestMatcher("/form/**"),
                        new AntPathRequestMatcher("/eliminar/**"),
                        new AntPathRequestMatcher("/factura/**"),
                        new AntPathRequestMatcher("/listar**")
                ).hasRole("ADMIN")
                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .successHandler(successHandler)
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