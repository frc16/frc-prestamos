package es.fplumara.dam1.prestamos.model;

public class Proyector extends Material{
    private int lumens;

    public Proyector(String id, String nombre, EstadoMaterial estado, int lumens){
         super(id, nombre, estado);
         this.lumens = lumens;
    }

    @Override

    public String getTipo(){
        return "Proyector";
    }

    public int getLumens() {
        return lumens;
    }

    /*  @Override
    public String toString() {
        return "Proyector{id='" + getId() + "', nombre='" + getNombre() + "', estado=" + getEstado()
                + ", lumens=" + lumens + ", etiquetas=" + getEtiquetas() + "}";
    } */
}
