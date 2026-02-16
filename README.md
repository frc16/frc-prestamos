# Examen práctico DAM1 — Programación + Entornos de Desarrollo

## Contexto
En el centro hay un pequeño “almacén” de material (portátiles, proyectores, etc.) que se presta a profesores. Se pide una aplicación Java **de consola** para registrar material, gestionar préstamos y trabajar con un CSV de materiales.

> Importante: este repositorio **no trae implementaciones** de modelo/repos/servicios. Debes crearlas tú siguiendo el diagrama de clases y los requisitos.

---

# 1) Requisitos de Entornos de Desarrollo (ED)

## 1.1 GitHub (Fork + Project + Issues + PR)
1. Haz **FORK** de este repositorio a tu cuenta.
2. Crea un **GitHub Project** (tablero) en tu fork con columnas:
   - Pendiente (To do)
   - En proceso (In progress)
   - En revisión (In review)
   - Hecho (Done)
3. Crea al menos **3 Issues** (tareas) y colócalos en el Project. Ejemplos:
   - Modelo (POO)
   - Repositorios genéricos
   - Servicios + reglas
   - Excepciones
   - Maven (dependencias + plugin exec)
   - Tests (JUnit + Mockito)
   - Integración CSV
4. Debes trabajar con ramas `feature/*`.
5. **Cualquier cambio que quieras integrar en `main` debe entrar mediante una Pull Request (PR)** (no se permite merge directo).
6. **Cada PR debe quedar aprobada por ti** (self-review) antes de hacer el merge.
7. Cada PR debe:
   - tener descripción breve de lo hecho
   - enlazar un Issue (por ejemplo: `Closes #3`)

---

# 2) Requisitos de Maven (ED) — obligatorio

## 2.1 Java
El proyecto debe compilar con **Java 21**.

## 2.2 Dependencias obligatorias (añadir en `pom.xml`)
Debes añadir exactamente estas dependencias y versiones:

- **Apache Commons CSV**
  - groupId: `org.apache.commons`
  - artifactId: `commons-csv`
  - version: `1.14.1`

- **JUnit Jupiter (JUnit 5)**
  - groupId: `org.junit.jupiter`
  - artifactId: `junit-jupiter`
  - version: `5.14.1`
  - scope: `test`

- **Mockito JUnit Jupiter**
  - groupId: `org.mockito`
  - artifactId: `mockito-junit-jupiter`
  - version: `5.21.0`
  - scope: `test`

## 2.3 Plugins
- El plugin **`maven-surefire-plugin`** (tests) **ya está configurado** en este repositorio y **no tienes que tocarlo**.

- Debes configurar el plugin **`exec-maven-plugin`** para poder ejecutar con `mvn exec:java`:

  - groupId: `org.codehaus.mojo`
  - artifactId: `exec-maven-plugin`
  - version: `3.6.3`
  - configuración obligatoria:
    - `<mainClass>es.fplumara.dam1.prestamos.app.Main</mainClass>`

✅ Debe poder ejecutarse:
- `mvn clean test`
- `mvn exec:java`

---

# 3) Requisitos de Programación

## 3.1 Capas y paquetes
Debes organizar el código por capas usando estos paquetes:

- `es.fplumara.dam1.prestamos.app`
- `es.fplumara.dam1.prestamos.model`
- `es.fplumara.dam1.prestamos.repository`
- `es.fplumara.dam1.prestamos.service`
- `es.fplumara.dam1.prestamos.csv`
- `es.fplumara.dam1.prestamos.exception`

No se permite “todo en Main”.

---

## 3.2 Modelo
Debes crear las clases/interfaces/enums indicadas en el diagrama de `docs/diagrama-clases.md`.

Requisitos mínimos:
- Una **interface** `Identificable` con `String getId()`.
- Un **enum** `EstadoMaterial`: `DISPONIBLE`, `PRESTADO`, `BAJA`.
- Una clase **abstracta** `Material` (con `getTipo()` abstracto).
- Clases hijas:
  - `Portatil` (incluye `ramGB`)
  - `Proyector` (incluye `lumens`)
- `Material` debe tener un `Set<String>` de `etiquetas` (sin repetidos).
- Clase `Prestamo` con:
  - `id`, `idMaterial`, `profesor`, `fecha (LocalDate)`


*(El diagrama completo está en `docs/diagrama-clases.md`.)*

---

## 3.3 Repositorios
Crea un repositorio genérico:

- `Repository<T extends Identificable>` con:
  - `save(T)`
  - `findById(String)` → `Optional<T>`
  - `listAll()` → `List<T>`
  - `delete(String)`

Implementación:
- `BaseRepository<T extends Identificable>`
  - debe usar internamente un `Map<String, T>`
- 	`MaterialRepositoryImpl extends BaseRepository<Material>`
- 	`PrestamoRepositoryImpl extends BaseRepository<Prestamo>`

---

## 3.4 Servicios (reglas de negocio)

> En todos los métodos, cuando se indique “→ excepción”, debes lanzar **una de las excepciones propias** del apartado 3.5 (no vale `RuntimeException` genérica).

### Servicio de material
Debes crear `MaterialService` con, al menos:

- `registrarMaterial(Material m)`
  - Si ya existe un material con el mismo `id` → **`DuplicadoException`**
  - Si `m` es `null` o `id` es `null`/vacío → **`IllegalArgumentException`**

- `darDeBaja(String idMaterial)`
  - Si no existe el material → **`NoEncontradoException`**
  - Si ya está en `BAJA` → **`MaterialNoDisponibleException`**
  - Si existe y no está en BAJA: cambia estado a `BAJA` y guarda el cambio

- `listar()`
  - Devuelve `List<Material>` con todos los materiales

### Servicio de préstamos
Debes crear `PrestamoService` con, al menos:

- `crearPrestamo(String idMaterial, String profesor, LocalDate fecha)`
  - Si algún parámetro es `null`/vacío (o fecha `null`) → **`IllegalArgumentException`**
  - Si no existe material con ese id → **`NoEncontradoException`**
  - Si existe pero su estado no es `DISPONIBLE` → **`MaterialNoDisponibleException`**
  - Si OK:
    - crea un `Prestamo` (id por ejemplo con `UUID`)
    - guarda el préstamo con `PrestamoRepository`
    - cambia el estado del material a `PRESTADO` y guarda el material actualizado

- `devolverMaterial(String idMaterial)`
  - Si `idMaterial` es `null`/vacío → **`IllegalArgumentException`**
  - Si no existe material → **`NoEncontradoException`**
  - Si existe pero su estado no es `PRESTADO` → **`MaterialNoDisponibleException`**
  - Si OK: cambia estado a `DISPONIBLE` y guarda el material actualizado

- `listarPrestamos()`
  - Devuelve `List<Prestamo>` con todos los préstamos

---

## 3.5 Excepciones propias
Crea y usa estas excepciones (paquete `...exception`):
- `DuplicadoException`
- `NoEncontradoException`
- `MaterialNoDisponibleException`
- `CsvInvalidoException`

---

# 4) CSV (Apache Commons CSV) — **código proporcionado (no se modifica)**

## 4.1 Archivo CSV
Existe un archivo de ejemplo `data/materiales.csv` con formato:

```csv
tipo,id,nombre,estado,extra,etiquetas
PORTATIL,M001,Portátil Aula 1,DISPONIBLE,16,ofimatica|aula1
PROYECTOR,M010,Proyector Epson,DISPONIBLE,3200,video|salon
```

## 4.2 Código CSV proporcionado (no editar)
En el repositorio se proporcionan clases de utilidad en el paquete:
- `es.fplumara.dam1.prestamos.csv`

Por ejemplo:
- `CsvMaterialImporter`
- `CsvMaterialExporter`
- `RegistroMaterialCsv` (record/DTO)

**Norma:** estas clases están para **usarlas**, no para reescribir el parser.
- No debes modificar su lógica interna.
- Sí debes **integrarlas** en tu aplicación y entender qué devuelven.

---

# 5) Tests unitarios (JUnit + Mockito) — obligatorio

Crea tests para `PrestamoService` usando **Mockito** con repositorios mockeados.

Mínimo 4 tests:
1. `crearPrestamo_ok_cambiaEstado_y_guarda`
2. `crearPrestamo_materialNoExiste_lanzaNoEncontrado`
3. `crearPrestamo_materialNoDisponible_lanzaMaterialNoDisponible`
4. `devolverMaterial_ok_cambiaADisponible`

Requisitos:
- usar `@ExtendWith(MockitoExtension.class)`
- mocks de repositorio(s)
- verificar interacciones con `verify(...)` al menos en el caso OK
---

# 6) Programa principal (mínimo)
En `app.Main` demuestra un flujo simple (sin menú complejo):
1. Importar materiales desde `data/materiales.csv` (importer proporcionado)
2. Convertir/registrar materiales en el repositorio (usando `MaterialService`)
3. Crear un préstamo
4. Listar materiales y préstamos por consola
5. Devolver material
6. Exportar materiales a `data/salida_materiales.csv` (exporter proporcionado)

---

# 7) Criterios de evaluación 

## ED 
- Fork + Project + Issues bien usados 
- Ramas feature/* y trazabilidad con PRs
- Manejo y configuración de Maven
- Tests JUnit+Mockito con verify

## Programación
- Modelo POO correcto (interface/abstract/enum/herencia/polimorfismo)
- Repositorios + Map + Optional + List/Set
- Servicios con reglas + excepciones bien aplicadas 
- Creación y manejo de excepciones propias
- CSV integrado (lectura + conversión + exportación)

