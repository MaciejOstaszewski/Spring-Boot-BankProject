package com.bank_system_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such template")
public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(){
        super(String.format("Szablon nie istnieje"));
    }

    public TemplateNotFoundException(Long id){
        super(String.format("Szablon o id #d nie istnieje", id));
    }
}
