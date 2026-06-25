package com.proyecto.pagos.repository;

import com.proyecto.pagos.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface PagoRepository extends JpaRepository<Pago, UUID> {
}
