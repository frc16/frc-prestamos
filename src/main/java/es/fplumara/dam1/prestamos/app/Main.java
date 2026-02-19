package es.fplumara.dam1.prestamos.app;

import es.fplumara.dam1.prestamos.csv.CSVMaterialExporter;
import es.fplumara.dam1.prestamos.csv.CSVMaterialImporter;
import es.fplumara.dam1.prestamos.csv.RegistroMaterialCsv;
import es.fplumara.dam1.prestamos.model.*;
import es.fplumara.dam1.prestamos.repository.MaterialRepositoryImpl;
import es.fplumara.dam1.prestamos.repository.PrestamoRepositoryImpl;
import es.fplumara.dam1.prestamos.service.MaterialService;
import es.fplumara.dam1.prestamos.service.PrestamoService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Main de ejemplo para demostrar el flujo mínimo del examen (sin menú complejo).
 * La idea es que este método ejecute una "demo" por consola.
 */
public class Main {


    public static void main(String[] args) {
        System.out.println("Examen DAM1 - Préstamo de Material (Java 21)");

        /*
         * FLUJO MÍNIMO OBLIGATORIO (lo que debe hacer tu main)
         *
         * 1) Crear repositorios en memoria
         *    - Crear MaterialRepositoryImpl (almacena materiales en memoria).
         *    - Crear PrestamoRepositoryImpl (almacena préstamos en memoria).
         */
        MaterialRepositoryImpl materialRepository = new MaterialRepositoryImpl();
        PrestamoRepositoryImpl prestamoRepository = new PrestamoRepositoryImpl();

         /* 2) Crear servicios
         *    - Crear MaterialService usando el repositorio de materiales.
         *    - Crear PrestamoService usando el repositorio de materiales y el de préstamos.
         */
        MaterialService materialService = new MaterialService(materialRepository);
        PrestamoService prestamoService = new PrestamoService(materialRepository,prestamoRepository);

        /* 3) Cargar materiales desde CSV (código proporcionado)
         *    - Usar CsvMaterialImporter para leer "materiales.csv".
         *    - El importer devuelve registros (por ejemplo RegistroMaterialCsv).
         *    - Convertir cada registro a tu modelo:
         *        - Si tipo == "PORTATIL" -> crear Portatil (extra = ramGB)
         *        - Si tipo == "PROYECTOR" -> crear Proyector (extra = lumens)
         *      (aplicando estado y etiquetas)
         *    - Registrar cada Material llamando a MaterialService.registrarMaterial(...)
         */
          CSVMaterialImporter importer = new CSVMaterialImporter();
          List<RegistroMaterialCsv> registros = importer.leer("data/materiales.csv");

          for(RegistroMaterialCsv r : registros){
              Material material;

              if("PORTATIL".equals(r.tipo())){
                  material = new Portatil(r.id(), r.nombre(),
                  EstadoMaterial.valueOf(r.estado()),r.extra());
              } else{
                  material = new Proyector(r.id(), r.nombre(),
                          EstadoMaterial.valueOf(r.estado()), r.extra());
              }
          }
         /* 4) Crear un préstamo
         *    - Elegir un id de material existente (por ejemplo "M001").
         *    - Llamar a PrestamoService.crearPrestamo("M001", "Nombre Profesor", fecha)
         *    - Comprobar que el material pasa a estado PRESTADO
         */
        System.out.println("\n--- préstamo creado para M001 ---");
        prestamoService.crearPrestamo("M001", "nombre profesor", LocalDate.now());
        System.out.println("prestamo creado. material 001 en estado: "
                + materialRepository.findById("M001").get().getEstado());

         /* 5) Listar por consola
         *    - Imprimir todos los materiales (MaterialService.listar()) mostrando: id, nombre, estado, tipo.
         *    - Imprimir todos los préstamos (PrestamoService.listarPrestamos()) mostrando: id, idMaterial, profesor, fecha.
         */
        System.out.println("\n--- lista de materiales: ---");
        for(Material m : materialService.listar()){
            System.out.println("id = " + m.getId()
                + ", nombre = " + m.getNombre()
                + ", estado = " + m.getEstado()
                + ", tipo = " + m.getTipo());
        }
        System.out.println("\n--- lista de prestamos ---");
        prestamoService.listarPrestamos().forEach(p ->
                System.out.println("id = "+ p.getId()
                + ", idMaterial = " + p.getIdMaterial()
                + ", profesor = " + p.getProfesor()
                + ", fecha = " + p.getFecha()));

         /* 6) Devolver el material
         *    - Llamar a PrestamoService.devolverMaterial("M001")
         *    - Comprobar que vuelve a estado DISPONIBLE
         */
        System.out.println("\n--- lista de material devuelto ---");
        prestamoService.devolverMaterial("M001");
        System.out.println("material M001 devuelto. Estado: "
        + materialRepository.findById("M001").get().getEstado());


         /* 7) Exportar a CSV (código proporcionado)
         *    - Convertir tu lista de Material a la estructura que pida el exporter (por ejemplo RegistroMaterialCsv).
         *    - Usar CsvMaterialExporter para escribir "salida_materiales.csv".
         */
        System.out.println("\n--- Exportando materiales a CSV ---");
        List<RegistroMaterialCsv> registrosExport = new ArrayList<>();
        for (Material m : materialService.listar()) {
            int extra = (m instanceof Portatil p) ? p.getRamGB()
                    : (m instanceof Proyector pr) ? pr.getLumens() : 0;
            registrosExport.add(new RegistroMaterialCsv(
                    m.getTipo(), m.getId(), m.getNombre(),
                    m.getEstado().name(), extra, m.getEtiquetas()
            ));
        }

        CSVMaterialExporter exporter = new CSVMaterialExporter();
        exporter.escribir("data/salida_materiales.csv", registrosExport);
        System.out.println("CSV exportado a data/salida_materiales.csv");
    }

         /* Nota:
         * - No hace falta interfaz, ni menú, ni pedir datos por teclado: valores fijos y salida por consola es suficiente.
         */
    }
