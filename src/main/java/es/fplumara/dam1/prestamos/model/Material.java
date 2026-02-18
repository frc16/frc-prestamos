package es.fplumara.dam1.prestamos.model;

import java.util.HashSet;
import java.util.Set;

public abstract class Material implements Identificable {

    private String id;
    private String nombre;
    private EstadoMaterial estado;
    private Set<String> etiquetas;

    public Material(String id, String nombre, EstadoMaterial estado){

        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.etiquetas = new HashSet<>();
    }

    public abstract String getTipo();

    @Override
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public EstadoMaterial getEstado() {
        return estado;
    }

    public Set<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEstado(EstadoMaterial estado) {
        this.estado = estado;
    }
}
