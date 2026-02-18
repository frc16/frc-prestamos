package es.fplumara.dam1.prestamos.service;

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
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con id: " + idMaterial));

        if (material.getEstado() != EstadoMaterial.DISPONIBLE) {
            throw new IllegalStateException(
                    "El material '" + idMaterial + "' no está disponible (estado: " + material.getEstado() + ")"
            );
        }

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
