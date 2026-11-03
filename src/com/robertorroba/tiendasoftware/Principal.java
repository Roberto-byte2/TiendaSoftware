package com.robertorroba.tiendasoftware;

import com.robertorroba.tiendasoftware.gui.TiendaSoftwareControlador;
import com.robertorroba.tiendasoftware.gui.TiendaSoftwareModelo;
import com.robertorroba.tiendasoftware.gui.Ventana;

/**
 * Clase principal de la aplicación Tienda de Software.
 * Se encarga de iniciar la aplicacion
 */
public class Principal {
    /**
     * Metodo principal inicia la aplicación.
     * @param args argumentos recibidos de la línea de comandos.
     */
    public static void main(String[] args) {
        Ventana vista = new Ventana();
        TiendaSoftwareModelo modelo = new TiendaSoftwareModelo();
        TiendaSoftwareControlador controlador =
                new TiendaSoftwareControlador(vista, modelo);
    }
}