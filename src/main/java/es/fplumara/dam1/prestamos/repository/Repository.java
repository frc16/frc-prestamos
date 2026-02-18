package es.fplumara.dam1.prestamos.repository;

import es.fplumara.dam1.prestamos.model.Identificable;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Identificable> {
    void save (T elemento);

    Optional<T> findById(String id);

    List<T> listAll();

    void delete(String id);
}
