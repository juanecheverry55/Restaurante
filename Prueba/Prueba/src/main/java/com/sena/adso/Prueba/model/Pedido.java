package com.sena.adso.Prueba.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteEmail;
    
    @Column(length = 1000)
    private String detallePlatos; // Ej: "Bandeja Paisa x1, Cazuela de mariscos x1"
    
    private Double total;
    private String estado = "PENDIENTE"; // PENDIENTE, EN_PREPARACION, ENTREGADO
    private LocalDateTime fecha = LocalDateTime.now();

    public Pedido() {
    }

    public Pedido(String clienteEmail, String detallePlatos, Double total) {
        this.clienteEmail = clienteEmail;
        this.detallePlatos = detallePlatos;
        this.total = total;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getDetallePlatos() { return detallePlatos; }
    public void setDetallePlatos(String detallePlatos) { this.detallePlatos = detallePlatos; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

