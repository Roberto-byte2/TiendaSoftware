# TiendaSoftware

Aplicación de escritorio desarrollada en **Java** para la gestión de productos de una tienda de software.

Este proyecto ha sido desarrollado como práctica de la asignatura **Acceso a Datos** del ciclo formativo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

## 📋 Descripción

TiendaSoftware permite gestionar diferentes tipos de productos de software mediante una interfaz gráfica desarrollada con **Java Swing**.

La aplicación utiliza un modelo basado en **herencia**, diferenciando entre productos de software **libre** y **privativo**, cada uno con sus propias características.

Los productos pueden ser gestionados desde la aplicación e importados o exportados en formato **XML**.

## ✨ Funcionalidades

- Alta de productos de software.
- Gestión de productos **libres** y **privativos**.
- Clasificación de productos como **software** o **herramientas**.
- Visualización y edición de productos.
- Gestión de categorías.
- Selección de fecha de publicación.
- Configuración específica según el tipo de producto:
  - **Productos libres:** licencia y sistemas operativos compatibles.
  - **Productos privativos:** precio, unidades y requisitos mínimos.
- Importación de datos desde archivos XML.
- Exportación de productos a formato XML.
- Exportación del producto seleccionado.
- Interfaz gráfica desarrollada con Java Swing.

## 🧩 Modelo de datos

La aplicación utiliza una estructura basada en herencia:

```text
Producto (clase abstracta)
│
├── Libre
│   ├── Licencia
│   └── Sistemas operativos compatibles
│
└── Privativo
    ├── Precio
    ├── Unidades
    └── Requisitos mínimos
```

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Java Swing** para la interfaz gráfica.
- **XML** para la importación y exportación de datos.
- **IntelliJ IDEA** como entorno de desarrollo.
- **Git y GitHub** para el control de versiones.

### Librerías externas

- **LGoodDatePicker** para la selección de fechas.
- **JGoodies Forms**.
- **JGoodies Common**.

## 🚀 Cómo ejecutar la aplicación

### Opción 1: Ejecutar el JAR

El archivo ejecutable se encuentra en la carpeta `dist`.

Es necesario tener instalado **Java 17 o una versión compatible**.

Desde la carpeta principal del proyecto:

```bash
java -jar dist/TiendaSoftware.jar
```

### Opción 2: Ejecutar desde el código fuente

1. Clona el repositorio:

```bash
git clone https://github.com/Roberto-byte2/TiendaSoftware.git
```

2. Abre el proyecto en IntelliJ IDEA.

3. Comprueba que las librerías necesarias estén correctamente configuradas.

4. Ejecuta la clase principal:

```text
com.robertorroba.tiendasoftware.Principal
```

## 📚 Documentación

El proyecto incluye documentación **JavaDoc** generada para facilitar la comprensión de las clases y métodos utilizados.

## 📁 Estructura del proyecto

```text
TiendaSoftware/
├── src/          # Código fuente
├── resources/    # Recursos de la aplicación
├── libs/         # Librerías externas
├── javadoc/      # Documentación JavaDoc
├── dist/         # Aplicación ejecutable
└── README.md     # Documentación del proyecto
```

## 👨‍💻 Autor

**Roberto**

Estudiante de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.