/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.dataCoverage;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.TestBundleBuilder;
import br.uece.lotus.tcg.generation.generator.PathGenAllTransitions;
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

    private List<String> resultTab;

    public List<Transition> getTransitions(LtsInfo mLtsInfo) {
        PathGenAllTransitions pathTransitions = new PathGenAllTransitions();
        TestBundle bundle = TestBundleBuilder.build(mLtsInfo, null, null);
        PathSet pathSet = pathTransitions.generate(mLtsInfo, bundle);
        if (pathSet.getPathList() != null) {
            for (List<Transition> path : pathSet.getPathList()) {
                return path;
//                for (Transition t : path) {
//                     System.err.println("Minhas Transiçoes (Action):" + t.getLabel()); 
//                }
            }
        }
        return null;
    }

    public void validateTest(Iterable<Transition> trasitionsList, List<String> columnDataGuardList, List<String> columnDataInputList, List<String> columnDataExpectedValueList) {
        for (int row = 0; row < columnDataGuardList.size(); row++) {
            List<String> coveragedPath = new ArrayList<>();
            boolean noGuard = true;
            boolean testResult = new ComparatorUtils().compare(columnDataGuardList.get(row), columnDataInputList.get(row), columnDataExpectedValueList.get(row));
            for (Transition transition : trasitionsList) {
                System.err.println("-------current transition  action: " + transition.getLabel());
                if (transition.getGuard() != null && transition.getGuard().equals(columnDataGuardList.get(row))) {
                    String resultMsg = (testResult) ? "True" : "False"; 
                    resultTab = Arrays.asList(coveragedPath.toString(), transition.getLabel(), resultMsg);
                    break;
                } else {
                    if (transition.getGuard() == null && noGuard) {
                        coveragedPath.add(transition.getLabel());
                    } else {
                        noGuard = false;
                    }

                }
            }

        }

    }

    public List<String> getResults() {
        System.err.println("Criando result Table");
        return resultTab;
    }

}
