package com.bank_system_project.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such userDetails")
public class UserDetailsNotFoundException extends RuntimeException{
    public UserDetailsNotFoundException(){
        super(String.format("Użytkownik nie istnieje"));
    }

    public UserDetailsNotFoundException(Long id){
        super(String.format("Użytkownik o id #d nie istnieje", id));
    }
}
