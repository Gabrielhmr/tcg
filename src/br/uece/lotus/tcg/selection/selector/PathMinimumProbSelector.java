package br.uece.lotus.tcg.selection.selector;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import java.util.List;

public class PathMinimumProbSelector extends PathSelector{

    @Override
    public String getName(){
        return "Minimum Probability";
    }

    @Override
    public String getParameterText(){
        return "Minimum prob. value (%):";
    }

    @Override
    public String getParameterDefaultValue(){
        return "15.0";
    }

    @Override
    public boolean acceptParameter(){
        return true;
    }

    @Override
    public String getDescription(){
        return "Returns all paths whose odds are greater than or equal likelihood entered by the user. Note: Add the probability in percent (%)";
    }

    @Override
    public PathSet select(LtsInfo lts, PathSet pathSet, String input){

        Double minimumProbability = Double.parseDouble(input);
        minimumProbability *= 0.01;

        PathSet ret = new PathSet();

        for (List<Transition> path : pathSet.getPathList()){
            
            if (calculatePathProbability(path) >= minimumProbability){
                ret.addPath(new PathStruct(path));
            }
        }

        return ret;
    }

    protected Double calculatePathProbability(List<Transition> path){
        
        Double prob = 1.0;

        for (Transition t : path){
            prob *= t.getProbability();
        }

        return prob;
    }
}
