package com.tecsup.petclinic.exceptions;

/**
 * Excepción lanzada cuando un Vet no es encontrado.
 */
public class VetNotFoundException extends Exception {
    
    public VetNotFoundException(String message) {
        super(message);
    }
}