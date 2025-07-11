package com.company.projectjwt1.exceptions;

public class LoginCredentialException extends RuntimeException {
    public LoginCredentialException(String exception){
        super(exception);
    }
}
