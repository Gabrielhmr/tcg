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

    public List<Transition> getTransitions(LtsInfo mLtsInfo) {
        PathGenAllTransitions pathTransitions = new PathGenAllTransitions();
        TestBundle bundle = TestBundleBuilder.build(mLtsInfo, null, null);
        PathSet pathSet = pathTransitions.generate(mLtsInfo, bundle);
        if (pathSet.getPathList() != null) {
            for (List<Transition> path : pathSet.getPathList()) {
                return path;
            }
        }
        return null;
    }

    public void validateTest(Iterable<Transition> trasitionsList, List<String> columnDataGuardList, List<String> columnDataInputList) {
        List<String> coveragedPath = new ArrayList<>();
        boolean guardTest = true;
        int row = 0;
        for (Transition transition : trasitionsList) {
            System.err.println("---current transition  action: " + transition.getLabel());
            if (transition.getGuard() != null && transition.getGuard().equals(columnDataGuardList.get(row))) {
                System.err.println("------Match in transition:" + transition.getLabel() + " e guarda:" + transition.getGuard() );
                boolean testResult = new ComparatorUtils().compare(columnDataGuardList.get(row), columnDataInputList.get(row));
                String resultMsg = (testResult) ? "True" : "False";
                System.err.println("------HABILITOU, transitions seram adicionadas no coveragePath");
                guardTest = true;
                if (row < columnDataGuardList.size()) {
                    System.err.println("------------- posiciona test para proxima guarda");
                    row++;
                } else {
                    System.err.println("----------------finalizando");
                    resultTab = Arrays.asList(coveragedPath.toString(), transition.getLabel(), resultMsg);
                    finalResultList.add(resultTab);
                    break;
                }
            } else {
                if (transition.getGuard() == null && guardTest) {
                    System.err.println("------adicionada transition no coverage path: " + transition.getLabel());
                    coveragedPath.add(transition.getLabel());
                } else {
                    System.err.println("------DESABILITOU transitions seram adicionadas no coveragePath");
                    guardTest = false;
                }
            }
        }
    }
    
    public void genCoverage(State stateInitial,List<String> columnDataGuardList){
       //List<PathStruct> list = new ArrayList<>();
       PathStruct path = new PathStruct();
       
       State currentState = stateInitial;
       int row = 0;
       for (Transition transition : currentState.getOutgoingTransitionsList()){        
            if (transition.getGuard() != null && transition.getGuard().equals(columnDataGuardList.get(row))){
                if (row < columnDataGuardList.size()) {
                    row++; //se ainda tiver guarda pra ser testada
                    currentState = transition.getDestiny();
                } else {
                    break; //retorna coverage path
                }
            }else{
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
