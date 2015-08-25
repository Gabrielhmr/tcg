package br.uece.lotus.tcg.purpose;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.utils.DebugLog;
import br.uece.lotus.tcg.utils.StateUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 1. a*b*&ACCEPT
 2. a,b*!c*d&ACCEPT
 3. *a*&ACCEPT
 */
public class TestPurposeParser{

    static public TestPurposeSuite parse(LtsInfo lts, String purpose) throws ParseException{
        
        if (purpose == null || "".equals(purpose)){
            return null;
        }

        TestPurposeSuite suite = new TestPurposeSuite();

        String[] strArr = purpose.split("&");

        if (strArr.length != 2){
            throw new ParseException(
                    "The purpose shall have this format: configuration&direction (Ex: a*&ACCEPT)",
                    0);
        }

        String direction = strArr[1];

        if (!direction.equals("ACCEPT") && !direction.equals("REJECT")){
            throw new ParseException(
                    "The direction shall be: 'ACCEPT' or 'REJECT'",
                    0);
        }

        suite.setAcceptPaths(direction.equals("ACCEPT"));

        String configuration = adjustConfiguration(strArr[0]);

        if ("*".equals(configuration)){
            return null;
        }

        String transitionsStr[];
        transitionsStr = configuration.split("\\*");

        boolean middle = false;
        boolean end = false;

        for (int i = 0; i <transitionsStr.length; ++i){
            
            String current = transitionsStr[i];

            if (current.equals("$")){
                
                if (middle){
                    end = true;
                } else{
                    middle = true;
                }
                continue;
            }

            if (i == 0){
                if (current.startsWith("$")){
                    current = current.substring(1);
                }
            } else if (i == transitionsStr.length - 1){
                if (current.endsWith("$")){
                    end = true;
                    current = current.substring(0, current.length() - 1);
                }
            }

            current = current.toLowerCase();

            String[] transitions = current.split(",");
            List<String> labelList = new ArrayList<>();

            for (String t : transitions){
                
                String label = t;

                if (label.startsWith("!")){
                    label = label.substring(1);
                }

                if (lts.getTransitionsByLabel(label) == null){
                    throw new ParseException("The LTS does not have the transition labeled '" + label + "'", 0);
                }
                labelList.add(t);
            }

            if (end){
                suite.addFinalTransitionLabel(labelList);
            } else if (middle){
                suite.addMiddleTransitionLabel(labelList);
            } else{
                suite.addBeginTransitionsLabel(labelList);
                middle = true;
            }
        }

        DebugLog.printLog("PurposeParser - Begin: " + suite.getPathRepresentation(suite.getBeginLabels()));
        DebugLog.printLog("PurposeParser - Middle size: " + suite.getMiddleLabels().size());
        DebugLog.printLog("PurposeParser - End: " + suite.getPathRepresentation(suite.getFinalLabels()));

        return build(lts, suite);
    }

    static private TestPurposeSuite build(LtsInfo lts, TestPurposeSuite suite){
        Set<Transition> initialTransitions = new HashSet<>();
        Set<Transition> additionalFinalTransitions = new HashSet<>();

        if (suite.acceptPaths()){
            
            String label = suite.getFirstTransitionLabel();

            if (label != null && !label.startsWith("!")){
                initialTransitions.addAll(lts.getTransitionsByLabel(label));
            } else{
                initialTransitions.addAll(StateUtils.getOutgoingTransitions(lts.getRootState()));
            }

            label = suite.getLastTransitionLabel();

            if (label != null && !label.startsWith("!")){
                additionalFinalTransitions.addAll(lts.getTransitionsByLabel(label));
            }
            
        } else{
            initialTransitions.addAll(StateUtils.getOutgoingTransitions(lts.getRootState()));
        }

        suite.setInitialTransitions(initialTransitions);
        suite.setAdditionalFinalTransitions(additionalFinalTransitions);

        return suite;
    }

    static private String adjustConfiguration(String configuration){
        
        while (configuration.contains("**")){ // Eliminate extra asterisks
            configuration = configuration.replace("**", "*");
        }

        while (configuration.contains(",,")){ // Eliminate extra commas
            configuration = configuration.replace(",,", ",");
        }

        return "$" + configuration + "$";
    }
}
