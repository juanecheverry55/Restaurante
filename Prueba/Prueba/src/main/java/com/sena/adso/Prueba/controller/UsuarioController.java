package com.sena.adso.Prueba.controller;

import com.sena.adso.Prueba.model.Usuario;
import com.sena.adso.Prueba.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // 2. Obtener perfil de usuario por Email (para el modal "Mi Perfil")
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestParam String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            return ResponseEntity.ok(usuarioOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Actualizar perfil de usuario (para guardar cambios en "Mi Perfil")
    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestBody Usuario datos) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(datos.getEmail());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(datos.getNombre());
            usuario.setTelefono(datos.getTelefono());
            usuario.setDireccion(datos.getDireccion());
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Datos actualizados correctamente"));
        }
        return ResponseEntity.notFound().build();
    }

    // 4. Registro de nuevos usuarios
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo electrónico ya está registrado."));
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("CLIENTE");
        }

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // 5. Autenticación / Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginData) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginData.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(loginData.getPassword())) {
                return ResponseEntity.ok(usuario);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales incorrectas (Email o contraseña)"));
    }

    // 6. PASO 1: Solicitar recuperación (Genera Token con expiración)
    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo electrónico es obligatorio."));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email.trim());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Generar token único y expiración a 15 minutos
            String token = UUID.randomUUID().toString();
            usuario.setResetToken(token);
            usuario.setTokenExpiration(LocalDateTime.now().plusMinutes(15));
            usuarioRepository.save(usuario);

            // Devolvemos el token en la respuesta para facilitar pruebas/desarrollo
            return ResponseEntity.ok(Map.of(
                "mensaje", "Token de recuperación generado exitosamente.",
                "token", token
            ));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No existe un usuario con ese correo electrónico."));
    }

    // 7. PASO 2: Confirmar restablecimiento con el Token
    @PostMapping("/confirmar-recuperacion")
    public ResponseEntity<?> confirmarRecuperacion(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nuevaPassword = request.get("nuevaPassword");

        if (token == null || nuevaPassword == null || token.trim().isEmpty() || nuevaPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El token y la nueva contraseña son obligatorios."));
        }

        // Requiere haber agregado 'findByResetToken' en UsuarioRepository
        Optional<Usuario> usuarioOpt = usuarioRepository.findByResetToken(token.trim());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El token ingresado no es válido."));
        }

        Usuario usuario = usuarioOpt.get();

        // Validar si el token ya expiró
        if (usuario.getTokenExpiration() == null || usuario.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El token de recuperación ha caducado. Solicita uno nuevo."));
        }

        // Actualizar contraseña e invalidar token
        usuario.setPassword(nuevaPassword);
        usuario.setResetToken(null);
        usuario.setTokenExpiration(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Contraseña restablecida exitosamente."));
    }
}