package negocio;

import java.util.ArrayList;
import java.util.List;
import modelo.UnidadBiblioteca;
import enums.TipoUnidad;

public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) {
            throw new IllegalArgumentException("La unidad no puede ser null.");
        }
        this.dato = dato;
        this.hijos = new ArrayList<NodoUnidad>();
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public List<NodoUnidad> getHijos() {
        return hijos;
    }

    public CatalogoABB getCatalogo() {
        return catalogo;
    }
}