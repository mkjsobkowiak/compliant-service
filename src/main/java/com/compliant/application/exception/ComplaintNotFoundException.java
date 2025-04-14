package com.compliant.application.exception;

public class ComplaintNotFoundException extends RuntimeException {
    public ComplaintNotFoundException(Long id) {
        super("Complaint with id " + id + " not found");
    }
}
