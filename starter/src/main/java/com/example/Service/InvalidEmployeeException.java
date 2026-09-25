package com.example.Service;

public class InvalidEmployeeException extends RuntimeException{
    public InvalidEmployeeException(String message){
        super(message);
    }
}
