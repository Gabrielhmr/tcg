/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.dataCoverage;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public final class ComparatorUtils {
  
    List<String> operators = Arrays.asList("==", "!=", ">", "<", ">=", "<=");

    /**
     *
     * @return
     */
    public boolean compare(String guard, String input, String expectedValue) {
        boolean result = false;
        for (String operator : operators) {
            if (guard.contains(operator)) {
                switch (operator) {
                    case "==":
                        result =  input.equals(expectedValue);
                        break;
                    case "!=":
                        result = !input.equals(expectedValue);
                        break;
                    case ">":
                        result = Integer.parseInt(input) > Integer.parseInt(expectedValue);
                        break;
                    case "<":
                        result = Integer.parseInt(input) < Integer.parseInt(expectedValue);
                        break;
                    case ">=":
                        result = Integer.parseInt(input) >= Integer.parseInt(expectedValue);
                        break;
                    case "<=":
                        result = Integer.parseInt(input) <= Integer.parseInt(expectedValue);
                        break;
                    default:
                        System.out.printf("Você digitou uma operação inválida.");
                }
            }
        }
        return result;
    }

}
