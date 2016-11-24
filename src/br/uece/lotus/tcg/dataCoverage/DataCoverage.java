/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.dataCoverage;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.generation.generator.OneLoopPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class DataCoverage {

    List<List<String>> finalResultList = new ArrayList<>();
    private List<String> resultTab;
    private String expectedOutput;

    public List<List<Transition>> getPathList(Component component, String testedTransition) {
        expectedOutput = testedTransition;
        OneLoopPath olp = new OneLoopPath();
        List<List<Transition>> AllPathList = olp.createOneLoopPath(component);
        System.err.println("all transition size: " + AllPathList.size());
        List<List<Transition>> resultsList = new ArrayList<>();
        if (AllPathList != null) {
            for (List<Transition> path : AllPathList) {
                for (Transition transition : path) {
                    if (new ComparatorUtils().isExpectedOutput(transition,expectedOutput)) {
                        resultsList.add(path);
                        break;
                    }
                }
            }
            System.err.println("all result size:" + resultsList.size());
            return resultsList;
        }
        return null;
    }

    public void validateTest(List<List<Transition>> resultPathList, List<String> columnDataGuardList) {
        for (List<Transition> transitionList : resultPathList) {
            List<String> coveragedPath = new ArrayList<>();
            boolean testResult = true;
            System.err.println("expectedOutput: " + expectedOutput);

            for (Transition transition : transitionList) {
                if (!new ComparatorUtils().isExpectedOutput(transition,expectedOutput)) {
                    if (transition.getGuard() == null || columnDataGuardList.contains(transition.getGuard())) {
                        coveragedPath.add(transition.getLabel());
                    } else {
                        System.err.println("---transition com guarda nao testada no submit... " + transition.getLabel());
                        testResult = false;
                        break;
                    }
                } else {
                    coveragedPath.add(transition.getLabel());
                    break;
                }

            }
            if (testResult == true) {
                System.err.println("---lista adicionada");
                resultTab = Arrays.asList(coveragedPath.toString(), "True");
                finalResultList.add(resultTab);
            }
        }
    }

    public List<List<String>> getResults() {
        System.err.println("Criando result Table");
        if (finalResultList.isEmpty()) {
            finalResultList.add(Arrays.asList("Nenhum caminho possivel", "False"));
        }
        return finalResultList;
    }
}
