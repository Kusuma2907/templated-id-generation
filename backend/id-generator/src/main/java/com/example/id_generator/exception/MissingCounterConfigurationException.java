package com.example.id_generator.exception;

public class MissingCounterConfigurationException
        extends RuntimeException {

    public MissingCounterConfigurationException(
            String message) {

        super(message);
    }
}