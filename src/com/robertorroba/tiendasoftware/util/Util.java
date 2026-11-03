package com.robertorroba.tiendasoftware.util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
/**
 * Clase de utilidad que proporciona métodos estáticos
 * para mostrar mensajes y seleccionar archivos.
 */
public class Util {
    /**
     * Muestra un mensaje de error mediante un cuadro de diálogo.
     *
     * @param mensaje texto del error que se desea mostrar.
     */
    public static void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null,
                mensaje,"Error",JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Muestra un cuadro de diálogo para solicitar confirmación al usuario.
     *
     * @param mensaje texto de la pregunta.
     * @param titulo título del cuadro de diálogo.
     * @return respuesta seleccionada por el usuario.
     */
    public static int mensajeConfirmacion(String mensaje,String titulo) {
        return JOptionPane.showConfirmDialog(null,mensaje,
                titulo,JOptionPane.YES_NO_OPTION);
    }
    /**
     * Muestra un mensaje informativo mediante un cuadro de diálogo.
     *
     * @param mensaje información que se desea mostrar.
     * @param titulo título del cuadro de diálogo.
     */
    public static void mensajeInformacion(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    /**
     * Crea un selector de archivos con una ruta inicial y un filtro opcional.
     *
     * @param rutaDefecto directorio inicial del selector.
     * @param tipoArchivos descripción del tipo de archivo permitido.
     * @param extension extensión de los archivos permitidos.
     * @return selector de archivos configurado.
     */
    public static JFileChooser crearSelectorFichero(File rutaDefecto,
                                                    String tipoArchivos,
                                                    String extension) {
        JFileChooser selectorFichero=new JFileChooser();
        if (rutaDefecto!=null) {
            selectorFichero.setCurrentDirectory(rutaDefecto);
        }
        if (extension!=null) {
            FileNameExtensionFilter filtro = new
                    FileNameExtensionFilter(tipoArchivos,extension);
            selectorFichero.setFileFilter(filtro);
        }
        return selectorFichero;
    }
    /**
     * Solicita confirmación al usuario antes de cerrar la aplicación.
     * Si el usuario acepta, finaliza la ejecución del programa.
     */
    private void cerrarAplicacion() {

        int respuesta = Util.mensajeConfirmacion(
                "¿Estás seguro de que quieres salir?", "Cerrar aplicación");

        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

