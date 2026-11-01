package com.robertorroba.tiendasoftware.base;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;

public class Privativo extends Producto {

    private double precio;
    private int unidades;
    private HashMap<String,Integer> requisitos;

    public Privativo() {
        this.requisitos = new HashMap<>();
    }

    public Privativo(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                     TipoProducto tipoProducto, double precio,int unidades,HashMap<String,Integer> requisitos) {
        super(nombre, descripcion, categorias, fechaPublicacion, tipoProducto);
        this.precio = precio;
        this.unidades = unidades;
        this.requisitos = new HashMap<>(requisitos);
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public HashMap<String, Integer> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(HashMap<String, Integer> requisitos) {
        this.requisitos = new HashMap<>(requisitos);
    }

    @Override
    public String toString() {
        return super.toString()  + ", precio: " + precio + ", unidades:" + unidades + ", requisitos: " + requisitos;
    }
}
