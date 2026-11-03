package com.robertorroba.tiendasoftware.gui;

import com.robertorroba.tiendasoftware.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.HashMap;
import com.robertorroba.tiendasoftware.base.TipoRequisito;
import com.robertorroba.tiendasoftware.base.SistemaOperativo;
import com.robertorroba.tiendasoftware.base.TipoProducto;
import com.robertorroba.tiendasoftware.base.Producto;
import com.robertorroba.tiendasoftware.base.TipoRequisito;
import com.robertorroba.tiendasoftware.base.Privativo;
import com.robertorroba.tiendasoftware.base.Libre;
import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class TiendaSoftwareControlador implements ActionListener, ListSelectionListener, WindowListener {

    private Ventana vista;
    private TiendaSoftwareModelo modelo;
    private File ultimaRutaExportada;

    public TiendaSoftwareControlador(Ventana vista, TiendaSoftwareModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        addActionListener(this);
        addWindowListener(this);
    }

    private void addActionListener(ActionListener listener) {
        vista.nuevobtn.addActionListener(listener);
        vista.importarBtn.addActionListener(listener);
        vista.exportarBtn.addActionListener(listener);
        vista.cerrarBtn.addActionListener(listener);

        vista.libreRadioButton.addActionListener(listener);
        vista.privativoRadioButton.addActionListener(listener);
        vista.softwareRadioButton.addActionListener(listener);
        vista.herramientaRadioButton.addActionListener(listener);
        vista.exportarSeleccionadoBtn.addActionListener(listener);
    }

    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }

    private boolean hayCamposVacios() {

        if (vista.nombreTxt.getText().trim().isEmpty() ||
                vista.descripcionTxt.getText().trim().isEmpty() ||
                vista.categoriasTxt.getText().trim().isEmpty() ||
                vista.fechaPublicacionTxt.getDate() == null) {

            return true;
        }

        return false;
    }

    private LinkedList<String> obtenerCategorias() {

        LinkedList<String> categorias = new LinkedList<>();

        String[] lista = vista.categoriasTxt.getText().split(",");

        for (String categoria : lista) {
            if (!categoria.trim().isEmpty()) {
                categorias.add(categoria.trim());
            }
        }

        return categorias;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();

        switch (actionCommand) {

            case "Nuevo":
                altaProducto();
                break;
            case "Importar":
                importarXML();
                break;

            case "Exportar":
                exportarXML();
                break;
            case "Exportar seleccionado":
                exportarSeleccionado();
                break;

            case "Cerrar":
                // Aquí programaremos el cierre.
                break;
        }
    }
    private void altaProducto() {

        if (hayCamposVacios()) {
            Util.mensajeError("Rellena todos los campos obligatorios");
            return;
        }

        String nombre = vista.nombreTxt.getText().trim();

        if (modelo.existeSoftware(nombre)) {
            Util.mensajeError("Ya existe un producto con ese nombre");
            return;
        }

        String descripcion = vista.descripcionTxt.getText().trim();
        LinkedList<String> categorias = obtenerCategorias();
        LocalDate fecha = vista.fechaPublicacionTxt.getDate();

        TipoProducto tipoProducto;

        if (vista.softwareRadioButton.isSelected()) {
            tipoProducto = TipoProducto.SOFTWARE;
        } else if (vista.herramientaRadioButton.isSelected()) {
            tipoProducto = TipoProducto.HERRAMIENTA;
        } else {
            Util.mensajeError("Selecciona Software o Herramienta");
            return;
        }

        if (vista.libreRadioButton.isSelected()) {
            altaLibre(nombre, descripcion, categorias, fecha, tipoProducto);

        } else if (vista.privativoRadioButton.isSelected()) {
            altaPrivativo(nombre, descripcion, categorias, fecha, tipoProducto);

        } else {
            Util.mensajeError("Selecciona Libre o Privativo");
        }
    }
    private void altaLibre(String nombre, String descripcion,
                           LinkedList<String> categorias, LocalDate fecha,
                           TipoProducto tipoProducto) {

        String licencia = vista.licenciaTxt.getText().trim();

        if (licencia.isEmpty()) {
            Util.mensajeError("Introduce la licencia");
            return;
        }

        TreeSet<SistemaOperativo> sistemas = new TreeSet<>();

        if (vista.windowsChk.isSelected()) {
            sistemas.add(SistemaOperativo.WINDOWS);
        }

        if (vista.linuxChk.isSelected()) {
            sistemas.add(SistemaOperativo.LINUX);
        }

        if (vista.macOsChk.isSelected()) {
            sistemas.add(SistemaOperativo.MACOS);
        }

        if (sistemas.isEmpty()) {
            Util.mensajeError("Selecciona al menos un sistema operativo");
            return;
        }

        modelo.altaLibre(nombre, descripcion, categorias, fecha,
                tipoProducto, licencia, sistemas);
        refrescar();
    }

    private void altaPrivativo(String nombre, String descripcion,
                               LinkedList<String> categorias, LocalDate fecha,
                               TipoProducto tipoProducto) {

        double precio;
        int unidades;

        try {
            precio = Double.parseDouble(vista.precioTxt.getText().trim());
            unidades = Integer.parseInt(vista.unidadesTxt.getText().trim());

            if (precio <= 0 || unidades <= 0) {
                Util.mensajeError("El precio y las unidades deben ser mayores que cero");
                return;
            }

        } catch (NumberFormatException e) {
            Util.mensajeError("Introduce un precio y unas unidades válidos");
            return;
        }

        HashMap<TipoRequisito, Integer> requisitos = new HashMap<>();

        try {
            if (vista.ramChck.isSelected()) {
                int ram = Integer.parseInt(vista.ramTxt.getText().trim());
                if (ram <= 0) {
                    Util.mensajeError("La RAM debe ser mayor que cero");
                    return;
                }
                requisitos.put(TipoRequisito.RAM, ram);
            }

            if (vista.espacioChk.isSelected()) {
                int espacio = Integer.parseInt(vista.espacioTxt.getText().trim());
                if (espacio <= 0) {
                    Util.mensajeError("El espacio debe ser mayor que cero");
                    return;
                }
                requisitos.put(TipoRequisito.ESPACIO, espacio);
            }

            if (vista.nucleosChk.isSelected()) {
                int nucleos = Integer.parseInt(vista.nucleosTxt.getText().trim());
                if (nucleos <= 0) {
                    Util.mensajeError("Los núcleos deben ser mayores que cero");
                    return;
                }
                requisitos.put(TipoRequisito.NUCLEOS, nucleos);
            }

        } catch (NumberFormatException e) {
            Util.mensajeError("Comprueba los valores de los requisitos");
            return;
        }

        modelo.altaPrivativo(nombre, descripcion, categorias, fecha,
                tipoProducto, precio, unidades, requisitos);

        refrescar();
    }

    private void refrescar() {

        vista.modeloTabla.setRowCount(0);

        for (Producto producto : modelo.obtenerProductos()) {

            vista.modeloTabla.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getTipoProducto(),
                    String.join(", ", producto.getCategorias()),
                    producto.getFechaPublicacion()
            });
        }
    }
    private void importarXML() {

        JFileChooser selector = Util.crearSelectorFichero(
                ultimaRutaExportada, "Archivos XML", "xml");

        int resultado = selector.showOpenDialog(vista.frame);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            File fichero = selector.getSelectedFile();

            try {
                modelo.importarXML(fichero);
                ultimaRutaExportada = fichero.getParentFile();
                refrescar();

            } catch (ParserConfigurationException | IOException | SAXException e) {
                Util.mensajeError("Error al importar el archivo XML");
                e.printStackTrace();
            }
        }
    }
    private void exportarXML() {

        JFileChooser selector = Util.crearSelectorFichero(
                ultimaRutaExportada, "Archivos XML", "xml");

        int resultado = selector.showSaveDialog(vista.frame);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            File fichero = selector.getSelectedFile();

            if (!fichero.getName().toLowerCase().endsWith(".xml")) {
                fichero = new File(fichero.getAbsolutePath() + ".xml");
            }

            modelo.exportarXML(fichero);
            ultimaRutaExportada = fichero.getParentFile();
        }

    }
    private void exportarSeleccionado() {

        int fila = vista.productosTabla.getSelectedRow();

        if (fila == -1) {
            Util.mensajeError("Selecciona un producto de la tabla");
            return;
        }

        Producto producto = modelo.obtenerProductos().get(fila);

        JFileChooser selector = Util.crearSelectorFichero(
                ultimaRutaExportada, "Archivos XML", "xml");

        int resultado = selector.showSaveDialog(vista.frame);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            File fichero = selector.getSelectedFile();

            if (!fichero.getName().toLowerCase().endsWith(".xml")) {
                fichero = new File(fichero.getAbsolutePath() + ".xml");
            }

            modelo.exportarProductoXML(fichero, producto);

            ultimaRutaExportada = fichero.getParentFile();
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {

    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }


    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }


}
