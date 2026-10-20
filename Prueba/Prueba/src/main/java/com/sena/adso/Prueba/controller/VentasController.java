package com.sena.adso.Prueba.controller;

import com.sena.adso.Prueba.model.Pedido;
import com.sena.adso.Prueba.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentasController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumenVentas() {
        List<Pedido> todosLosPedidos = pedidoRepository.findAll();

        // Filtrar únicamente los pedidos completados/entregados
        List<Pedido> ventasCompletadas = todosLosPedidos.stream()
                .filter(p -> "ENTREGADO".equalsIgnoreCase(p.getEstado()))
                .collect(Collectors.toList());

        double totalIngresos = ventasCompletadas.stream()
                .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                .sum();

        long totalPedidosCompletados = ventasCompletadas.size();
        double ticketMedio = totalPedidosCompletados > 0 ? (totalIngresos / totalPedidosCompletados) : 0.0;

        long totalPendientes = todosLosPedidos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()) || "EN_PREPARACION".equalsIgnoreCase(p.getEstado()))
                .count();

        long totalCancelados = todosLosPedidos.stream()
                .filter(p -> "CANCELADO".equalsIgnoreCase(p.getEstado()))
                .count();

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalIngresos", totalIngresos);
        resumen.put("totalPedidosCompletados", totalPedidosCompletados);
        resumen.put("ticketMedio", ticketMedio);
        resumen.put("totalPendientes", totalPendientes);
        resumen.put("totalCancelados", totalCancelados);
        resumen.put("historialVentas", ventasCompletadas);

        return resumen;
    }
}