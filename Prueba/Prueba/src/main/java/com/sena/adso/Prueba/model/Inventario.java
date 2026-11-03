package com.sena.adso.Prueba.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_producto", nullable = false)
    private String codigoProducto;

    @Column(name = "numero_lote", nullable = false)
    private String numeroLote;

    @Column(nullable = false)
    private String nombreInsumo;

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private String unidadMedida;

    @Column(nullable = false)
    private Double stockMinimo;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(name = "fecha_expiracion", nullable = false)
    private String fechaExpiracion; // Cambiado a String para evitar errores de parseo con LocalDateTime

    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    public Inventario() {}

    @PreUpdate
    @PrePersist
    public void calcularTotalesYFecha() {
        this.fechaActualizacion = LocalDateTime.now();
        if (this.cantidad != null && this.precioUnitario != null) {
            this.valorTotal = this.cantidad * this.precioUnitario;
        } else {
            this.valorTotal = 0.0;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { 
        this.cantidad = cantidad;
        if (this.precioUnitario != null) this.valorTotal = this.cantidad * this.precioUnitario;
    }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public Double getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Double stockMinimo) { this.stockMinimo = stockMinimo; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { 
        this.precioUnitario = precioUnitario;
        if (this.cantidad != null) this.valorTotal = this.cantidad * this.precioUnitario;
    }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}