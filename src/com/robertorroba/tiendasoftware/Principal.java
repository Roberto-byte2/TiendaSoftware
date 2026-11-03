package com.robertorroba.tiendasoftware;

import com.robertorroba.tiendasoftware.gui.TiendaSoftwareControlador;
import com.robertorroba.tiendasoftware.gui.TiendaSoftwareModelo;
import com.robertorroba.tiendasoftware.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        Ventana vista = new Ventana();
        TiendaSoftwareModelo modelo = new TiendaSoftwareModelo();
        TiendaSoftwareControlador controlador =
                new TiendaSoftwareControlador(vista, modelo);
    }
}