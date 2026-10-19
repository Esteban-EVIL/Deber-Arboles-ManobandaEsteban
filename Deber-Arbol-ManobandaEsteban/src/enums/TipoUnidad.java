package enums;

public enum TipoUnidad {
    BIBLIOTECA,
    AREA,
    ESTANTE;

    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) {
            return false;
        }
        return (this == BIBLIOTECA && hijo == AREA)
                || (this == AREA && hijo == ESTANTE);
    }
}