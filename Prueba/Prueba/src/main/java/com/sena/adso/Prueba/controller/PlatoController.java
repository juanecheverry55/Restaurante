package com.sena.adso.Prueba.controller;

import com.sena.adso.Prueba.model.Plato;
import com.sena.adso.Prueba.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
@CrossOrigin(origins = "*") // Permite peticiones desde el frontend
public class PlatoController {

    @Autowired
    private PlatoRepository platoRepository;

    // 1. OBTENER TODOS LOS PLATOS (GET)
    @GetMapping
    public List<Plato> obtenerPlatos() {
        return platoRepository.findAll();
    }

    // 2. OBTENER UN PLATO POR SU ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Plato> obtenerPlatoPorId(@PathVariable Long id) {
        return platoRepository.findById(id)
                .map(plato -> ResponseEntity.ok().body(plato))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREAR UN NUEVO PLATO (POST)
    @PostMapping
    public Plato guardarPlato(@RequestBody Plato plato) {
        return platoRepository.save(plato);
    }

    // 4. ACTUALIZAR UN PLATO EXISTENTE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Plato> actualizarPlato(@PathVariable Long id, @RequestBody Plato detallesPlato) {
        return platoRepository.findById(id)
                .map(plato -> {
                    plato.setNombre(detallesPlato.getNombre());
                    plato.setPrecio(detallesPlato.getPrecio());
                    plato.setImagen(detallesPlato.getImagen());           // <-- AQUÍ FALTABA
                    plato.setDescripcion(detallesPlato.getDescripcion()); // <-- AQUÍ FALTABA
                    
                    Plato platoActualizado = platoRepository.save(plato);
                    return ResponseEntity.ok().body(platoActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR UN PLATO (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long id) {
        return platoRepository.findById(id)
                .map(plato -> {
                    platoRepository.delete(plato);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}