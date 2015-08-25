package br.uece.lotus.tcg.selection.selector;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.utils.PathHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PathMostProbableSelector extends PathSelector{

    protected Integer mParameter;

    @Override
    public String getName(){
        return "Most Probable Paths";
    }

    @Override
    public String getParameterText(){
        return "Number of paths:";
    }

    @Override
    public String getParameterDefaultValue(){
        return "1";
    }

    @Override
    public boolean acceptParameter(){
        return true;
    }

    @Override
    public String getDescription(){
        return "Selects the most probable paths generated.";
    }

    @Override
    public PathSet select(LtsInfo lts, PathSet pathSet, String input){

        if (input == null || input.isEmpty()){
            input = "1";
        }

        Double maximumProbability = 0.0;
        
        List<List<Transition>> mostProbablePaths = new ArrayList<>();
        PathSet ret = new PathSet();

        Integer numberOfPaths = Integer.parseInt(input);

        Map<List<Transition>,Double> probabilitiesMap = new HashMap<>();

        for (List<Transition> p : pathSet.getPathList()){
            Double prob = PathHelper.calculatePathProbability(p);
            probabilitiesMap.put(p, prob);
        }

        List list = new LinkedList<>(((HashMap)(probabilitiesMap)).entrySet());

        Collections.sort(list, new Comparator(){
            public int compare(Object o1, Object o2){
                return -1 * ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        
       HashMap<List<Transition >, Double > sortedHashMap = new HashMap();
       
       for (Iterator it = list.iterator(); it.hasNext();){
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put((List<Transition >)entry.getKey(), (Double)entry.getValue());
       } 
       
        int i = 0;

        for (Map.Entry<List<Transition >, Double > entry : sortedHashMap.entrySet()){
            if (i++ >= numberOfPaths){
                break;
            }

            ret.addPath(new PathStruct(entry.getKey()));
        }

        return ret;
    }
}
