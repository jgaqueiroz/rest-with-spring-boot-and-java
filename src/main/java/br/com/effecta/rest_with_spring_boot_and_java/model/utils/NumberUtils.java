package br.com.effecta.rest_with_spring_boot_and_java.model.utils;

public class NumberUtils {

    public static Double convertToDouble(String strNumber) {
        if (strNumber == null || strNumber.isEmpty())
            return 0D;
        String number = normalize(strNumber);
        return Double.parseDouble(number);
    }

    public static boolean isNumeric(String strNumber) {
        if (strNumber == null || strNumber.isEmpty())
            return false;
        String number = normalize(strNumber);
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static String normalize(String strNumber) {
        return strNumber.replace(",", ".");
    }

}
