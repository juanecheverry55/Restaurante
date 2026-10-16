package com.sena.adso.Prueba.repository;

import com.sena.adso.Prueba.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Método derivado opcional: Spring genera la consulta SQL automática para buscar por correo
    List<Pedido> findByClienteEmailOrderByFechaDesc(String clienteEmail);
}