package com.robertorroba.tiendasoftware.gui;

import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
/**
 * Clase que representa la ventana principal de la aplicación.
 * Contiene los componentes gráficos y configura la interfaz
 * de usuario de Tienda de Software.
 */
public class Ventana {
    private JPanel panel1;
    public JLabel tipoLbl;
    public JRadioButton libreRadioButton;
    public  JRadioButton privativoRadioButton;
    public JLabel tipoProductoLbl;
    public JRadioButton softwareRadioButton;
    public JRadioButton herramientaRadioButton;
    public JLabel nombreLbl;
    public JTextField nombreTxt;
    public JLabel descripcionLbl;
    public JTextArea descripcionTxt;
    public JLabel categoriasLbl;
    public JTextField categoriasTxt;
    public JLabel fechaPublicacionLbl;
    public DatePicker fechaPublicacionTxt;
    public JPanel panelTipoEspecifico;
    public JLabel licenciaLbl;
    public JTextField licenciaTxt;
    public JCheckBox windowsChk;
    public JCheckBox linuxChk;
    public JCheckBox macOsChk;
    public JLabel precioLbl;
    public JTextField precioTxt;
    public JLabel udsLbl;
    public JTextField unidadesTxt;
    public JCheckBox ramChck;
    public JCheckBox espacioChk;
    public JCheckBox nucleosChk;
    public JTextField ramTxt;
    public JTextField espacioTxt;
    public JTextField nucleosTxt;
    public JPanel panelPrivativo;
    private JPanel panelInferior;
    private JScrollPane productosScroll;
    public JTable productosTabla;
    public JButton nuevobtn;
    public JButton importarBtn;
    public JButton exportarBtn;
    public JButton cerrarBtn;
    public JButton exportarSeleccionadoBtn;
    public JPanel panelLibre;
    public JButton editarBtn;
    private CardLayout cardLayout;
    // añadidos por mi
    public JFrame frame;
    public DefaultTableModel modeloTabla;
    /**
     * Constructor que inicializa y configura la ventana principal,
     * sus componentes gráficos y su apariencia.
     */
    public Ventana() {
        frame = new JFrame("Tienda de Software");
        ImageIcon icono = new ImageIcon(
                getClass().getResource("/img/logo.png")
        );

        frame.setIconImage(icono.getImage());
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 400));

        initComponents();


        // Registramos las tarjetas
        panelTipoEspecifico.removeAll();
        cardLayout = (CardLayout) panelTipoEspecifico.getLayout();

        panelTipoEspecifico.add(panelLibre, "LIBRE");
        panelTipoEspecifico.add(panelPrivativo, "PRIVATIVO");
        panelTipoEspecifico.setVisible(false);

        personalizarBoton(nuevobtn);
        personalizarBoton(editarBtn);
        personalizarBoton(importarBtn);
        personalizarBoton(exportarBtn);
        personalizarBoton(exportarSeleccionadoBtn);
        personalizarBoton(cerrarBtn);
        cerrarBtn.setBackground(new Color(169, 68, 66));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    /**
     * Crea y configura los componentes personalizados de la interfaz,
     * incluyendo el selector de fechas.
     */
    private void createUIComponents() {
        fechaPublicacionTxt =new DatePicker();
        fechaPublicacionTxt.getComponentToggleCalendarButton()
                .setIcon(new ImageIcon(getClass().getResource("/img/calendario.png")));

        fechaPublicacionTxt.getComponentToggleCalendarButton()
                .setText("");
    }
    /**
     * Inicializa el modelo de la tabla y configura su apariencia.
     */
    private void initComponents() {
        descripcionTxt.setRows(4);
        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Producto", "Licencia", "Categorías", "Fecha publicación"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        productosTabla.setModel(modeloTabla);
        // Fondo de la tabla
        productosTabla.setBackground(new Color(220, 220, 220));
        productosTabla.setForeground(Color.BLACK);

        productosTabla.setGridColor(new Color(190, 190, 190));

        productosTabla.setSelectionBackground(new Color(100, 140, 180));
        productosTabla.setSelectionForeground(Color.WHITE);

        productosTabla.getTableHeader().setBackground(new Color(90, 90, 90));
        productosTabla.getTableHeader().setForeground(Color.WHITE);

        productosTabla.getParent().setBackground(new Color(225, 225, 225));
    }
    /**
     * Muestra el panel correspondiente al tipo de producto seleccionado.
     *
     * @param nombre nombre de la tarjeta que se desea mostrar.
     */
    public void mostrarPanel(String nombre) {
        cardLayout.show(panelTipoEspecifico, nombre);
        panelTipoEspecifico.setVisible(true);

        frame.revalidate();
        frame.repaint();
    }
    /**
     * Aplica un estilo visual común a los botones de la aplicación.
     *
     * @param boton botón al que se aplica el estilo.
     */
    private void personalizarBoton(JButton boton) {
        boton.setBackground(new Color(55, 85, 115));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setMargin(new Insets(8, 15, 8, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
