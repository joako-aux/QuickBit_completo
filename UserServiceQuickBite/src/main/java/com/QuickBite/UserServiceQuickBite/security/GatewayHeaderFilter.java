package com.QuickBite.UserServiceQuickBite.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class GatewayHeaderFilter extends OncePerRequestFilter {

    // Método que le dice a Spring Security que NO ejecute este filtro para las rutas de Swagger
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extraemos las cabeceras HTTP que vienen desde el APIGateway
        String email = request.getHeader("X-User-Email");
        String role = request.getHeader("X-User-Role");

        // 2. Si las cabeceras existen, registramos al usuario en el contexto de Spring Security
        if (email != null && role != null) {

            // Creamos la autoridad con el prefijo ROLE_ (Ej: ROLE_CLIENTE)
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email, // Principal (usuario)
                            null,  // Credentials (no se necesitan contraseñas aquí)
                            List.of(authority) // Roles/Permisos
                    );

            // Guardamos la autenticación en el contexto del hilo actual
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("USER-SERVICE - Headers interceptados con éxito. Email: " + email + " | Rol: " + role);
        }

        // 3. Continuar con la ejecución de la petición
        filterChain.doFilter(request, response);
    }
}