package negocio;

import java.util.ArrayList;
import java.util.List;
import enums.TipoRecorrido;
import modelo.Libro;

public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    public boolean insertar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null.");
        }
        boolean[] insertado = {false};
        raiz = insertarRec(raiz, libro, insertado);
        return insertado[0];
    }

    private NodoLibro insertarRec(NodoLibro nodo, Libro libro, boolean[] insertado) {
        if (nodo == null) {
            insertado[0] = true;
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), libro, insertado));
        } else if (libro.getCodigo() > nodo.getDato().getCodigo()) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), libro, insertado));
        }
        return nodo;
    }

    public Libro buscar(int codigo) {
        return buscarRec(raiz, codigo);
    }

    private Libro buscarRec(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo == nodo.getDato().getCodigo()) {
            return nodo.getDato();
        }
        return codigo < nodo.getDato().getCodigo()
                ? buscarRec(nodo.getIzquierdo(), codigo)
                : buscarRec(nodo.getDerecho(), codigo);
    }

    public int contar() {
        return contarRec(raiz);
    }

    private int contarRec(NodoLibro nodo) {
        return nodo == null ? 0 : 1 + contarRec(nodo.getIzquierdo()) + contarRec(nodo.getDerecho());
    }

    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        List<Libro> libros = new ArrayList<Libro>();
        recorrerRec(raiz, tipo, libros);
        return libros;
    }

    private void recorrerRec(NodoLibro nodo, TipoRecorrido tipo, List<Libro> libros) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            libros.add(nodo.getDato());
        }
        recorrerRec(nodo.getIzquierdo(), tipo, libros);
        if (tipo == TipoRecorrido.INORDEN) {
            libros.add(nodo.getDato());
        }
        recorrerRec(nodo.getDerecho(), tipo, libros);
        if (tipo == TipoRecorrido.POSORDEN) {
            libros.add(nodo.getDato());
        }
    }

    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        StringBuilder traza = new StringBuilder();
        List<Libro> secuencia = new ArrayList<Libro>();
        trazaRecorridoRec(raiz, tipo, 0, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(": ");
        agregarCodigos(secuencia, 0, traza);
        return traza.toString();
    }

    private void trazaRecorridoRec(NodoLibro nodo, TipoRecorrido tipo, int nivel,
            StringBuilder traza, List<Libro> secuencia) {
        if (nodo == null) {
            espacios(traza, nivel);
            traza.append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        espacios(traza, nivel);
        traza.append("entra ").append(codigo).append('\n');
        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(nodo, nivel, traza, secuencia);
        }
        trazaRecorridoRec(nodo.getIzquierdo(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(nodo, nivel, traza, secuencia);
        }
        trazaRecorridoRec(nodo.getDerecho(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(nodo, nivel, traza, secuencia);
        }
    }

    private void visitar(NodoLibro nodo, int nivel, StringBuilder traza, List<Libro> secuencia) {
        espacios(traza, nivel);
        traza.append("VISITA ").append(nodo.getDato().getCodigo()).append('\n');
        secuencia.add(nodo.getDato());
    }

    private void espacios(StringBuilder texto, int nivel) {
        int cantidad = nivel * 2;
        for (int indice = 0; indice < cantidad; indice++) {
            texto.append(' ');
        }
    }

    private void agregarCodigos(List<Libro> libros, int indice, StringBuilder texto) {
        if (indice == libros.size()) {
            return;
        }
        if (indice > 0) {
            texto.append(' ');
        }
        texto.append(libros.get(indice).getCodigo());
        agregarCodigos(libros, indice + 1, texto);
    }

    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) {
            return false;
        }
        libro.actualizar(titulo, autor, anio);
        return true;
    }

    public boolean eliminar(int codigo) {
        boolean[] eliminado = {false};
        raiz = eliminarRec(raiz, codigo, eliminado);
        return eliminado[0];
    }

    private NodoLibro eliminarRec(NodoLibro nodo, int codigo, boolean[] eliminado) {
        if (nodo == null) {
            return null;
        }
        if (codigo < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), codigo, eliminado));
            return nodo;
        }
        if (codigo > nodo.getDato().getCodigo()) {
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), codigo, eliminado));
            return nodo;
        }
        eliminado[0] = true;
        if (nodo.getIzquierdo() == null) {
            return nodo.getDerecho();
        }
        if (nodo.getDerecho() == null) {
            return nodo.getIzquierdo();
        }
        NodoLibro sucesor = minimo(nodo.getDerecho());
        nodo.setDato(sucesor.getDato());
        nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor.getDato().getCodigo(), eliminado));
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }
        return minimo(nodo.getIzquierdo());
    }

    public int contarEnRango(int a, int b) {
        return a > b ? 0 : contarEnRangoRec(raiz, a, b);
    }

    private int contarEnRangoRec(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            return contarEnRangoRec(nodo.getDerecho(), a, b);
        }
        if (codigo > b) {
            return contarEnRangoRec(nodo.getIzquierdo(), a, b);
        }
        return 1 + contarEnRangoRec(nodo.getIzquierdo(), a, b)
                + contarEnRangoRec(nodo.getDerecho(), a, b);
    }

    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<Integer>();
        caminoBusquedaRec(raiz, codigo, camino);
        return camino;
    }

    private boolean caminoBusquedaRec(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return false;
        }
        camino.add(nodo.getDato().getCodigo());
        if (codigo == nodo.getDato().getCodigo()) {
            return true;
        }
        return codigo < nodo.getDato().getCodigo()
                ? caminoBusquedaRec(nodo.getIzquierdo(), codigo, camino)
                : caminoBusquedaRec(nodo.getDerecho(), codigo, camino);
    }

    public boolean esABBValido() {
        return esABBValidoRec(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esABBValidoRec(NodoLibro nodo, long minimo, long maximo) {
        if (nodo == null) {
            return true;
        }
        int codigo = nodo.getDato().getCodigo();
        return codigo > minimo && codigo < maximo
                && esABBValidoRec(nodo.getIzquierdo(), minimo, codigo)
                && esABBValidoRec(nodo.getDerecho(), codigo, maximo);
    }

    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException("El preorden no puede ser null.");
        }
        CatalogoABB catalogo = new CatalogoABB();
        int[] indice = {0};
        catalogo.raiz = construirDesdePreorden(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("La secuencia no es un preorden valido de ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construirDesdePreorden(int[] preorden, int[] indice,
            long minimo, long maximo) {
        if (indice[0] >= preorden.length) {
            return null;
        }
        int codigo = preorden[indice[0]];
        if (codigo <= minimo || codigo >= maximo) {
            return null;
        }
        indice[0]++;
        Libro libro = new Libro(codigo, "Libro " + codigo, "Autor desconocido", 2020);
        NodoLibro nodo = new NodoLibro(libro);
        nodo.setIzquierdo(construirDesdePreorden(preorden, indice, minimo, codigo));
        nodo.setDerecho(construirDesdePreorden(preorden, indice, codigo, maximo));
        return nodo;
    }
}