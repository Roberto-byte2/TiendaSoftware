package com.robertorroba.tiendasoftware.base;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Clase que repersenta un producto de software libre.
 * Hereda los atributos y métodos comunes de la clase Producto.
 */
public class Libre extends Producto {
    private String licencia;
    private TreeSet<SistemaOperativo> sistemasCompatibles;

    /**
     * Constructor vacio que inicia un TreeSet de sistemas operativos compatibles.
     */
    public Libre() {
       this.sistemasCompatibles = new TreeSet<>();
    }

    /**
     * Constructor que inicializa un producto libre con todos sus atributos,
     * tanto los heredados de Producto como los propios de esta clase.
     * @param nombre nombre del producto.
     * @param descripcion descripción del producto.
     * @param categorias categorías del producto.
     * @param fechaPublicacion fecha de publicación.
     * @param tipoProducto  tipo de producto.
     * @param licencia tipo de licencia del software.
     * @param sistemasCompatibles sistemas operativos compatibles.
     */
    public Libre(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                 TipoProducto tipoProducto, String licencia, TreeSet<SistemaOperativo> sistemasCompatibles) {
        super(nombre, descripcion, categorias, fechaPublicacion, tipoProducto);
        this.licencia = licencia;
        this.sistemasCompatibles = new TreeSet<>(sistemasCompatibles);
    }

    /**
     * Devuelve el tipo de licencia del software.
     * @return licencia del software.
     */
    public String getLicencia() {
        return licencia;
    }

    /**
     *  Guarda el tipo de licencia del software.
     * @param licencia tipo de licencia del software.
     */
    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    /**
     * Devuelve los sistemas operativos compatibles con el software.
     * @return TreeSet con los sistemas operativos compatibles.
     */
    public TreeSet<SistemaOperativo> getSistemasCompatibles() {
        return sistemasCompatibles;
    }

    /**
     * Guarda el conjunto de sistemas operativos compatibles con el software.
     * @param sistemasCompatibles guarda los sistemas compatibles en un TreeSet.
     */
    public void setSistemasCompatibles(TreeSet<SistemaOperativo> sistemasCompatibles) {
        this.sistemasCompatibles = new TreeSet<>(sistemasCompatibles);
    }
    /**
     *Devuelve una representación textual del producto libre con sus principales atributos..
     * @return información del producto en forma de texto.
     */
    @Override
    public String toString() {
        return super.toString() +
                ", licencia: " + licencia + ", sistemas compatibles: " + sistemasCompatibles;
    }
}
