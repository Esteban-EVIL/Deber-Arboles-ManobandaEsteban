package negocio;

import java.util.List;
import java.util.Locale;
import modelo.UnidadBiblioteca;

public class ArbolBiblioteca {
    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca raiz) {
        if (raiz == null || raiz.getTipo() != enums.TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raiz debe ser una unidad tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(raiz);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null || buscar(nueva.getCodigo()) != null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || !padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.getHijos().add(new NodoUnidad(nueva));
        return true;
    }

    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscarRec(raiz, codigo.trim().toUpperCase(Locale.ROOT));
    }

    private NodoUnidad buscarRec(NodoUnidad nodo, String codigo) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nodo;
        }
        return buscarHijoRec(nodo.getHijos(), 0, codigo);
    }

    private NodoUnidad buscarHijoRec(List<NodoUnidad> hijos, int indice, String codigo) {
        if (indice >= hijos.size()) {
            return null;
        }
        NodoUnidad encontrado = buscarRec(hijos.get(indice), codigo);
        return encontrado != null ? encontrado : buscarHijoRec(hijos, indice + 1, codigo);
    }

    public int altura() {
        return alturaRec(raiz);
    }

    private int alturaRec(NodoUnidad nodo) {
        return alturaHijosRec(nodo.getHijos(), 0) + 1;
    }

    private int alturaHijosRec(List<NodoUnidad> hijos, int indice) {
        if (indice >= hijos.size()) {
            return -1;
        }
        int alturaActual = alturaRec(hijos.get(indice));
        int alturaResto = alturaHijosRec(hijos, indice + 1);
        return Math.max(alturaActual, alturaResto);
    }

    public int contarHojas() {
        return contarHojasRec(raiz);
    }

    private int contarHojasRec(NodoUnidad nodo) {
        if (nodo.getHijos().isEmpty()) {
            return 1;
        }
        return contarHojasHijosRec(nodo.getHijos(), 0);
    }

    private int contarHojasHijosRec(List<NodoUnidad> hijos, int indice) {
        return indice >= hijos.size() ? 0
                : contarHojasRec(hijos.get(indice)) + contarHojasHijosRec(hijos, indice + 1);
    }

    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivelRec(raiz, codigo.trim().toUpperCase(Locale.ROOT), 0);
    }

    private int nivelRec(NodoUnidad nodo, String codigo, int nivel) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nivel;
        }
        return nivelHijosRec(nodo.getHijos(), 0, codigo, nivel + 1);
    }

    private int nivelHijosRec(List<NodoUnidad> hijos, int indice, String codigo, int nivel) {
        if (indice >= hijos.size()) {
            return -1;
        }
        int encontrado = nivelRec(hijos.get(indice), codigo, nivel);
        return encontrado >= 0 ? encontrado : nivelHijosRec(hijos, indice + 1, codigo, nivel);
    }

    public String mostrarJerarquia() {
        StringBuilder texto = new StringBuilder();
        mostrarRec(raiz, 0, texto);
        return texto.toString();
    }

    private void mostrarRec(NodoUnidad nodo, int nivel, StringBuilder texto) {
        espacios(texto, nivel * 2);
        texto.append(nodo.getDato().getCodigo()).append(' ')
                .append(nodo.getDato().getNombre()).append('\n');
        mostrarHijosRec(nodo.getHijos(), 0, nivel + 1, texto);
    }

    private void mostrarHijosRec(List<NodoUnidad> hijos, int indice, int nivel, StringBuilder texto) {
        if (indice >= hijos.size()) {
            return;
        }
        mostrarRec(hijos.get(indice), nivel, texto);
        mostrarHijosRec(hijos, indice + 1, nivel, texto);
    }

    private void espacios(StringBuilder texto, int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        texto.append("  ");
        espacios(texto, cantidad - 2);
    }

    public int contarLibrosTotal() {
        return contarLibrosRec(raiz);
    }

    private int contarLibrosRec(NodoUnidad nodo) {
        int libros = nodo.getCatalogo() == null ? 0 : nodo.getCatalogo().contar();
        return libros + contarLibrosHijosRec(nodo.getHijos(), 0);
    }

    private int contarLibrosHijosRec(List<NodoUnidad> hijos, int indice) {
        return indice >= hijos.size() ? 0
                : contarLibrosRec(hijos.get(indice)) + contarLibrosHijosRec(hijos, indice + 1);
    }
}