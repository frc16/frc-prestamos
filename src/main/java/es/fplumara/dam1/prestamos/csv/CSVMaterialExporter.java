package es.fplumara.dam1.prestamos.csv;


import es.fplumara.dam1.prestamos.exception.CsvInvalidoException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Escribe una lista de RegistroMaterialCsv a CSV con el mismo formato del enunciado.
 */
public class CSVMaterialExporter {

    private static final String[] HEADER = {"tipo", "id", "nombre", "estado", "extra", "etiquetas"};

    public void escribir(String ruta, List<RegistroMaterialCsv> registros) {
        if (ruta == null || ruta.isBlank()) {
            throw new IllegalArgumentException("La ruta no puede ser null/vacía");
        }
        if (registros == null) {
            throw new IllegalArgumentException("La lista de registros no puede ser null");
        }

        Path path = Path.of(ruta);

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(HEADER)
                .setTrim(true)
                .build();

        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = format.print(bw)) {

            for (RegistroMaterialCsv r : registros) {
                printer.printRecord(
                        r.tipo(),
                        r.id(),
                        r.nombre(),
                        r.estado(),
                        r.extra(),
                        etiquetasToString(r.etiquetas())
                );
            }

        } catch (IOException e) {
            throw new CsvInvalidoException("Error escribiendo CSV: " + e.getMessage(), e);
        }
    }

    private String etiquetasToString(Set<String> etiquetas) {
        if (etiquetas == null || etiquetas.isEmpty()) return "";
        List<String> ordenadas = new ArrayList<>(etiquetas);
        Collections.sort(ordenadas);
        return String.join("|", ordenadas);
    }
}
