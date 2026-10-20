package com.sena.adso.Prueba.repository;

import com.sena.adso.Prueba.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}