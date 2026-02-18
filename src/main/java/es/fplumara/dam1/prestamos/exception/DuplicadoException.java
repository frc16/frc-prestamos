package es.fplumara.dam1.prestamos.exception;

public class DuplicadoException extends RuntimeException {
    public DuplicadoException(String message) {
        super(message);
    }
}
