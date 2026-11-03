package com.robertorroba.tiendasoftware.gui;

import com.robertorroba.tiendasoftware.base.*;
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

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Controlador de la aplicación "Tienda de Software".
 * Actúa como intermediario entre la vista ({@link Ventana}) y el modelo ({@link TiendaSoftwareModelo}),
 * gestionando los eventos de usuario, las validaciones de formularios, las operaciones de alta,
 * edición, exportación/importación de ficheros XML y el ciclo de vida de la ventana principal.
 *
 * Implementa las interfaces {@link ActionListener}, {@link ListSelectionListener} y {@link WindowListener}.
 *
 * @author Roberto Torroba
 * @version 1.0
 */
public class TiendaSoftwareControlador implements ActionListener, ListSelectionListener, WindowListener {
    /** Referencia a la interfaz gráfica principal de la aplicación. */
    private Ventana vista;
    /** Referencia al modelo de datos que gestiona la colección de productos y persistencia. */
    private TiendaSoftwareModelo modelo;
    /** Última ruta de directorio utilizada al exportar o importar ficheros. */
    private File ultimaRutaExportada;
    /** Producto seleccionado actualmente para su modificación ({@code null} si se trata de una nueva alta). */
    private Producto productoEnEdicion = null;
    /**
     * Constructor del controlador. Inicializa la vista y el modelo, y registra
     * los escuchadores de eventos y de ventana correspondientes.
     *
     * @param vista Instancia de la ventana principal.
     * @param modelo Instancia del modelo de datos de la tienda.
     */
    public TiendaSoftwareControlador(Ventana vista, TiendaSoftwareModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        addActionListener(this);
        addWindowListener(this);
    }
    /**
     * Registra los escuchadores de acciones ({@link ActionListener}) para los diferentes
     * botones, radiobotones y componentes interactivos de la vista.
     *
     * @param listener Instancia del escuchador de acciones.
     */
    private void addActionListener(ActionListener listener) {
        vista.nuevobtn.addActionListener(listener);
        vista.importarBtn.addActionListener(listener);
        vista.exportarBtn.addActionListener(listener);
        vista.cerrarBtn.addActionListener(listener);

        vista.libreRadioButton.addActionListener(this);
        vista.privativoRadioButton.addActionListener(this);
        vista.softwareRadioButton.addActionListener(listener);
        vista.herramientaRadioButton.addActionListener(listener);
        vista.exportarSeleccionadoBtn.addActionListener(listener);
        vista.editarBtn.addActionListener(this);
    }
    /**
     * Registra el escuchador de eventos de ventana ({@link WindowListener}) para el frame principal.
     *
     * @param listener Instancia del escuchador de ventana.
     */
    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }
    /**
     * Comprueba si alguno de los campos obligatorios comunes del formulario se encuentra vacío.
     *
     * @return {@code true} si hay campos obligatorios vacíos; {@code false} en caso contrario.
     */
    private boolean hayCamposVacios() {

        if (vista.nombreTxt.getText().trim().isEmpty() ||
                vista.descripcionTxt.getText().trim().isEmpty() ||
                vista.categoriasTxt.getText().trim().isEmpty() ||
                vista.fechaPublicacionTxt.getDate() == null) {

            return true;
        }

        return false;
    }
    /**
     * Procesa el campo de texto de categorías separadas por comas y las extrae
     * en una lista enlazada.
     *
     * @return Una {@link LinkedList} con las cadenas de texto correspondientes a cada categoría.
     */
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

    /**
     * Gestiona la recepción de eventos de acción disparados por los componentes de la interfaz,
     * redirigiendo el flujo hacia los métodos correspondientes (altas, ediciones, importaciones,
     * exportaciones y cierre).
     *
     * @param actionEvent Evento de acción capturado.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == vista.libreRadioButton) {
            vista.mostrarPanel("LIBRE");
        }

        if (actionEvent.getSource() == vista.privativoRadioButton) {

            vista.mostrarPanel("PRIVATIVO");
        }

        vista.frame.revalidate();
        vista.frame.repaint();
        String actionCommand = actionEvent.getActionCommand();

        switch (actionCommand) {

            case "Guardar":
                if (productoEnEdicion == null) {
                    altaProducto();
                } else {
                    editarProducto();
                }
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
            case "Ver / Editar":
                cargarProductoSeleccionado();
                break;

            case "Cerrar":
                cerrarAplicacion();
                break;
        }
    }
    /**
     * Valida los datos comunes y específicos del formulario y procede a dar de alta
     * un nuevo producto (libre o privativo) en el modelo tras comprobar que no existan duplicados.
     */
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
    /**
     * Valida los campos específicos de un producto libre (licencia y sistemas operativos)
     * y solicita al modelo su inserción en la colección.
     *
     * @param nombre Nombre único del software.
     * @param descripcion Descripción detallada.
     * @param categorias Lista de categorías asociadas.
     * @param fecha Fecha de publicación.
     * @param tipoProducto Tipo de producto (Software o Herramienta).
     */
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
        borrarFormulario();
    }
    /**
     * Valida los campos numéricos específicos de un producto privativo (precio, unidades y requisitos técnicos)
     * y solicita al modelo su inserción en la colección.
     *
     * @param nombre Nombre único del software.
     * @param descripcion Descripción detallada.
     * @param categorias Lista de categorías asociadas.
     * @param fecha Fecha de publicación.
     * @param tipoProducto Tipo de producto (Software o Herramienta).
     */
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
        borrarFormulario();
    }
    /**
     * Actualiza los datos de un producto que se encontraba previamente en edición,
     * validando restricciones y reemplazándolo en el modelo.
     */
    private void editarProducto() {

        if (hayCamposVacios()) {
            Util.mensajeError("Rellena todos los campos obligatorios");
            return;
        }

        String nombre = vista.nombreTxt.getText().trim();

        // Comprobar duplicados, excluyendo el producto actual
        for (Producto p : modelo.obtenerProductos()) {
            if (p != productoEnEdicion &&
                    p.getNombre().equalsIgnoreCase(nombre)) {
                Util.mensajeError("Ya existe un producto con ese nombre");
                return;
            }
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

        Producto nuevoProducto;

        if (productoEnEdicion instanceof Libre) {

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

            nuevoProducto = new Libre(
                    nombre, descripcion, categorias, fecha,
                    tipoProducto, licencia, sistemas
            );

        } else if (productoEnEdicion instanceof Privativo) {

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

            nuevoProducto = new Privativo(
                    nombre, descripcion, categorias, fecha,
                    tipoProducto, precio, unidades, requisitos
            );

        } else {
            Util.mensajeError("Tipo de producto desconocido");
            return;
        }

        // Sustituir el producto antiguo por el nuevo
        if (modelo.actualizarProducto(productoEnEdicion, nuevoProducto)) {

            Util.mensajeInformacion(
                    "Producto actualizado correctamente",
                    "Edición"
            );

            productoEnEdicion = null;
            refrescar();
            borrarFormulario();

        } else {
            Util.mensajeError("No se ha podido actualizar el producto");
        }
    }
    /**
     * Limpia y recarga el contenido de la tabla de la interfaz gráfica con los datos
     * actuales procedentes de la lista de productos del modelo.
     */
    private void refrescar() {

        vista.modeloTabla.setRowCount(0);

        for (Producto producto : modelo.obtenerProductos()) {

            String licencia;

            if (producto instanceof Libre) {
                licencia = "Libre";
            } else if (producto instanceof Privativo) {
                licencia = "Privativo";
            } else {
                licencia = "Desconocido";
            }

            vista.modeloTabla.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getTipoProducto(),
                    licencia,
                    String.join(", ", producto.getCategorias()),
                    producto.getFechaPublicacion()
            });
        }
    }
    /**
     * Abre un selector de archivos para importar un catálogo de productos
     * desde un fichero XML externo hacia el modelo de la aplicación.
     */
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
    /** Abre un selector de archivos para exportar todos los productos actuales
     * de la tienda a un fichero XML utilizando la estructura DOM.
     */
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
    /**
     * Exporta de manera individual el producto seleccionado en la tabla de la interfaz
     * hacia un archivo XML.
     */
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

            boolean exportado = modelo.exportarProductoXML(fichero, producto);


            if (exportado) {
                Util.mensajeInformacion("Producto exportado correctamente","Exportación");
            } else {
                Util.mensajeError("El producto ya existe en el archivo XML");
            }

            ultimaRutaExportada = fichero.getParentFile();
        }
    }
    /**
     * Resetea y vacía todos los campos de texto, casillas de verificación, selectores
     * y botones de opción del formulario de la vista.
     */
    private void borrarFormulario() {

        // Campos comunes
        vista.nombreTxt.setText("");
        vista.descripcionTxt.setText("");
        vista.categoriasTxt.setText("");
        vista.fechaPublicacionTxt.setDate(null);


        // Tipo de producto
        vista.softwareRadioButton.setSelected(false);
        vista.herramientaRadioButton.setSelected(false);

        // Tipo de licencia
        vista.libreRadioButton.setSelected(false);
        vista.privativoRadioButton.setSelected(false);

        // Producto libre
        vista.licenciaTxt.setText("");
        vista.windowsChk.setSelected(false);
        vista.linuxChk.setSelected(false);
        vista.macOsChk.setSelected(false);

        // Producto privativo
        vista.precioTxt.setText("");
        vista.unidadesTxt.setText("");

        vista.ramChck.setSelected(false);
        vista.espacioChk.setSelected(false);
        vista.nucleosChk.setSelected(false);

        vista.ramTxt.setText("");
        vista.nucleosTxt.setText("");
        vista.espacioTxt.setText("");

        vista.panelTipoEspecifico.setVisible(false);
        vista.libreRadioButton.setEnabled(true);
        vista.privativoRadioButton.setEnabled(true);



    }
    /**
     * Carga los datos del producto seleccionado en la tabla de la interfaz sobre
     * el formulario para permitir su visualización o posterior edición.
     */
    private void cargarProductoSeleccionado() {

        int fila = vista.productosTabla.getSelectedRow();

        if (fila == -1) {
            Util.mensajeError("Selecciona un producto de la tabla");
            return;
        }
        vista.libreRadioButton.setEnabled(false);
        vista.privativoRadioButton.setEnabled(false);

        productoEnEdicion = modelo.obtenerProductos().get(fila);

        // Datos comunes
        vista.nombreTxt.setText(productoEnEdicion.getNombre());
        vista.descripcionTxt.setText(productoEnEdicion.getDescripcion());
        vista.categoriasTxt.setText(
                String.join(", ", productoEnEdicion.getCategorias())
        );
        vista.fechaPublicacionTxt.setDate(
                productoEnEdicion.getFechaPublicacion()
        );

        // Tipo de producto
        if (productoEnEdicion.getTipoProducto() == TipoProducto.SOFTWARE) {
            vista.softwareRadioButton.setSelected(true);
        } else {
            vista.herramientaRadioButton.setSelected(true);
        }

        // Tipo específico
        if (productoEnEdicion instanceof Libre) {

            Libre libre = (Libre) productoEnEdicion;

            vista.libreRadioButton.setSelected(true);
            vista.mostrarPanel("LIBRE");

            vista.licenciaTxt.setText(libre.getLicencia());

            vista.windowsChk.setSelected(
                    libre.getSistemasCompatibles().contains(SistemaOperativo.WINDOWS)
            );
            vista.linuxChk.setSelected(
                    libre.getSistemasCompatibles().contains(SistemaOperativo.LINUX)
            );
            vista.macOsChk.setSelected(
                    libre.getSistemasCompatibles().contains(SistemaOperativo.MACOS)
            );

        } else if (productoEnEdicion instanceof Privativo) {

            Privativo privativo = (Privativo) productoEnEdicion;

            vista.privativoRadioButton.setSelected(true);
            vista.mostrarPanel("PRIVATIVO");

            vista.precioTxt.setText(String.valueOf(privativo.getPrecio()));
            vista.unidadesTxt.setText(String.valueOf(privativo.getUnidades()));

            HashMap<TipoRequisito, Integer> requisitos = privativo.getRequisitos();

            vista.ramChck.setSelected(requisitos.containsKey(TipoRequisito.RAM));
            vista.espacioChk.setSelected(requisitos.containsKey(TipoRequisito.ESPACIO));
            vista.nucleosChk.setSelected(requisitos.containsKey(TipoRequisito.NUCLEOS));

            vista.ramTxt.setText(
                    requisitos.containsKey(TipoRequisito.RAM)
                            ? String.valueOf(requisitos.get(TipoRequisito.RAM)) : ""
            );

            vista.espacioTxt.setText(
                    requisitos.containsKey(TipoRequisito.ESPACIO)
                            ? String.valueOf(requisitos.get(TipoRequisito.ESPACIO)) : ""
            );

            vista.nucleosTxt.setText(
                    requisitos.containsKey(TipoRequisito.NUCLEOS)
                            ? String.valueOf(requisitos.get(TipoRequisito.NUCLEOS)) : ""
            );
        }
    }
    /**
     * Muestra un cuadro de diálogo de confirmación para salir de la aplicación
     * y, en caso afirmativo, finaliza la ejecución del programa.
     */
    private void cerrarAplicacion() {

        int respuesta = Util.mensajeConfirmacion(
                "¿Estás seguro de que quieres salir?",
                "Cerrar aplicación"
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
    cerrarAplicacion();
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
