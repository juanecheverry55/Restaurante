package com.sena.adso.Prueba.controller;

import com.sena.adso.Prueba.model.Usuario;
import com.sena.adso.Prueba.repository.UsuarioRepository;
import com.sena.adso.Prueba.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está registrado.");
        }
        
        // Todos los nuevos usuarios nacen como CLIENTE por defecto
        usuario.setRol("CLIENTE");

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody Usuario datosLogin) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(datosLogin.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(datosLogin.getPassword())) {
                return ResponseEntity.ok(usuario);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo o contraseña incorrectos.");
    }

    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<String> solicitarRecuperacion(@RequestParam String email) {
        boolean enviado = authService.solicitarRecuperacion(email);
        if (enviado) {
            return ResponseEntity.ok("Código enviado exitosamente a tu correo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El correo no se encuentra registrado.");
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<String> restablecerPassword(
            @RequestParam String email,
            @RequestParam String codigo,
            @RequestParam String nuevaPassword) {

        boolean exito = authService.cambiarPasswordConCodigo(email, codigo, nuevaPassword);
        if (exito) {
            return ResponseEntity.ok("Contraseña restablecida exitosamente.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código es incorrecto o ha expirado.");
    }
}