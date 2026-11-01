package com.robertorroba.tiendasoftware.base;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeSet;

public class Libre extends Producto {
    private String licencia;
    private TreeSet<SistemaOperativo> sistemasCompatibles;

    public Libre() {
       this.sistemasCompatibles = new TreeSet<>();
    }

    public Libre(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                 TipoProducto tipoProducto, String licencia, TreeSet<SistemaOperativo> sistemasCompatibles) {
        super(nombre, descripcion, categorias, fechaPublicacion, tipoProducto);
        this.licencia = licencia;
        this.sistemasCompatibles = new TreeSet<>(sistemasCompatibles);
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public TreeSet<SistemaOperativo> getSistemasCompatibles() {
        return sistemasCompatibles;
    }

    public void setSistemasCompatibles(TreeSet<SistemaOperativo> sistemasCompatibles) {
        this.sistemasCompatibles = new TreeSet<>(sistemasCompatibles);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", licencia: " + licencia + ", sistemas compatibles: " + sistemasCompatibles;
    }
}
