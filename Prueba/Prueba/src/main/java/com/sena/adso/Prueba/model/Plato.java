package com.sena.adso.Prueba.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "platos")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;

    @Column(name = "imagen_url", length = 1000)
    private String imagen; // Se define como 'imagen' con getters/setters 'getImagen()' y 'setImagen()'

    // Constructor vacío necesario para JPA
    public Plato() {}

    public Plato(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    // Getter y Setter adaptados para el controlador (getImagen y setImagen)
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    // Compatibilidad en caso de que alguna parte antigua de tu código use getImagenUrl
    public String getImagenUrl() { return imagen; }
    public void setImagenUrl(String imagen) { this.imagen = imagen; }
}