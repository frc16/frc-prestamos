package es.fplumara.dam1.prestamos.csv;


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * DTO/Record con los campos del CSV.
 * Los alumnos deben convertir esto a su modelo de dominio (Material/Portatil/Proyector).
 */
public record RegistroMaterialCsv(
        String tipo,
        String id,
        String nombre,
        String estado,
        int extra,
        Set<String> etiquetas
) {
    public RegistroMaterialCsv {
        tipo = noVacio(tipo, "tipo");
        id = noVacio(id, "id");
        nombre = noVacio(nombre, "nombre");
        estado = noVacio(estado, "estado");
        etiquetas = etiquetas == null ? Set.of() : Collections.unmodifiableSet(new HashSet<>(etiquetas));
    }

    private static String noVacio(String v, String campo) {
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalArgumentException("Campo obligatorio vacío: " + campo);
        }
        return v.trim();
    }
}
