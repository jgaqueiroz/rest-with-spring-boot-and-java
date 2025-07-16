package br.com.effecta.rest_with_spring_boot_and_java.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.effecta.rest_with_spring_boot_and_java.services.MathService;

// throw new UnsupportedMathOperationException("Please set a numeric value");
@RestController
@RequestMapping("/math")
public class MathController {

    private final MathService service;

    public MathController(MathService service) {
        this.service = service;
    }

    @RequestMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo) {
        return service.sum(numberOne, numberTwo);
    }

    @RequestMapping("/subtraction/{numberOne}/{numberTwo}")
    public Double subtraction(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo) {
        return service.subtraction(numberOne, numberTwo);
    }

    @RequestMapping("/multiplication/{numberOne}/{numberTwo}")
    public Double multiplication(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo) {
        return service.multiplication(numberOne, numberTwo);
    }

    @RequestMapping("/division/{numberOne}/{numberTwo}")
    public Double division(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo) {
        return service.division(numberOne, numberTwo);
    }

    @RequestMapping("/mean/{numberOne}/{numberTwo}")
    public Double mean(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo) {
        return service.mean(numberOne, numberTwo);
    }

    @RequestMapping("/sqrt/{number}")
    public Double sqrt(@PathVariable("number") String number) {
        return service.sqrt(number);
    }

}
