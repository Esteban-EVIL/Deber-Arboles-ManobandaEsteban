package app;

import java.util.Scanner;
import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

public class Main {
    private final Scanner entrada = new Scanner(System.in);
    private ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaInicial();

    public static void main(String[] args) {
        new Main().ejecutar();
    }

    private void ejecutar() {
        int opcion;
        do {
            System.out.println("\n=== Biblioteca FISEI ===");
            System.out.println("1. Cargar estructura inicial");
            System.out.println("2. Mostrar jerarquia");
            System.out.println("3. Agregar unidad");
            System.out.println("4. Buscar unidad");
            System.out.println("5. Consultar estructura");
            System.out.println("6. Gestionar libros de un estante");
            System.out.println("7. Ejecutar retos del ABB");
            System.out.println("0. Salir");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    biblioteca = DatosPrueba.crearBibliotecaInicial();
                    System.out.println("Estructura inicial cargada.");
                    break;
                case 2:
                    System.out.print(biblioteca.mostrarJerarquia());
                    break;
                case 3:
                    agregarUnidad();
                    break;
                case 4:
                    buscarUnidad();
                    break;
                case 5:
                    consultarEstructura();
                    break;
                case 6:
                    gestionarCatalogo();
                    break;
                case 7:
                    ejecutarRetos();
                    break;
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private void agregarUnidad() {
        String padre = leerTexto("Codigo del padre: ");
        String codigo = leerTexto("Codigo nuevo: ");
        String nombre = leerTexto("Nombre: ");
        System.out.println("Tipo: 1. AREA  2. ESTANTE");
        int tipoOpcion = leerEntero("Tipo: ");
        TipoUnidad tipo = tipoOpcion == 1 ? TipoUnidad.AREA
                : tipoOpcion == 2 ? TipoUnidad.ESTANTE : null;
        if (tipo == null) {
            System.out.println("Tipo no valido.");
            return;
        }
        try {
            boolean agregado = biblioteca.agregar(padre,
                    new UnidadBiblioteca(codigo, nombre, tipo));
            System.out.println(agregado ? "Unidad agregada." : "No se pudo agregar la unidad.");
        } catch (IllegalArgumentException error) {
            System.out.println("Datos invalidos: " + error.getMessage());
        }
    }

    private void buscarUnidad() {
        String codigo = leerTexto("Codigo de unidad: ");
        NodoUnidad unidad = biblioteca.buscar(codigo);
        System.out.println(unidad == null ? "Unidad no encontrada." : unidad.getDato());
    }

    private void consultarEstructura() {
        String codigo = leerTexto("Codigo para consultar nivel: ");
        System.out.println("Altura: " + biblioteca.altura());
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
    }

    private void gestionarCatalogo() {
        String codigoEstante = leerTexto("Codigo del estante: ");
        NodoUnidad estante = biblioteca.buscar(codigoEstante);
        if (estante == null || estante.getCatalogo() == null) {
            System.out.println("No existe un estante con ese codigo.");
            return;
        }
        CatalogoABB catalogo = estante.getCatalogo();
        int opcion;
        do {
            System.out.println("\nEstante " + estante.getDato().getCodigo()
                    + " | libros: " + catalogo.contar());
            System.out.println("1. Insertar libro");
            System.out.println("2. Buscar libro");
            System.out.println("3. Modificar libro");
            System.out.println("4. Eliminar libro");
            System.out.println("5. Mostrar recorridos y trazas");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    insertarLibro(catalogo);
                    break;
                case 2:
                    buscarLibro(catalogo);
                    break;
                case 3:
                    modificarLibro(catalogo);
                    break;
                case 4:
                    eliminarLibro(catalogo);
                    break;
                case 5:
                    mostrarRecorridos(catalogo);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private void insertarLibro(CatalogoABB catalogo) {
        try {
            Libro libro = leerLibro();
            System.out.println(catalogo.insertar(libro)
                    ? "Libro insertado." : "Ya existe un libro con ese codigo.");
        } catch (IllegalArgumentException error) {
            System.out.println("Datos invalidos: " + error.getMessage());
        }
    }

    private void buscarLibro(CatalogoABB catalogo) {
        Libro libro = catalogo.buscar(leerEntero("Codigo del libro: "));
        System.out.println(libro == null ? "Libro no encontrado." : libro);
    }

    private void modificarLibro(CatalogoABB catalogo) {
        int codigo = leerEntero("Codigo del libro a modificar: ");
        String titulo = leerTexto("Nuevo titulo: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo anio (1900-2026): ");
        try {
            System.out.println(catalogo.modificar(codigo, titulo, autor, anio)
                    ? "Libro modificado." : "Libro no encontrado.");
        } catch (IllegalArgumentException error) {
            System.out.println("Datos invalidos: " + error.getMessage());
        }
    }

    private void eliminarLibro(CatalogoABB catalogo) {
        int codigo = leerEntero("Codigo del libro a eliminar: ");
        System.out.println(catalogo.eliminar(codigo)
                ? "Libro eliminado." : "Libro no encontrado.");
    }

    private void mostrarRecorridos(CatalogoABB catalogo) {
        mostrarRecorrido(catalogo, TipoRecorrido.PREORDEN);
        mostrarRecorrido(catalogo, TipoRecorrido.INORDEN);
        mostrarRecorrido(catalogo, TipoRecorrido.POSORDEN);
    }

    private void mostrarRecorrido(CatalogoABB catalogo, TipoRecorrido tipo) {
        System.out.println("\n--- " + tipo + " ---");
        System.out.println(catalogo.trazaRecorrido(tipo));
    }

    private void ejecutarRetos() {
        NodoUnidad estante = biblioteca.buscar(leerTexto("Codigo del estante: "));
        if (estante == null || estante.getCatalogo() == null) {
            System.out.println("No existe un estante con ese codigo.");
            return;
        }
        CatalogoABB catalogo = estante.getCatalogo();
        System.out.println("1. Contar codigos en rango");
        System.out.println("2. Mostrar camino de busqueda");
        System.out.println("3. Validar ABB");
        int opcion = leerEntero("Opcion: ");
        switch (opcion) {
            case 1:
                int a = leerEntero("Limite inferior: ");
                int b = leerEntero("Limite superior: ");
                System.out.println("Cantidad: " + catalogo.contarEnRango(a, b));
                break;
            case 2:
                System.out.println("Camino: " + catalogo.caminoBusqueda(leerEntero("Codigo: ")));
                break;
            case 3:
                System.out.println("ABB valido: " + catalogo.esABBValido());
                break;
            default:
                System.out.println("Opcion no valida.");
        }
    }

    private Libro leerLibro() {
        int codigo = leerEntero("Codigo positivo: ");
        String titulo = leerTexto("Titulo: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Anio (1900-2026): ");
        return new Libro(codigo, titulo, autor, anio);
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException error) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }
}