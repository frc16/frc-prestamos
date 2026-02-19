package es.fplumara.dam1.prestamos.model;

public class Portatil extends Material{
    private int ramGB;

    public Portatil(String id, String nombre, EstadoMaterial estado, int ramGB){
        super(id, nombre, estado);
        this.ramGB = ramGB;

    }
    @Override
    public String getTipo(){
        return "Portatil";
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
