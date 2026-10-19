package app;

import java.util.List;
import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

public final class PruebasBiblioteca {
    private static int correctas;
    private static int total;

    private PruebasBiblioteca() {
    }

    public static void main(String[] args) {
        probar("biblioteca solo con raiz", pruebaRaizSola());
        probar("estructura inicial", pruebaEstructuraInicial());
        probar("niveles y busqueda sin mayusculas", pruebaNiveles());
        probar("rechazo de estante bajo biblioteca", pruebaRelacionInvalida());
        probar("recorrido preorden", pruebaPreorden());
        probar("recorrido inorden", pruebaInorden());
        probar("recorrido posorden", pruebaPosorden());
        probar("rechazo de codigo duplicado", pruebaDuplicado());
        probar("camino y rango", pruebaCaminoYRango());
        probar("eliminaciones hoja, un hijo y dos hijos", pruebaEliminaciones());
        probar("buscar, modificar y eliminar inexistente", pruebaInexistente());
        probar("validacion de libro invalido", pruebaLibroInvalido());
        probar("validacion global del ABB", pruebaAbbInvalido());
        probar("reconstruccion desde preorden", pruebaDesdePreorden());
        System.out.println(correctas + "/" + total + " pruebas OK");
    }

    private static void probar(String nombre, boolean resultado) {
        total++;
        if (resultado) {
            correctas++;
            System.out.println("OK - " + nombre);
        } else {
            System.out.println("FALLO - " + nombre);
        }
    }

    private static boolean pruebaRaizSola() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca", TipoUnidad.BIBLIOTECA));
        return arbol.altura() == 0 && arbol.contarHojas() == 1;
    }

    private static boolean pruebaEstructuraInicial() {
        ArbolBiblioteca arbol = DatosPrueba.crearBibliotecaInicial();
        return arbol.altura() == 2 && arbol.contarHojas() == 4 && arbol.contarLibrosTotal() == 13;
    }

    private static boolean pruebaNiveles() {
        ArbolBiblioteca arbol = DatosPrueba.crearBibliotecaInicial();
        return arbol.nivel("BIB") == 0 && arbol.nivel("sw") == 1
                && arbol.nivel("SW-E2") == 2 && arbol.nivel("XYZ") == -1;
    }

    private static boolean pruebaRelacionInvalida() {
        ArbolBiblioteca arbol = DatosPrueba.crearBibliotecaInicial();
        return !arbol.agregar("BIB", new UnidadBiblioteca("NUEVO", "Estante invalido", TipoUnidad.ESTANTE));
    }

    private static boolean pruebaPreorden() {
        return codigos(armarCatalogo()).equals("60 35 20 45 40 80 70 75 90");
    }

    private static boolean pruebaInorden() {
        return codigos(armarCatalogo()).equals("60 35 20 45 40 80 70 75 90")
                && secuencia(armarCatalogo(), TipoRecorrido.INORDEN)
                        .equals("20 35 40 45 60 70 75 80 90");
    }

    private static boolean pruebaPosorden() {
        return secuencia(armarCatalogo(), TipoRecorrido.POSORDEN)
                .equals("20 40 45 35 75 70 90 80 60");
    }

    private static boolean pruebaDuplicado() {
        CatalogoABB catalogo = armarCatalogo();
        return !catalogo.insertar(new Libro(60, "Duplicado", "Autor", 2020));
    }

    private static boolean pruebaCaminoYRango() {
        CatalogoABB catalogo = armarCatalogo();
        return catalogo.caminoBusqueda(42).toString().equals("[60, 35, 45, 40]")
                && catalogo.contarEnRango(40, 75) == 5;
    }

    private static boolean pruebaEliminaciones() {
        CatalogoABB catalogo = armarCatalogo();
        catalogo.insertar(new Libro(42, "Libro 42", "Autor", 2020));
        return catalogo.eliminar(20) && catalogo.eliminar(70) && catalogo.eliminar(60)
                && secuencia(catalogo, TipoRecorrido.PREORDEN).equals("75 35 45 40 42 80 90");
    }

    private static boolean pruebaInexistente() {
        CatalogoABB catalogo = armarCatalogo();
        return catalogo.buscar(999) == null && !catalogo.modificar(999, "X", "Y", 2020)
                && !catalogo.eliminar(999);
    }

    private static boolean pruebaLibroInvalido() {
        try {
            new Libro(0, "Libro", "Autor", 2020);
            return false;
        } catch (IllegalArgumentException esperado) {
            return true;
        }
    }

    private static boolean pruebaAbbInvalido() {
        NodoLibro raiz = nodo(60);
        NodoLibro hijoIzquierdo = nodo(35);
        hijoIzquierdo.setDerecho(nodo(70));
        raiz.setIzquierdo(hijoIzquierdo);
        raiz.setDerecho(nodo(80));
        CatalogoABB catalogo = new CatalogoABB();
        catalogo.setRaiz(raiz);
        return !catalogo.esABBValido();
    }

    private static boolean pruebaDesdePreorden() {
        CatalogoABB catalogo = CatalogoABB.desdePreorden(new int[] {60, 35, 20, 45, 40, 80, 70, 75, 90});
        return catalogo.esABBValido()
                && secuencia(catalogo, TipoRecorrido.INORDEN).equals("20 35 40 45 60 70 75 80 90");
    }

    private static CatalogoABB armarCatalogo() {
        return DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
    }

    private static NodoLibro nodo(int codigo) {
        return new NodoLibro(new Libro(codigo, "Libro " + codigo, "Autor", 2020));
    }

    private static String codigos(CatalogoABB catalogo) {
        return secuencia(catalogo, TipoRecorrido.PREORDEN);
    }

    private static String secuencia(CatalogoABB catalogo, TipoRecorrido tipo) {
        StringBuilder texto = new StringBuilder();
        agregarCodigo(catalogo.recorrer(tipo), 0, texto);
        return texto.toString();
    }

    private static void agregarCodigo(List<Libro> libros, int indice, StringBuilder texto) {
        if (indice >= libros.size()) {
            return;
        }
        if (indice > 0) {
            texto.append(' ');
        }
        texto.append(libros.get(indice).getCodigo());
        agregarCodigo(libros, indice + 1, texto);
    }
}