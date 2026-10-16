package com.sena.adso.Prueba.repository;

import com.sena.adso.Prueba.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método existente
    Optional<Usuario> findByEmail(String email);

    // MÉTODOS AÑADIDOS PARA EL TOKEN DE RECUPERACIÓN:
    Optional<Usuario> findByResetToken(String resetToken);
}