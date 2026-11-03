package com.robertorroba.tiendasoftware.gui;

import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

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
    public JTextField descripcionTxt;
    public JLabel categoriasLbl;
    public JTextField categoriasTxt;
    public JLabel fechaPublicacionLbl;
    public DatePicker fechaPublicacionTxt;
    private JPanel panelLibre;
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
    public JTextField nucleosTxt;
    public JTextField espacioTxt;
    private JPanel panelPrivativo;
    private JPanel panelInferior;
    private JScrollPane productosScroll;
    public JTable productosTabla;
    public JButton nuevobtn;
    public JButton importarBtn;
    public JButton exportarBtn;
    public JButton cerrarBtn;
    public JButton exportarSeleccionadoBtn;
    // añadidos por mi
    public JFrame frame;
    public DefaultTableModel modeloTabla;

    public Ventana() {
        frame = new JFrame("Tienda de Software");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        initComponents();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        fechaPublicacionTxt =new DatePicker();
        fechaPublicacionTxt.getComponentToggleCalendarButton()
                .setIcon(new ImageIcon("resources/img/calendario.png"));

        fechaPublicacionTxt.getComponentToggleCalendarButton()
                .setText("");
    }
    private void initComponents() {

        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Tipo", "Categorías", "Fecha publicación"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        productosTabla.setModel(modeloTabla);
    }
}
