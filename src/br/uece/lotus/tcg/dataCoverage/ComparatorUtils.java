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
    public boolean compare(String guard, String input) {
        String expectedValue = getGuardValue(guard);
        boolean result = false;
        for (String operator : operators) {
            if (guard.contains(operator)) {
                switch (operator) {
                    case "==":
                        result = input.equals(expectedValue);
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

    public String getGuardName(String guard) {
        String result = "Você digitou uma operação inválida.";
        for (String operator : operators) {
            if (guard.contains(operator)) {
                String[] guardName = guard.split(operator);
                result = guardName[0];
                break;
            }
        }
        return result;
    }

    private String getGuardValue(String guard) {
        String result = "Você digitou uma operação inválida.";
        for (String operator : operators) {
            if (guard.contains(operator)) {
                String[] guardName = guard.split(operator);
                result = guardName[1];
                break;
            }
        }
        return result;
    }

    public String getGuardByName(String guardName, String value, List<String> guardList) {
        for (String guard : guardList) {
            if (getGuardName(guard).equals(guardName)) {
                if(compare(guard, value)){
                    System.err.println("****Guarda escolhida: " + guard);
                    return guard;
                }
            }
        }
        return null;
    }
}
