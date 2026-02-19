package es.fplumara.dam1.prestamos.service;

import es.fplumara.dam1.prestamos.exception.MaterialNoDisponibleException;
import es.fplumara.dam1.prestamos.model.EstadoMaterial;
import es.fplumara.dam1.prestamos.model.Material;
import es.fplumara.dam1.prestamos.model.Prestamo;
import es.fplumara.dam1.prestamos.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrestamoService {
    private final Repository<Material> materialRepository;
    private final Repository<Prestamo> prestamoRepository;

    public PrestamoService(Repository<Material> materialRepository, Repository<Prestamo> prestamoRepository){
        this.materialRepository = materialRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Crea un préstamo para un material disponible.
     * Cambia el estado del material a PRESTADO.
     */
    public Prestamo crearPrestamo(String idMaterial, String profesor, LocalDate fecha) {

        // identifica si algún parámetro requerido esta nullo o vacío y de ser ais te lanza una excepción
        if (idMaterial == null || idMaterial.isBlank() ||
                profesor == null || profesor.isBlank() ||
                fecha == null) {
            throw new IllegalArgumentException("Los parámetros requeridos no pueden ser nulos o vacíos");
        }

        //busca el ID del material requerido y si no existe te lanza un noEncontradoException
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new MaterialNoDisponibleException("el material solicitado no existe en este sistema"));


        //verifica su estado y si o está disponible te lanza una MaterialNoDisponibleException
        if (material.getEstado() != EstadoMaterial.DISPONIBLE) {
            throw new MaterialNoDisponibleException(
                    "El material '" + idMaterial
                            + "' no está disponible (estado: " + material.getEstado() + ")");
        }
        //para crear un prestamo, cambiar estado y guardar estado
        material.setEstado(EstadoMaterial.PRESTADO);
        materialRepository.save(material);

        String idPrestamo = UUID.randomUUID().toString();
        Prestamo prestamo = new Prestamo(idPrestamo, idMaterial, profesor, fecha);
        prestamoRepository.save(prestamo);

        return prestamo;
    }

    /**
     * Devuelve un material prestado buscando el préstamo activo por idMaterial.
     * Cambia el estado del material a DISPONIBLE y elimina el préstamo.
     */
    public void devolverMaterial(String idMaterial) {
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con id: " + idMaterial));

        // Buscar el préstamo activo asociado a este material
        Prestamo prestamo = prestamoRepository.listAll().stream()
                .filter(p -> p.getIdMaterial().equals(idMaterial))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No existe un préstamo activo para el material: " + idMaterial));

        material.setEstado(EstadoMaterial.DISPONIBLE);
        materialRepository.save(material);

        prestamoRepository.delete(prestamo.getId());
    }

    /**
     * Devuelve la lista de todos los préstamos activos.
     */
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.listAll();
    }
}
