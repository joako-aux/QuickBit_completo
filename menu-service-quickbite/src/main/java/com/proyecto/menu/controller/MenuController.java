package com.proyecto.menu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @PostMapping("/products")
    public String createProduct(

            @RequestHeader("X-User-Email")
            String email,

            @RequestHeader("X-User-Role")
            String role
    ) {

        if (!role.equals("VENDEDOR")) {

            throw new RuntimeException(
                    "No autorizado"
            );
        }

        return "Producto creado por: " + email;
    }
}