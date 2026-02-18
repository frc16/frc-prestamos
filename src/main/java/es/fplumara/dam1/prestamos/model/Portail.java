package es.fplumara.dam1.prestamos.model;

public class Portail extends Material{
    private int ramGB;

    public Portail(String id, String nombre, EstadoMaterial estado, int ramGB){
        super(id, nombre, estado);
        this.ramGB = ramGB;

    }
    @Override
    public String getTipo(){
        return "Portail";
    }

    public int getRamGB() {
        return ramGB;
    }

    /*  @Override
    public String toString() {
        return "Portatil{id='" + getId() + "', nombre='" + getNombre() + "', estado=" + getEstado()
                + ", ramGB=" + ramGB + ", etiquetas=" + getEtiquetas() + "}";
    } */
}
