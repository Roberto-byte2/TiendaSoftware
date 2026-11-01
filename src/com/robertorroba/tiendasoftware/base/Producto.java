package com.robertorroba.tiendasoftware.base;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeSet;

public abstract class  Producto {
    private String nombre;
    private String descripcion;
    private LinkedList<String> categorias;
    private LocalDate fechaPublicacion;
    private TipoProducto tipoProducto;


    public Producto(){

    }
    public Producto(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                    TipoProducto tipoProducto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.fechaPublicacion = fechaPublicacion;
        this.tipoProducto = tipoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LinkedList<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(LinkedList<String> categorias) {
        this.categorias = categorias;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    @Override
    public String toString() {
        return "nombre: " + nombre  + ", tipo: " + tipoProducto + ", " + descripcion + ", categoria: " + categorias + ", fechaPublicacion: " +
                fechaPublicacion;
    }
}
