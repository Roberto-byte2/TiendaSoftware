package com.robertorroba.tiendasoftware.gui;

import com.robertorroba.tiendasoftware.base.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Modelo de la aplicación "Tienda de Software".
 * Gestiona la colección de productos (libres y privativos), así como su
 * persistencia, importación y exportación a ficheros XML mediante la API DOM.
 *
 * @author Roberto Torroba
 * @version 1.0
 */
public class TiendaSoftwareModelo {
    /** Lista que almacena todos los productos de la tienda (tanto libres como privativos). */
    public ArrayList<Producto> listaProductos;
    /**
     * Constructor que inicializa la lista de productos vacía.
     */
    public TiendaSoftwareModelo(){
        listaProductos = new ArrayList<>();
    }
    /**
     * Obtiene la lista completa de productos registrados en el modelo.
     *
     * @return Un {@link ArrayList} con todos los productos actuales.
     */
    public ArrayList<Producto> obtenerProductos(){
        return listaProductos;
    }
    /**
     * Da de alta un nuevo producto de tipo Libre en la tienda, siempre y cuando
     * no exista ya otro producto con el mismo nombre.
     *
     * @param nombre Nombre único del software.
     * @param descripcion Descripción detallada del producto.
     * @param categorias Lista de categorías a las que pertenece.
     * @param fechaPublicacion Fecha de lanzamiento o publicación.
     * @param tipoProducto Tipo o clasificación del producto.
     * @param licencia Tipo de licencia libre (ej. GPL, MIT).
     * @param sistemasCompatibles Conjunto de sistemas operativos compatibles.
     */
    public void altaLibre(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                          TipoProducto tipoProducto, String licencia, TreeSet<SistemaOperativo> sistemasCompatibles){
        if(!existeSoftware(nombre)) {
            Libre nuevoLibre = new Libre(nombre, descripcion, categorias, fechaPublicacion,
                    tipoProducto, licencia, sistemasCompatibles);
            listaProductos.add(nuevoLibre);
        }
    }
    /**
     * Da de alta un nuevo producto de tipo Privativo en la tienda, siempre y cuando
     * no exista ya otro producto con el mismo nombre.
     *
     * @param nombre Nombre único del software.
     * @param descripcion Descripción detallada del producto.
     * @param categorias Lista de categorías a las que pertenece.
     * @param fechaPublicacion Fecha de lanzamiento o publicación.
     * @param tipoProducto Tipo o clasificación del producto.
     * @param precio Precio de adquisición del software.
     * @param unidades Número de licencias o unidades disponibles.
     * @param requisitos Mapa con los requisitos técnicos necesarios y sus valores.
     */
    public void altaPrivativo(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                              TipoProducto tipoProducto, double precio, int unidades, HashMap<TipoRequisito,Integer> requisitos){
        if (!existeSoftware(nombre)) {
            Privativo nuevoPrivativo = new Privativo(nombre, descripcion, categorias, fechaPublicacion, tipoProducto,
                    precio, unidades, requisitos);
            listaProductos.add(nuevoPrivativo);
        }
    }
    /**
     * Comprueba si ya existe un producto registrado en la tienda con el nombre indicado
     * (ignorando mayúsculas y minúsculas).
     *
     * @param nombre Nombre del software a buscar.
     * @return {@code true} si el producto ya existe; {@code false} en caso contrario.
     */
    public boolean existeSoftware(String nombre){
        for (Producto p:listaProductos) {
            if(p != null && p.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Exporta todos los productos de la lista actual a un fichero XML utilizando DOM.
     *
     * @param fichero Archivo de destino donde se guardará la estructura XML.
     */
    public void exportarXML(File fichero){

        try {
            // Preparacion del documento
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            DOMImplementation dom = builder.getDOMImplementation();
            Document documento = dom.createDocument(null, "xml", null);

            Element raiz = documento.createElement("Productos");
            documento.getDocumentElement().appendChild(raiz);
            Element nodoProducto=null;
            Element nodoDatos=null;
            Text texto =null;
            for (Producto p: listaProductos) {
                if(p == null) continue;
                if(p instanceof Libre){

                    nodoProducto = documento.createElement("Libre");
                } else if (p instanceof Privativo){
                    nodoProducto = documento.createElement("Privativo");
                }
                raiz.appendChild(nodoProducto);
                nodoDatos = documento.createElement("nombre");
                nodoProducto.appendChild(nodoDatos);
                texto = documento.createTextNode(p.getNombre());
                nodoDatos.appendChild(texto);

                nodoDatos = documento.createElement("descripcion");
                nodoProducto.appendChild(nodoDatos);
                texto = documento.createTextNode(p.getDescripcion());
                nodoDatos.appendChild(texto);

                Element nodoCategorias = documento.createElement("categorias");
                nodoProducto.appendChild(nodoCategorias);

                for (String c : p.getCategorias()) {
                    Element nodoCategoria = documento.createElement("categoria");
                    nodoCategorias.appendChild(nodoCategoria);

                    Text textoCategoria = documento.createTextNode(c);
                    nodoCategoria.appendChild(textoCategoria);
                }

                nodoDatos = documento.createElement("fechaPublicacion");
                nodoProducto.appendChild(nodoDatos);
                texto = documento.createTextNode(p.getFechaPublicacion().toString());
                nodoDatos.appendChild(texto);

                nodoDatos = documento.createElement("tipoProducto");
                nodoProducto.appendChild(nodoDatos);
                texto = documento.createTextNode(p.getTipoProducto().toString());
                nodoDatos.appendChild(texto);

                if (p instanceof Libre) {
                    Libre productoLibre = (Libre) p;
                    // Guardar licencia
                    nodoDatos = documento.createElement("licencia");
                    nodoProducto.appendChild(nodoDatos);
                    texto = documento.createTextNode(productoLibre.getLicencia());
                    nodoDatos.appendChild(texto);

                    // Guardar sistemasCompatibles
                    Element nodoSistemas = documento.createElement("sistemasCompatibles");
                    nodoProducto.appendChild(nodoSistemas);

                    for (SistemaOperativo sistema : productoLibre.getSistemasCompatibles()) {
                        Element nodoSistema = documento.createElement("sistema");
                        nodoSistemas.appendChild(nodoSistema);
                        Text textoSistema = documento.createTextNode(sistema.toString());
                        nodoSistema.appendChild(textoSistema);
                    }
                } else if (p instanceof Privativo) {
                    Privativo productoPrivado = (Privativo) p;
                    // Guardar precio
                    nodoDatos = documento.createElement("precio");
                    nodoProducto.appendChild(nodoDatos);
                    texto = documento.createTextNode(String.valueOf(productoPrivado.getPrecio()));
                    nodoDatos.appendChild(texto);
                    // Guardar unidades
                    nodoDatos = documento.createElement("unidades");
                    nodoProducto.appendChild(nodoDatos);
                    texto = documento.createTextNode(String.valueOf(productoPrivado.getUnidades()));
                    nodoDatos.appendChild(texto);
                    // Guardar requisitos
                    Element nodoRequisitos = documento.createElement("requisitos");
                    nodoProducto.appendChild(nodoRequisitos);
                    for (Map.Entry<TipoRequisito, Integer> requisito :
                            productoPrivado.getRequisitos().entrySet()) {

                        TipoRequisito clave = requisito.getKey();
                        Integer valor = requisito.getValue();

                        Element nodoEtiqueta = documento.createElement(clave.name());
                        nodoRequisitos.appendChild(nodoEtiqueta);

                        texto = documento.createTextNode(String.valueOf(valor));
                        nodoEtiqueta.appendChild(texto);
                    }
                    }else continue;

                }
            Source source = new DOMSource(documento);
            Result resultado = new StreamResult(fichero);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source,resultado);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }


    }

    /**
     * Lee un fichero XML e importa los productos contenidos en él hacia la lista del modelo.
     *
     * @param fichero Archivo XML de origen.
     * @throws ParserConfigurationException Si ocurre un error de configuración en el parser DOM.
     * @throws IOException Si ocurre un error de lectura de ficheros.
     * @throws SAXException Si ocurre un error durante el análisis sintáctico del XML.
     */
        public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
            Libre nuevoLibre = null;
            Privativo nuevoPrivativo = null;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(fichero);
            NodeList listaElementos = documento.getElementsByTagName("*");
            for (int i=0;i<listaElementos.getLength();i++){
                Element nodoProducto = (Element) listaElementos.item(i);
                if(nodoProducto.getTagName().equals("Libre")){
                    nuevoLibre = new Libre();
                    nuevoLibre.setNombre(nodoProducto.getChildNodes().item(0).getTextContent());
                    nuevoLibre.setDescripcion(nodoProducto.getChildNodes().item(1).getTextContent());
                    Element nodoCategorias = (Element) nodoProducto.getChildNodes().item(2);
                    LinkedList<String> categorias = new LinkedList<>();
                    NodeList listaCategorias = nodoCategorias.getChildNodes();

                    for (int j = 0; j < listaCategorias.getLength(); j++) {
                        Element categoria = (Element) listaCategorias.item(j);
                        categorias.add(categoria.getTextContent());
                    }
                    nuevoLibre.setCategorias(categorias);
                    nuevoLibre.setFechaPublicacion(LocalDate.parse(nodoProducto.getChildNodes().item(3).getTextContent()));
                    nuevoLibre.setTipoProducto(TipoProducto.valueOf(nodoProducto.getChildNodes().item(4).getTextContent()));
                    nuevoLibre.setLicencia(nodoProducto.getChildNodes().item(5).getTextContent());
                    Element nodoSistemas = (Element) nodoProducto.getChildNodes().item(6);
                    TreeSet<SistemaOperativo> sistemas = new TreeSet<>();
                    NodeList listaSistemas = nodoSistemas.getChildNodes();
                    for (int j = 0; j < listaSistemas.getLength();j++){
                        Element sistema = (Element) listaSistemas.item(j);
                        sistemas.add(SistemaOperativo.valueOf(sistema.getTextContent()));
                    }
                    nuevoLibre.setSistemasCompatibles(sistemas);
                    if (!existeSoftware(nuevoLibre.getNombre())) {
                        listaProductos.add(nuevoLibre);
                    }


                }else if (nodoProducto.getTagName().equals("Privativo")) {
                    nuevoPrivativo = new Privativo();
                    nuevoPrivativo.setNombre(nodoProducto.getChildNodes().item(0).getTextContent());
                    nuevoPrivativo.setDescripcion(nodoProducto.getChildNodes().item(1).getTextContent());
                    Element nodoCategorias = (Element) nodoProducto.getChildNodes().item(2);
                    LinkedList<String> categorias = new LinkedList<>();
                    NodeList listaCategorias = nodoCategorias.getChildNodes();

                    for (int j = 0; j < listaCategorias.getLength(); j++) {
                        Element categoria = (Element) listaCategorias.item(j);
                        categorias.add(categoria.getTextContent());
                    }
                    nuevoPrivativo.setCategorias(categorias);
                    nuevoPrivativo.setFechaPublicacion(LocalDate.parse(nodoProducto.getChildNodes().item(3).getTextContent()));
                    nuevoPrivativo.setTipoProducto(TipoProducto.valueOf(nodoProducto.getChildNodes().item(4).getTextContent()));
                    nuevoPrivativo.setPrecio(Double.parseDouble(nodoProducto.getChildNodes().item(5).getTextContent()));
                    nuevoPrivativo.setUnidades(Integer.parseInt(nodoProducto.getChildNodes().item(6).getTextContent()));
                    Element nodoRequisitos = (Element) nodoProducto.getChildNodes().item(7);
                    HashMap<TipoRequisito,Integer> requisitos = new HashMap<>();
                    NodeList listaRequisitos = nodoRequisitos.getChildNodes();
                    for (int j = 0; j < listaRequisitos.getLength(); j++) {
                        Element requisito = (Element) listaRequisitos.item(j);

                        TipoRequisito tipo = TipoRequisito.valueOf(requisito.getTagName());
                        Integer valor = Integer.parseInt(requisito.getTextContent());

                        requisitos.put(tipo, valor);
                    }
                    nuevoPrivativo.setRequisitos(requisitos);
                    if (!existeSoftware(nuevoPrivativo.getNombre())) {
                        listaProductos.add(nuevoPrivativo);
                    }
                    }


                }
            }
    /**
     * Exporta un producto individual a un archivo XML existente o nuevo, comprobando
     * previamente si ya está registrado para evitar duplicados.
     *
     * @param fichero Fichero XML donde se desea añadir el producto.
     * @param producto Objeto producto que se quiere exportar.
     * @return {@code true} si se exportó con éxito; {@code false} si ya existía o hubo un error.
     */
    public boolean exportarProductoXML(File fichero, Producto producto) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento;
            Element raiz;

            if (fichero.exists()) {
                documento = builder.parse(fichero);
                raiz = (Element) documento.getElementsByTagName("Productos").item(0);

                if (raiz == null) {
                    throw new IOException("El XML no contiene el nodo Productos");
                }

            } else {
                DOMImplementation dom = builder.getDOMImplementation();
                documento = dom.createDocument(null, "xml", null);

                raiz = documento.createElement("Productos");
                documento.getDocumentElement().appendChild(raiz);
            }

            // Comprobar si el producto ya existe
            if (productoExisteEnXML(raiz, producto.getNombre())) {
                return false;
            }

            // Añadir el producto
            agregarProductoXML(documento, raiz, producto);

            // Guardar el documento
            Source source = new DOMSource(documento);
            Result resultado = new StreamResult(fichero);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, resultado);

            return true;

        } catch (ParserConfigurationException | SAXException |
                IOException | TransformerException e) {

            e.printStackTrace();
            return false;
        }
    }
    /**
     * Método auxiliar privado para comprobar si un producto ya se encuentra reflejado
     * dentro de los nodos de un documento XML DOM cargado.
     *
     * @param raiz Nodo raíz del documento XML ("Productos").
     * @param nombre Nombre del producto a buscar.
     * @return {@code true} si se encuentra en el XML; {@code false} en caso contrario.
     */
    private boolean productoExisteEnXML(Element raiz, String nombre) {

        NodeList productos = raiz.getChildNodes();

        for (int i = 0; i < productos.getLength(); i++) {

            Node nodo = productos.item(i);

            if (nodo.getNodeType() == Node.ELEMENT_NODE) {

                Element elemento = (Element) nodo;

                NodeList nombres = elemento.getElementsByTagName("nombre");

                if (nombres.getLength() > 0) {
                    String nombreXML = nombres.item(0).getTextContent();

                    if (nombreXML.equals(nombre)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    /**
     * Método auxiliar privado encargado de construir y añadir los nodos XML correspondientes
     * a un producto (sea Libre o Privativo) dentro del árbol DOM.
     *
     * @param documento Documento XML en curso.
     * @param raiz Nodo raíz contenedor.
     * @param p Producto del cual se extraerán los datos.
     */
    private void agregarProductoXML(Document documento, Element raiz, Producto p) {

        if (p == null) return;

        Element nodoProducto;

        if (p instanceof Libre) {
            nodoProducto = documento.createElement("Libre");
        } else if (p instanceof Privativo) {
            nodoProducto = documento.createElement("Privativo");
        } else {
            return;
        }

        raiz.appendChild(nodoProducto);

        // Nombre
        Element nodoDatos = documento.createElement("nombre");
        nodoProducto.appendChild(nodoDatos);
        nodoDatos.appendChild(documento.createTextNode(p.getNombre()));

        // Descripción
        nodoDatos = documento.createElement("descripcion");
        nodoProducto.appendChild(nodoDatos);
        nodoDatos.appendChild(documento.createTextNode(p.getDescripcion()));

        // Categorías
        Element nodoCategorias = documento.createElement("categorias");
        nodoProducto.appendChild(nodoCategorias);

        for (String categoria : p.getCategorias()) {
            Element nodoCategoria = documento.createElement("categoria");
            nodoCategorias.appendChild(nodoCategoria);
            nodoCategoria.appendChild(documento.createTextNode(categoria));
        }

        // Fecha de publicación
        nodoDatos = documento.createElement("fechaPublicacion");
        nodoProducto.appendChild(nodoDatos);
        nodoDatos.appendChild(documento.createTextNode(
                p.getFechaPublicacion().toString()));

        // Tipo de producto
        nodoDatos = documento.createElement("tipoProducto");
        nodoProducto.appendChild(nodoDatos);
        nodoDatos.appendChild(documento.createTextNode(
                p.getTipoProducto().toString()));

        // Datos específicos de Libre
        if (p instanceof Libre) {

            Libre productoLibre = (Libre) p;

            nodoDatos = documento.createElement("licencia");
            nodoProducto.appendChild(nodoDatos);
            nodoDatos.appendChild(documento.createTextNode(
                    productoLibre.getLicencia()));

            Element nodoSistemas = documento.createElement("sistemasCompatibles");
            nodoProducto.appendChild(nodoSistemas);

            for (SistemaOperativo sistema :
                    productoLibre.getSistemasCompatibles()) {

                Element nodoSistema = documento.createElement("sistema");
                nodoSistemas.appendChild(nodoSistema);
                nodoSistema.appendChild(documento.createTextNode(
                        sistema.toString()));
            }

        } else if (p instanceof Privativo) {

            Privativo productoPrivado = (Privativo) p;

            // Precio
            nodoDatos = documento.createElement("precio");
            nodoProducto.appendChild(nodoDatos);
            nodoDatos.appendChild(documento.createTextNode(
                    String.valueOf(productoPrivado.getPrecio())));

            // Unidades
            nodoDatos = documento.createElement("unidades");
            nodoProducto.appendChild(nodoDatos);
            nodoDatos.appendChild(documento.createTextNode(
                    String.valueOf(productoPrivado.getUnidades())));

            // Requisitos
            Element nodoRequisitos = documento.createElement("requisitos");
            nodoProducto.appendChild(nodoRequisitos);

            for (Map.Entry<TipoRequisito, Integer> requisito :
                    productoPrivado.getRequisitos().entrySet()) {

                Element nodoEtiqueta = documento.createElement(
                        requisito.getKey().name());

                nodoRequisitos.appendChild(nodoEtiqueta);

                nodoEtiqueta.appendChild(documento.createTextNode(
                        String.valueOf(requisito.getValue())));
            }
        }
    }
    /**
     * Actualiza los datos de un producto existente en la lista reemplazándolo por uno nuevo.
     *
     * @param antiguo El objeto producto original que se desea modificar.
     * @param nuevo El nuevo objeto producto con los datos actualizados.
     * @return {@code true} si se actualizó correctamente; {@code false} si el producto antiguo no se encontró.
     */
    public boolean actualizarProducto(Producto antiguo, Producto nuevo) {

        int posicion = listaProductos.indexOf(antiguo);

        if (posicion == -1) {
            return false;
        }

        listaProductos.set(posicion, nuevo);

        return true;
    }

    }



