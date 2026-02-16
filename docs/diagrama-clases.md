# Diagrama de clases (Mermaid)

Este diagrama sirve como referencia para implementar el modelo, repositorios y servicios del examen.
**Debes crear tú las clases/interfaces/enums** en los paquetes indicados.

Paquetes recomendados (según enunciado):
- `es.fplumara.dam1.prestamos.model`
- `es.fplumara.dam1.prestamos.repository`
- `es.fplumara.dam1.prestamos.service`

> Nota: el bloque CSV está proporcionado y no se modifica. Por eso no aparece aquí.

---

## Modelo (POO)

```mermaid
classDiagram
direction TB

class Identificable {
  <<interface>>
  +String getId()
}

class EstadoMaterial {
  <<enumeration>>
  DISPONIBLE
  PRESTADO
  BAJA
}

class Material {
  <<abstract>>
  -String id
  -String nombre
  -EstadoMaterial estado
  -Set~String~ etiquetas
  +String getId()
  +String getNombre()
  +EstadoMaterial getEstado()
  +Set~String~ getEtiquetas()
  +void setEstado(EstadoMaterial e)
  +String getTipo()*
}

class Portatil {
  -int ramGB
  +String getTipo()
}

class Proyector {
  -int lumens
  +String getTipo()
}

class Prestamo {
  -String id
  -String idMaterial
  -String profesor
  -LocalDate fecha
  +String getId()
}

Identificable <|.. Material
Identificable <|.. Prestamo

Material <|-- Portatil
Material <|-- Proyector

Material --> EstadoMaterial
```

---

## Repositorios

```mermaid
classDiagram
direction TB


class Repository~T~ {
  <<interface>>
  +void save(T elemento)
  +Optional~T~ findById(String id)
  +List~T~ listAll()
  +void delete(String id)
}

class BaseRepository~T~ {
  -Map~String,T~ datos
  +void save(T elemento)
  +Optional~T~ findById(String id)
  +List~T~ listAll()
  +void delete(String id)
}

class MaterialRepositoryImpl
class PrestamoRepositoryImpl

Repository~T~ <|.. BaseRepository~T~
BaseRepository~T~ ..> Identificable : T extends

Identificable <|.. Material
Identificable <|.. Prestamo

BaseRepository~Material~ <|-- MaterialRepositoryImpl
BaseRepository~Prestamo~ <|-- PrestamoRepositoryImpl

MaterialRepositoryImpl ..> Material
PrestamoRepositoryImpl ..> Prestamo

    

```

## Servicios (reglas de negocio)

> El diagrama muestra los servicios dependiendo de repositorios.

```mermaid
classDiagram
direction TB

class MaterialService {
  -Repository~Material~ materialRepository
  +void registrarMaterial(Material m)
  +void darDeBaja(String idMaterial)
  +List~Material~ listar()
}

class PrestamoService {
  -Repository~Material~ materialRepository
  -Repository~Prestamo~ prestamoRepository
  +Prestamo crearPrestamo(String idMaterial, String profesor, LocalDate fecha)
  +void devolverMaterial(String idMaterial)
  +List~Prestamo~ listarPrestamos()
}

class Repository~T~ {
  <<interface>>
  +void save(T elemento)
  +Optional~T~ findById(String id)
  +List~T~ listAll()
  +void delete(String id)
}

MaterialService --> Repository~Material~
PrestamoService --> Repository~Material~
PrestamoService --> Repository~Prestamo~
```
