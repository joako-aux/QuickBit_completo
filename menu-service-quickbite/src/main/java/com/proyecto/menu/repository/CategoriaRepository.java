package com.proyecto.menu.repository;

import com.proyecto.menu.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
}
