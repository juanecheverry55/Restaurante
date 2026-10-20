package com.sena.adso.Prueba.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreInsumo;

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private String unidadMedida; // Ej: Kg, Litros, Unidades, Gramos

    @Column(nullable = false)
    private Double stockMinimo; // Para alertar cuando esté por agotarse

    public Inventario() {}

    public Inventario(String nombreInsumo, Double cantidad, String unidadMedida, Double stockMinimo) {
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.stockMinimo = stockMinimo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public Double getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Double stockMinimo) { this.stockMinimo = stockMinimo; }
}