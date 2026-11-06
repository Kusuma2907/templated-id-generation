package com.example.id_generator.exception;

public class UnknownCityException
        extends RuntimeException {

    public UnknownCityException(String message) {
        super(message);
    }
}