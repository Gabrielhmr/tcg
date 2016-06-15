/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.dataCoverage;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.TestBundleBuilder;
import br.uece.lotus.tcg.generation.generator.PathGenAllTransitions;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
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
        PathGenAllTransitions pathTransitions = new PathGenAllTransitions();
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
            System.err.println("Numero de possiveis caminhos: " + resultPathList.size());
            List<String> coveragedPath = new ArrayList<>();
            int row = 0;
            for (Transition transition : transitionList) {
                System.err.println("------adicionada current transition no coverage path: " + transition.getLabel());
                coveragedPath.add(transition.getLabel());
                if (transition.getGuard() != null && transition.getGuard().equals(columnDataGuardList.get(row))) {
                    System.err.println("------Match in transition:" + transition.getLabel() + " e guarda:" + transition.getGuard());
                    boolean testResult = new ComparatorUtils().compare(columnDataGuardList.get(row), columnDataInputList.get(row));
                    if (testResult == false) {
                        System.err.println("------------- input diferente");
                        resultTab = Arrays.asList(coveragedPath.toString(), "False");
                        finalResultList.add(resultTab);
                        break;
                    } else if (row < columnDataGuardList.size() - 1) {
                        System.err.println("------------- posiciona test para proxima guarda");
                        row++;
                    } else {
                        System.err.println("----------------finalizando");
                        resultTab = Arrays.asList(transitionList.toString(), "True");
                        finalResultList.add(resultTab);
                        break;
                    }
                }             
            }
        }
    }

    public void genCoverage(State stateInitial, List<String> columnDataGuardList) {
        //List<PathStruct> list = new ArrayList<>();
        PathStruct path = new PathStruct();

        State currentState = stateInitial;
        int row = 0;
        for (Transition transition : currentState.getOutgoingTransitionsList()) {
            if (transition.getGuard() != null && transition.getGuard().equals(columnDataGuardList.get(row))) {
                if (row < columnDataGuardList.size() - 1) {
                    row++; //se ainda tiver guarda pra ser testada
                    currentState = transition.getDestiny();
                } else {
                    break; //retorna coverage path
                }
            } else {
                //add currente transition to coverage path
                currentState = transition.getDestiny();
            }

        }

    }

    public List<List<String>> getResults() {
        System.err.println("Criando result Table");
        return finalResultList;
    }

}
