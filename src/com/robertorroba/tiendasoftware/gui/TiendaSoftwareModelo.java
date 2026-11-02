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

public class TiendaSoftwareModelo {
    public ArrayList<Producto> listaProductos;

    public TiendaSoftwareModelo(){
        listaProductos = new ArrayList<>();
    }
    public ArrayList<Producto> obtenerProductos(){
        return listaProductos;
    }
    public void altaLibre(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                          TipoProducto tipoProducto, String licencia, TreeSet<SistemaOperativo> sistemasCompatibles){
        if(!existeSoftware(nombre)) {
            Libre nuevoLibre = new Libre(nombre, descripcion, categorias, fechaPublicacion,
                    tipoProducto, licencia, sistemasCompatibles);
            listaProductos.add(nuevoLibre);
        }
    }
    public void altaPrivativo(String nombre, String descripcion, LinkedList<String> categorias, LocalDate fechaPublicacion,
                              TipoProducto tipoProducto, double precio, int unidades, HashMap<TipoRequisito,Integer> requisitos){
        if (!existeSoftware(nombre)) {
            Privativo nuevoPrivativo = new Privativo(nombre, descripcion, categorias, fechaPublicacion, tipoProducto,
                    precio, unidades, requisitos);
            listaProductos.add(nuevoPrivativo);
        }
    }
    public boolean existeSoftware(String nombre){
        for (Producto p:listaProductos) {
            if(p != null && p.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }
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
        public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
            listaProductos= new ArrayList<Producto>();
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
                    listaProductos.add(nuevoLibre);

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
                    listaProductos.add(nuevoPrivativo);
                    }


                }
            }


    }



