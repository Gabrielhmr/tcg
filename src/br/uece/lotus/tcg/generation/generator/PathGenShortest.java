package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PathGenShortest extends PathGenBreadthFirst{

    @Override
    public boolean acceptPurpose(){
        return false;
    }

    @Override
    protected boolean isGenerationCompleted(LtsInfo lts, PathSet pathSet){
        return pathSet.getPathsCount() > 0;
    }

    @Override
    protected List<PathStruct> expand(PathStruct path){
        
        List<PathStruct> list = new ArrayList<>();

        for (Transition t : path.getCurrentState().getOutgoingTransitions()){
            
            State lastState = t.getDestiny();

            PathStruct newPath = new PathStruct(path);
            newPath.addTransition(t);

            list.add(newPath);
        }

        return list;
    }

    @Override
    protected boolean isPathCompleted(LtsInfo lts, PathStruct ps, List<Transition> additionalFinalTransitions){
        
        State current = ps.getCurrentState();

        if (current.isFinal() || current.isError()){
            return true;
        }

        if (additionalFinalTransitions != null){
            if (additionalFinalTransitions.contains(ps.getLastTransition())){
                return true;
            }
        }

        if (lts.getSelfLoopStateSet().contains(current)){
            return true;
        }

        Set<State> stateRange = lts.getStateRange(ps.getInitialState());
        int visitedStateSize = ps.getVisitedStates().size();

        // Verify cases that all the states were visited
        if (stateRange.size() - visitedStateSize <= 1){
            
            if (ps.getVisitedStates().size() == stateRange.size() + 1){
                return true;
            } else if (ps.getVisitedStates().size() == stateRange.size() && stateRange.contains(ps.getInitialState())){
                return true;
            }
        }

        // Verify if there's some state to visit
        for (State s : lts.getStateRange(current)){
            if (!ps.getVisitedStates().contains(s)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName(){
        return "Shortest Path";
    }

    @Override
    public String getDescription(){
        return "Returns the LTS's shortest complete path.";
    }

    @Override
    public boolean acceptParameter(){
        return false;
    }

    @Override
    public String getParameterText(){
        return null;
    }

    @Override
    public void setParameter(String value){
    }

    @Override
    public boolean acceptSelector(){
        return false;
    }
}
