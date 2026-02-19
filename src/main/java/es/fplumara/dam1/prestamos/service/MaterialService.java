package es.fplumara.dam1.prestamos.service;

import es.fplumara.dam1.prestamos.exception.DuplicadoException;
import es.fplumara.dam1.prestamos.exception.MaterialNoDisponibleException;
import es.fplumara.dam1.prestamos.exception.NoEncontradoException;
import es.fplumara.dam1.prestamos.model.EstadoMaterial;
import es.fplumara.dam1.prestamos.model.Material;
import es.fplumara.dam1.prestamos.repository.Repository;

import java.util.List;
public class MaterialService {


    private final Repository<Material> materialRepository;

    // Constructor
    public MaterialService(Repository<Material> materialRepository) {
        this.materialRepository = materialRepository;
    }

    // Método
    public Repository<Material> getMaterialRepository() {
        return materialRepository;
    }

    /**
     * Registra un nuevo material en el sistema.
     */
    public void registrarMaterial(Material m) {

        // Validar que m y su id no sea null o lanza una exception
        if (m == null || m.getId() == null || m.getId().isBlank()) {
            throw new IllegalArgumentException("El material o su id no pueden ser nulos o vacíos");
        }

        // Comprobar si ya existe un material con el mismo id
        if (materialRepository.findById(m.getId()).isPresent()) {
            throw new DuplicadoException("Ya existe un material con id: " + m.getId());
        }

        materialRepository.save(m);
    }

    /**
     * Da de baja un material cambiando su estado a BAJA.
     */
    public void darDeBaja(String idMaterial) {
        // Buscar el material, lanzar excepción si no existe
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new NoEncontradoException("Material no encontrado con id: " + idMaterial));

        // Si ya está en BAJA, lanzar excepción
        if (material.getEstado() == EstadoMaterial.BAJA) {
            throw new MaterialNoDisponibleException("El material con id: " + idMaterial + " ya está en BAJA");
        }

        // Cambiar estado y guardar
        material.setEstado(EstadoMaterial.BAJA);
        materialRepository.save(material);
    }

    /**
     * Devuelve la lista de todos los materiales.
     */
    public List<Material> listar() {
        return materialRepository.listAll();
    }
}
