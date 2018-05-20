package io.github.artur_zaripov.simplecrudapp.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException() {
    }

    public RecordNotFoundException(Integer recordId) {
        super("No record with id = " + recordId);
    }
}
