package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.NodoUnidad;

public final class DatosPrueba {
    private DatosPrueba() {
    }

    public static ArbolBiblioteca crearBibliotecaInicial() {
        ArbolBiblioteca biblioteca = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        biblioteca.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        biblioteca.agregar("BIB", new UnidadBiblioteca("TI",
                "Tecnologias de la Informacion", TipoUnidad.AREA));
        biblioteca.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        biblioteca.agregar("SW", new UnidadBiblioteca("SW-E1",
                "Estante 1 de Software", TipoUnidad.ESTANTE));
        biblioteca.agregar("SW", new UnidadBiblioteca("SW-E2",
                "Estante 2 de Software", TipoUnidad.ESTANTE));
        biblioteca.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));

        insertarLibros(biblioteca.buscar("SW-E1"), new int[] {60, 35, 80, 20, 45, 70, 90, 40, 75});
        insertarLibros(biblioteca.buscar("SW-E2"), new int[] {15, 10, 25});
        insertarLibros(biblioteca.buscar("TI-E1"), new int[] {50});
        return biblioteca;
    }

    private static void insertarLibros(NodoUnidad estante, int[] codigos) {
        insertarLibrosRec(estante, codigos, 0);
    }

    private static void insertarLibrosRec(NodoUnidad estante, int[] codigos, int indice) {
        if (indice >= codigos.length) {
            return;
        }
        int codigo = codigos[indice];
        estante.getCatalogo().insertar(
                new Libro(codigo, "Libro " + codigo, "Autor desconocido", 2020));
        insertarLibrosRec(estante, codigos, indice + 1);
    }
}