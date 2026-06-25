package com.proyecto.menu.repository;

import com.proyecto.menu.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {
}
