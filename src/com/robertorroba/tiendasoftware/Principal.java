package com.robertorroba.tiendasoftware;

import com.robertorroba.tiendasoftware.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana();
            ventana.setVisible(true); // ¡Importante para que se muestre!
        });

    }
}
