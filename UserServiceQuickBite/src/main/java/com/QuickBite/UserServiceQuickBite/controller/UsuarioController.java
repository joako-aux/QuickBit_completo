package com.QuickBite.UserServiceQuickBite.controller;

import com.QuickBite.UserServiceQuickBite.model.Usuario;
import com.QuickBite.UserServiceQuickBite.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> crearPerfil(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> consultarUsuario(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.consultarUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarPerfil(
            @PathVariable UUID id,
            @Valid @RequestBody Usuario usuario) {

        Usuario usuarioEditado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioEditado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable UUID id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}