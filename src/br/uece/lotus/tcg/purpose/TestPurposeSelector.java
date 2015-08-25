package br.uece.lotus.tcg.purpose;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.utils.DebugLog;
import java.util.ArrayList;
import java.util.List;

public class TestPurposeSelector{

    static public PathSet select(PathSet pathSet, TestPurposeSuite suite){
        
        PathSet filteredSet = new PathSet();

        String initialPattern = "";
        String middleStr = "(.*)";
        String finalPattern = "";

        List<String> middlePatterns = new ArrayList<>();

        // Mount pattern to match with the beginning of path
        initialPattern = mountPattern(suite.getBeginLabels());
        finalPattern = mountPattern(suite.getFinalLabels());
        middlePatterns = mountMiddlePatterns(suite.getMiddleLabels());

        DebugLog.printLog("Purpose Selector - Initial Pattern: " + initialPattern);
        DebugLog.printLog("Purpose Selector - Final Pattern: " + finalPattern);
        DebugLog.printLog("Purpose Selector - Middle Patterns: " + middlePatterns.toString());

        String pattern = "^" + initialPattern + middleStr + finalPattern + "$";
        //DebugLog.printLog("Purpose Selector - Main Pattern: " + pattern);

        for (List<Transition> path : pathSet.getPathList()){
            
            boolean valid = true;
            String pathStr = getPathString(path);

            DebugLog.printLog("Purpose Selector - pathSet: " + pathStr);

            if (suite.acceptPaths()){
                
                valid = matchMiddlePatterns(pathStr, middlePatterns);

                if (valid && pathStr.matches(pattern)){
                    DebugLog.printLog("PurposeSelector", "Path was added!");
                    filteredSet.addPath(new PathStruct(path));
                }                
            } else{ // Reject paths
                
                valid = matchMiddlePatterns(pathStr, middlePatterns);

                if (!valid || !pathStr.matches(pattern)){
                    DebugLog.printLog("PurposeSelector", "Path was added!");

                    filteredSet.addPath(new PathStruct(path));
                }
            }
        }
        return filteredSet;
    }

    static private boolean matchMiddlePatterns(String pathStr, List<String> midPatterns){
        
        for (String midPattern : midPatterns){
            
            System.out.println("Mid Pattern: " + midPattern);
            System.out.println("Path Pattern: " + pathStr);
            
            if (!pathStr.matches(midPattern)){
                System.out.println("Path Pattern: " + pathStr);
                return false;
            }            
        }
        return true;
    }

    static private String mountPattern(List<String> labels){
        
        String aux = "";
        String ret = "";

        for (String transitionLabel : labels){
            
            aux = "";

            if (transitionLabel.startsWith("!")){ // Reject
                aux = "?!";
                transitionLabel = transitionLabel.substring(1);
            }
            ret += aux + "@" + transitionLabel;
        }

        if (!ret.isEmpty()){
            ret = "(" + ret + "@)";
        }

        return ret;
    }

    static private List<String> mountMiddlePatterns(List<List<String>> labelsList){
        
        String str = "";
        String aux = "";

        List<String> ret = new ArrayList<>();

        for (List<String> list : labelsList){
            
            str = "";

            for (String label : list){
                
                if (label.startsWith("!")){ // Reject                    
                    aux = "?!";
                    label = label.substring(1);
                    str += "@(?!" + label + ")";
                } else{
                    str += "@" + label;
                }
            }

            str = "^(.*(" + str + "@).*)*$";

            ret.add(str);
        }

        return ret;
    }

    static public String getPathString(List<Transition> path){
        
        String ret = "@";

        for (Transition t : path){
            ret = ret + t.getLabel().toLowerCase() + "@";
        }

        return ret;
    }
}
