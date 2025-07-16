package br.com.effecta.rest_with_spring_boot_and_java.exceptions;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, int status, String error, String path) {
}
