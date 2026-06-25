package com.proyecto.menu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GatewayHeaderFilter gatewayHeaderFilter;

    public SecurityConfig(GatewayHeaderFilter gatewayHeaderFilter) {
        this.gatewayHeaderFilter = gatewayHeaderFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. Permitir acceso público y libre a la documentación de Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2. Protegemos estrictamente todos los endpoints del menú
                        .requestMatchers("/api/menu/**").authenticated()

                        // 3. Cualquier otra ruta residual también requerirá autenticación
                        .anyRequest().authenticated()
                )
                // Enganchamos el filtro interceptor antes del flujo interno de Spring
                .addFilterBefore(gatewayHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}