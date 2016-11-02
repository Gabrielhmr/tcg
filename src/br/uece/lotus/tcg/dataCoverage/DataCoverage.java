/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.dataCoverage;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.TestBundleBuilder;
import br.uece.lotus.tcg.generation.generator.OneLoopPath;
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

    public List<List<Transition>> getPathList(Component component, String testedTransition) {
        OneLoopPath olp = new OneLoopPath();
        List<List<Transition>> AllPathList = olp.createOneLoopPath(component);
        System.err.println("all transition size: " + AllPathList.size());
        List<List<Transition>> resultsList = new ArrayList<>();
        if (AllPathList != null) {
            for (List<Transition> path : AllPathList) {
                for (Transition transition : path) {
                    if (transition.getLabel().equals(testedTransition)) {
                        resultsList.add(path);
                        break;
                    }
                }
            }
            System.err.println("all result size:"+resultsList.size());
            return resultsList;
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
                          testResult = true;
//                        int row = columnDataGuardList.indexOf(transition.getGuard());
//                        testResult = new ComparatorUtils().compare(columnDataGuardList.get(row), columnDataInputList.get(row));
//                        if(!testResult) {
//                           System.err.println("---input diferente on trasition: " + transition.getLabel()); 
//                           break;
//                        }
                    }
                } else {
                    System.err.println("---transition com guarda nao testada no submit... " + transition.getLabel());
                    //diffGuard = true;
                    testResult = false;
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
        if(finalResultList.isEmpty()){
            finalResultList.add(Arrays.asList("Nenhum caminho possivel", "False"));
        }
        return finalResultList;
    }
}
