package com.example.productcatalog.exception;

/**
 * Thrown when a product ID cannot be converted to the active data source's ID type
 * (UUID for the database, Long for FakeStore).
 */
public class InvalidProductIdException extends IllegalArgumentException {

    public InvalidProductIdException(String id) {
        super("Invalid product ID: " + id);
    }
}
