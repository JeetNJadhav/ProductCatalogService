package com.example.productcatalog.exception;

/**
 * Thrown when the active product source (e.g. a third-party API) fails or cannot be reached.
 * Deliberately source-agnostic so the web layer does not depend on any integration package.
 */
public class ProductSourceUnavailableException extends RuntimeException {

    public ProductSourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
