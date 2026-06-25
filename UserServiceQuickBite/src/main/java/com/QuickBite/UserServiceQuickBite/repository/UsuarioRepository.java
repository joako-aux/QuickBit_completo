package com.QuickBite.UserServiceQuickBite.repository;


import com.QuickBite.UserServiceQuickBite.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {


}
