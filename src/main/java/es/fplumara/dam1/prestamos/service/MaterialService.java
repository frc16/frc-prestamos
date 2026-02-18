package es.fplumara.dam1.prestamos.service;

import es.fplumara.dam1.prestamos.model.EstadoMaterial;
import es.fplumara.dam1.prestamos.model.Material;
import es.fplumara.dam1.prestamos.repository.Repository;

import java.util.List;

public class MaterialService {
    private final Repository<Material> materialRepository;

    //constructor
    public MaterialService(Repository<Material> materialRepository){

        this.materialRepository = materialRepository;

    }

    //metodo

    public Repository<Material> getMaterialRepository() {
        return materialRepository;
    }

    /**
     * Registra un nuevo material en el sistema.
     */
    public void registrarMaterial(Material m) {



        }
        materialRepository.save(m);
    }

    /**
     * Da de baja un material cambiando su estado a BAJA.
     */
    public void darDeBaja(String idMaterial) {
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con id: " + idMaterial));
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
