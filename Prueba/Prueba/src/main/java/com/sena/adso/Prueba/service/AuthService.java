package com.sena.adso.Prueba.service;

import com.sena.adso.Prueba.model.Usuario;
import com.sena.adso.Prueba.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    public boolean solicitarRecuperacion(String email) {
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);
        if (optUsuario.isEmpty()) {
            return false;
        }

        Usuario usuario = optUsuario.get();
        // Generar código de 6 dígitos
        String codigo = String.valueOf((int)(Math.random() * 900000) + 100000);

        usuario.setResetToken(codigo);
        usuario.setTokenExpiration(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);

        // Envío del correo
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setSubject("Restaurante - Código de Recuperación");
        mensaje.setText("Hola " + usuario.getNombre() + ",\n\nTu código para restablecer la contraseña es: " + codigo + "\nEste código expira en 15 minutos.");

        mailSender.send(mensaje);
        return true;
    }

    public boolean cambiarPasswordConCodigo(String email, String codigo, String nuevaPassword) {
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);
        if (optUsuario.isEmpty()) return false;

        Usuario usuario = optUsuario.get();

        if (usuario.getResetToken() == null || 
            !usuario.getResetToken().equals(codigo) || 
            LocalDateTime.now().isAfter(usuario.getTokenExpiration())) {
            return false;
        }

        usuario.setPassword(nuevaPassword);
        usuario.setResetToken(null);
        usuario.setTokenExpiration(null);
        usuarioRepository.save(usuario);

        return true;
    }
}
