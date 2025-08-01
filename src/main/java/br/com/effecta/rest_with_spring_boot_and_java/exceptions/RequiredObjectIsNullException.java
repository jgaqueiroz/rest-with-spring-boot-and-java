package br.com.effecta.rest_with_spring_boot_and_java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequiredObjectIsNullException extends RuntimeException {

    public RequiredObjectIsNullException() {
        super("It is not allowed to persist a null object");
    }

    public RequiredObjectIsNullException(String message) {
        super(message);
    }

}
