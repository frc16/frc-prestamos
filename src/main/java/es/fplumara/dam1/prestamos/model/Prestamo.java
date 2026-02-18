package es.fplumara.dam1.prestamos.model;

import java.time.LocalDate;

public class Prestamo implements Identificable{

    private String id;
    private String idMaterial;
    private String profesor;
    private LocalDate fecha;

    public Prestamo(String id, String idMaterial, String profesor, LocalDate fecha){

        this.id = id;
        this.idMaterial = idMaterial;
        this.profesor = profesor;
        this.fecha = fecha;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getProfesor() {
        return profesor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    /*@Override
    public String toString(){
        return "prestamo{id='" + id
                + "', idMaterial='" + idMaterial +
                "',profesor'" + "',fecha=" + fecha + "}";
    }*/
}
