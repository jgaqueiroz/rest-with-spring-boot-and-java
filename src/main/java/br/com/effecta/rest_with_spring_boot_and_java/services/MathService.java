package br.com.effecta.rest_with_spring_boot_and_java.services;

import org.springframework.stereotype.Service;

import br.com.effecta.rest_with_spring_boot_and_java.exceptions.UnsupportedMathOperationException;
import br.com.effecta.rest_with_spring_boot_and_java.model.utils.NumberUtils;

@Service
public class MathService {

    public Double sum(String numberOne, String numberTwo) {
        validateNumbers(numberOne, numberTwo);
        return NumberUtils.convertToDouble(numberOne) + NumberUtils.convertToDouble(numberTwo);
    }

    public Double subtraction(String numberOne, String numberTwo) {
        validateNumbers(numberOne, numberTwo);
        return NumberUtils.convertToDouble(numberOne) - NumberUtils.convertToDouble(numberTwo);
    }

    public Double multiplication(String numberOne, String numberTwo) {
        validateNumbers(numberOne, numberTwo);
        return NumberUtils.convertToDouble(numberOne) * NumberUtils.convertToDouble(numberTwo);
    }

    public Double division(String numberOne, String numberTwo) {
        validateNumbers(numberOne, numberTwo);
        return NumberUtils.convertToDouble(numberOne) / NumberUtils.convertToDouble(numberTwo);
    }

    public Double mean(String numberOne, String numberTwo) {
        validateNumbers(numberOne, numberTwo);
        return (NumberUtils.convertToDouble(numberOne) + NumberUtils.convertToDouble(numberTwo)) / 2;
    }

    public Double sqrt(String number) {
        validateNumbers(number);
        return Math.sqrt(NumberUtils.convertToDouble(number));
    }

    private void validateNumbers(String... numbers) {
        for (String number : numbers) {
            if (!NumberUtils.isNumeric(number))
                throw new UnsupportedMathOperationException("Please set a numeric value");
        }
    }
}
