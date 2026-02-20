package es.fplumara.dam1.prestamos.service;

import es.fplumara.dam1.prestamos.model.EstadoMaterial;
import es.fplumara.dam1.prestamos.model.Material;
import es.fplumara.dam1.prestamos.model.Portatil;
import es.fplumara.dam1.prestamos.model.Prestamo;
import es.fplumara.dam1.prestamos.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


// TODO (alumnos): añadir JUnit 5 y Mockito en el pom.xml y completar: ya esta!

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private Repository<Material> materialRepository;

    @Mock
    private Repository<Prestamo> prestamoRepository;

    private PrestamoService prestamoService;

    @BeforeEach
    void setUp(){
        prestamoService = new PrestamoService(materialRepository, prestamoRepository);
    }

    // - 1. crearPrestamo_ok_cambiaEstado_y_guarda()

    @Test
    void crearPrestamo_pk_cambiaEstado_y_guarda(){
        //ARRANGE (preparacion de enntrono)
        Portatil portatil = new Portatil("M001","Portatil Aula 1", EstadoMaterial.DISPONIBLE, 16);
         when(materialRepository.findById("M001")).thenReturn(Optional.of(portatil));

         //ACT (ejecucion del codigo preparado previamente)
         Prestamo resultado = prestamoService.crearPrestamo("M001", "Profesor García", LocalDate.now());

         //ASSERT (resultados de la interaccion de la prueba del escenario)
        assertNotNull(resultado);
        assertEquals("M001", resultado.getId());
        assertEquals(EstadoMaterial.PRESTADO, portatil.getEstado());

        //VERIFY: verificacion final de las funciones o interacciones, es un "apartado" de mockito.
        verify(materialRepository, times(1)).save(portatil);
        verify(prestamoRepository, times(1)).save(resultado);

    }


    // - crearPrestamo_materialNoExiste_lanzaNoEncontrado()
    // - crearPrestamo_materialNoDisponible_lanzaMaterialNoDisponible()
    // - devolverMaterial_ok_cambiaADisponible()
    //
    // Requisito: usar mocks de repositorios y verify(...)
}
