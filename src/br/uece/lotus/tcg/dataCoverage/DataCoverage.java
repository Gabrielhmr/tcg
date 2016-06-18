/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.dataCoverage;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.TestBundleBuilder;
import br.uece.lotus.tcg.generation.generator.PathGenAllOneLoop;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.TestBundle;
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

    public List<List<Transition>> getPathList(LtsInfo mLtsInfo, String testedTransition) {
        List<List<Transition>> resultsPathList = new ArrayList<>();
        PathGenAllOneLoop pathTransitions = new PathGenAllOneLoop();
        TestBundle bundle = TestBundleBuilder.build(mLtsInfo, null, null);
        PathSet pathSet = pathTransitions.generate(mLtsInfo, bundle);
        if (pathSet.getPathList() != null) {
            for (List<Transition> path : pathSet.getPathList()) {
                for (Transition transition : path) {
                    if (transition.getLabel().equals(testedTransition)) {
                        resultsPathList.add(path);
                    }
                }
            }
            return resultsPathList;
        }
        return null;
    }

    public void validateTest(List<List<Transition>> resultPathList, List<String> columnDataGuardList, List<String> columnDataInputList) {
        for (List<Transition> transitionList : resultPathList) {
            List<String> coveragedPath = new ArrayList<>();
            boolean diffGuard = false;
            boolean testResult = true;    
            for (Transition transition : transitionList) {
                if (transition.getGuard() == null || columnDataGuardList.contains(transition.getGuard())) {
                    coveragedPath.add(transition.getLabel());
                    if (columnDataGuardList.contains(transition.getGuard())) {
                        int row = columnDataGuardList.indexOf(transition.getGuard());
                        testResult = new ComparatorUtils().compare(columnDataGuardList.get(row), columnDataInputList.get(row));
                        if(!testResult) {
                           System.err.println("---input diferente on trasition: " + transition.getLabel()); 
                           break;
                        }
                    }
                } else {
                    System.err.println("---transition com guarda nao testada no submit... " + transition.getLabel());
                    diffGuard = true;
                    break;
                }
            }
            if (diffGuard == false) {
                System.err.println("---lista adicionada");
                String result = (testResult) ? "True" : "False"; 
                resultTab = Arrays.asList(coveragedPath.toString(), result);
                finalResultList.add(resultTab);
            }
        }
    }

    public List<List<String>> getResults() {
        System.err.println("Criando result Table");
        return finalResultList;
    }
}
