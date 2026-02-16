package es.fplumara.dam1.prestamos.csv;

import es.fplumara.dam1.prestamos.exception.CsvInvalidoException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Lee materiales desde CSV y devuelve una lista de RegistroMaterialCsv.
 * NO crea Material/Portatil/Proyector: eso lo hacen los alumnos (conversión a dominio).
 */
public class CSVMaterialImporter {

    private static final String COL_TIPO = "tipo";
    private static final String COL_ID = "id";
    private static final String COL_NOMBRE = "nombre";
    private static final String COL_ESTADO = "estado";
    private static final String COL_EXTRA = "extra";
    private static final String COL_ETIQUETAS = "etiquetas";

    public List<RegistroMaterialCsv> leer(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            throw new IllegalArgumentException("La ruta no puede ser null/vacía");
        }

        Path path = Path.of(ruta);
        if (!Files.exists(path)) {
            throw new CsvInvalidoException("No existe el archivo: " + ruta);
        }

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .setIgnoreEmptyLines(true)
                .build();

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = format.parse(br)) {

            validarCabecera(parser.getHeaderMap());

            List<RegistroMaterialCsv> out = new ArrayList<>();
            for (CSVRecord r : parser) {
                out.add(parseRecord(r));
            }
            return out;

        } catch (IOException e) {
            throw new CsvInvalidoException("Error leyendo CSV: " + e.getMessage(), e);
        }
    }

    private void validarCabecera(Map<String, Integer> headerMap) {
        if (headerMap == null || headerMap.isEmpty()) {
            throw new CsvInvalidoException("El CSV no tiene cabecera.");
        }
        for (String col : List.of(COL_TIPO, COL_ID, COL_NOMBRE, COL_ESTADO, COL_EXTRA, COL_ETIQUETAS)) {
            if (!headerMap.containsKey(col)) {
                throw new CsvInvalidoException("Falta columna obligatoria en cabecera: " + col);
            }
        }
    }

    private RegistroMaterialCsv parseRecord(CSVRecord r) {
        String tipo = obligatorio(r, COL_TIPO);
        String id = obligatorio(r, COL_ID);
        String nombre = obligatorio(r, COL_NOMBRE);
        String estado = obligatorio(r, COL_ESTADO);

        int extra;
        try {
            extra = Integer.parseInt(obligatorio(r, COL_EXTRA));
        } catch (NumberFormatException ex) {
            throw new CsvInvalidoException("Campo 'extra' inválido en línea " + r.getRecordNumber());
        }

        String etiquetasStr = opcional(r, COL_ETIQUETAS);
        Set<String> etiquetas = parseEtiquetas(etiquetasStr);

        // Validación ligera del tipo (sin depender de enums del dominio)
        if (!"PORTATIL".equals(tipo) && !"PROYECTOR".equals(tipo)) {
            throw new CsvInvalidoException("Tipo inválido '" + tipo + "' en línea " + r.getRecordNumber());
        }

        return new RegistroMaterialCsv(tipo, id, nombre, estado, extra, etiquetas);
    }

    private String obligatorio(CSVRecord r, String col) {
        String v = r.get(col);
        if (v == null || v.trim().isEmpty()) {
            throw new CsvInvalidoException("Campo obligatorio vacío: " + col + " (línea " + r.getRecordNumber() + ")");
        }
        return v.trim();
    }

    private String opcional(CSVRecord r, String col) {
        String v = r.get(col);
        return v == null ? "" : v.trim();
    }

    private Set<String> parseEtiquetas(String etiquetasStr) {
        Set<String> set = new HashSet<>();
        if (etiquetasStr == null || etiquetasStr.isBlank()) return set;

        for (String p : etiquetasStr.split("\\|")) {
            String t = p.trim();
            if (!t.isEmpty()) set.add(t);
        }
        return set;
    }
}
