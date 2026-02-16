package es.fplumara.dam1.prestamos.exception;

public class CsvInvalidoException extends RuntimeException {

    private Exception parentException;
    public CsvInvalidoException(String message){
        super(message);
    }
    public CsvInvalidoException(String message, Exception e) {
        super(message);
        parentException = e;
    }
}
