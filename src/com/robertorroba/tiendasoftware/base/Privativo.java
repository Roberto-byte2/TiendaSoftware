package com.robertorroba.tiendasoftware.base;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
/**
 * Clase que repersenta un producto de software privativo.
 * Hereda los atributos y métodos comunes de la clase Producto.
 */
public class Privativo extends Producto {

    private double precio;
    private int unidades;
    private HashMap<TipoRequisito,Integer> requisitos;

    /**
     * Constructor vacio que inicia un HashMap vacio para insertar las caracteristicas minimas
     * para poder usar el software.
     */
    public Privativo() {
        this.requisitos = new HashMap<>();
    }

    /**
     * Constructor que inicializa un producto privativo con todos sus atributos,
     * tanto los heredados de Producto como los propios de esta clase.
     * @param nombre
     * @param descripcion
     * @param categorias
     * @param fechaPublicacion
     * @param tipoProducto
     * @param precio
     * @param unidades
     * @param requisitos
     */
    public Privativo(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                     TipoProducto tipoProducto, double precio,int unidades,HashMap<TipoRequisito,Integer> requisitos) {
        super(nombre, descripcion, categorias, fechaPublicacion, tipoProducto);
        this.precio = precio;
        this.unidades = unidades;
        this.requisitos = new HashMap<>(requisitos);
    }

    /**
     * Devuelve el precio por unidad del software.
     * @return precio en double.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Guarda el precio por unidad del software.
     * @param precio en double.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Devuelve el numero de unidades disponibles.
     * @return devuelve un entero con las unidades disponibles.
     */
    public int getUnidades() {
        return unidades;
    }

    /**
     * Guarda el numero de unidades disponibles.
     * @param unidades disponibles.
     */
    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    /**
     * Devuelve los requisitos necesarios para poder usar el software.
     * @return HashMap con los requisitos necesarios para poder usal el software.
     */
    public HashMap<TipoRequisito, Integer> getRequisitos() {
        return requisitos;
    }

    /**
     * Guarda los requisitos necesarios para poder usar el software.
     * @param requisitos HashMap de los requisitos necesarios.
     */
    public void setRequisitos(HashMap<TipoRequisito, Integer> requisitos) {
        this.requisitos = new HashMap<>(requisitos);
    }
    /**
     *Devuelve una representación textual del producto privativo con sus principales atributos..
     * @return información del producto en forma de texto.
     */
    @Override
    public String toString() {
        return super.toString()  + ", precio: " + precio + ", unidades:" + unidades + ", requisitos: " + requisitos;
    }
}
