package com.sena.adso.Prueba.controller;

import com.sena.adso.Prueba.model.Inventario;
import com.sena.adso.Prueba.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @GetMapping
    public List<Inventario> obtenerTodo() {
        return inventarioRepository.findAll();
    }

    @PostMapping
    public Inventario guardarInsumo(@RequestBody Inventario insumo) {
        return inventarioRepository.save(insumo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInsumo(@PathVariable Long id, @RequestBody Inventario detalles) {
        return inventarioRepository.findById(id).map(insumo -> {
            insumo.setNombreInsumo(detalles.getNombreInsumo());
            insumo.setCantidad(detalles.getCantidad());
            insumo.setUnidadMedida(detalles.getUnidadMedida());
            insumo.setStockMinimo(detalles.getStockMinimo());
            return ResponseEntity.ok(inventarioRepository.save(insumo));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Long id) {
        if (inventarioRepository.existsById(id)) {
            inventarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}