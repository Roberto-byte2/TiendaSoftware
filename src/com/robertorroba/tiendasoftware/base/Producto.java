package com.robertorroba.tiendasoftware.base;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Clase abstracta que repesenta un producto de la tienda
 * Contiene los atributos y metodos comunes a los distintos tipos de productos
 */
public abstract class  Producto {
    private String nombre;
    private String descripcion;
    private LinkedList<String> categorias;
    private LocalDate fechaPublicacion;
    private TipoProducto tipoProducto;

    /**
     * Constructor vacio que inializa una LinkedList vacía
     * solo lo utilizan las clases que heredan de esta.
     */
    public Producto(){
        this.categorias = new LinkedList<>();
        /**
         * Constructor con todos sus atributos.
         * solo lo utilizan las clases que heredan de esta.
         *   @param nombre nombre del producto.
         *   @param descripcion descripción del producto.
         *   @param categorias categorías a las que pertenece.
         *   @param fechaPublicacion fecha de publicación.
         *   @param tipoProducto tipo de producto.
         */
    }
    public Producto(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                    TipoProducto tipoProducto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.fechaPublicacion = fechaPublicacion;
        this.tipoProducto = tipoProducto;
    }

    /**
     * Devuelve el nombre de la aplicación
     * @return String con el nombre de la aplicación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *  Guarda el nombre de la aplicación.
     * @param nombre nuevo nombre de la aplicación.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la descripción del producto.
     *
     * @return descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Guarda la descripción del producto.
     * @param descripcion nueva descripción del producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve un LinkedList de String con las categorias del software.
     * @return LinkedList de String con las categorias.
     */
    public LinkedList<String> getCategorias() {
        return categorias;
    }

    /**
     * Guarda un LinkedList de Stream con las categorias del software.
     * @param categorias nueva LinkedList de Stream de categorias de software.
     */
    public void setCategorias(LinkedList<String> categorias) {
        this.categorias = categorias;
    }

    /**
     * Devuelve  la fecha de publicación.
     * @return LocalDate con la fecha de publicación
     */
    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Guarda la fecha de publicación.
     * @param fechaPublicacion con la fecha de publicación.
     */
    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Devuelve el tipo de producto.
     * @return tipo de producto.
     */
    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    /**
     * Guarda el tipo de producto.
     * @param tipoProducto con el tipo de producto.
     */
    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    /**
     *Devuelve una representación textual del producto con sus principales atributos..
     * @return información del producto en forma de texto.
     */
    @Override
    public String toString() {
        return "nombre: " + nombre  + ", tipo: " + tipoProducto + ", " + descripcion + ", categoria: " + categorias + ", fechaPublicacion: " +
                fechaPublicacion;
    }
}
