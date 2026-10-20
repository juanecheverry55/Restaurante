package com.sena.adso.Prueba.controller;

import com.sena.adso.Prueba.model.Pedido;
import com.sena.adso.Prueba.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("PENDIENTE");
        }
        if (pedido.getMedioPago() == null || pedido.getMedioPago().isEmpty()) {
            pedido.setMedioPago("Efectivo");
        }
        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return pedidoRepository.findById(id).map(pedido -> {
            // Actualizar estado si viene en el JSON
            if (body.containsKey("estado")) {
                String nuevoEstado = body.get("estado");
                if (nuevoEstado != null && !nuevoEstado.isEmpty()) {
                    pedido.setEstado(nuevoEstado);
                }
            }

            // Actualizar número de mesa si viene en el JSON
            if (body.containsKey("numeroMesa")) {
                String nuevaMesa = body.get("numeroMesa");
                pedido.setNumeroMesa(nuevaMesa);
            }

            // Actualizar medio de pago si viene en el JSON
            if (body.containsKey("medioPago")) {
                String nuevoMedioPago = body.get("medioPago");
                pedido.setMedioPago(nuevoMedioPago);
            }

            Pedido pedidoActualizado = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoActualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}