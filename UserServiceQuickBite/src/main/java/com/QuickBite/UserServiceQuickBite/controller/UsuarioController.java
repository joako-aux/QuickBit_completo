package com.QuickBite.UserServiceQuickBite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import com.QuickBite.UserServiceQuickBite.model.Usuario;
import com.QuickBite.UserServiceQuickBite.service.UsuarioService;



@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearPerfil(@RequestBody Usuario us) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(us);
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
    public ResponseEntity<Usuario> actualizarPerfil(@PathVariable UUID id, @RequestBody Usuario usuario) {
        Usuario usuarioEditado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioEditado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable UUID id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}