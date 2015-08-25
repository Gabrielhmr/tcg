package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.struct.TestBundle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class PathGenAllPaths extends PathGenerator{

    @Override
    public String getName(){
        return "All Paths";
    }

    @Override
    public String getDescription(){
        return "This generator does an exaustive search, generating a large number of paths.";
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
        return true;
    }

    @Override
    public boolean acceptPurpose(){
        return true;
    }

    @Override
    protected PathSet generate(LtsInfo lts, TestBundle bundle){
        
        PathSet pathSet = new PathSet();
        Queue<PathStruct> queue = new ArrayDeque<>();

        // Initialization
        queue.addAll(initialize(bundle));

        // Start stop condition set
        bundle.getStopConditionSet().start();

        while (!queue.isEmpty()){
            
            if (verifyAborted() || bundle.getStopConditionSet().satisfied()){
                break;
            }

            PathStruct topPath = queue.poll();

            if (isPathCompleted(lts, topPath, bundle.getAdditionalFinalTransitionList())){
                pathSet.addPath(topPath);
            }
            // Expand topPath            
            queue.addAll(expand(pathSet, topPath));
        }
        return pathSet;
    }

    protected List<PathStruct> expand(PathSet pathSet, PathStruct path){
        
        List<PathStruct> list = new ArrayList<>();

        for (Transition t : path.getCurrentState().getOutgoingTransitions()){
            
            State lastState = t.getDestiny();

            if (lastState == path.getCurrentState() && path.containsTransition(t)){
                continue;
            }

            PathStruct newPath = new PathStruct(path);
            newPath.addTransition(t);

            if (path.containsTransition(t)){ // Transition cycle
                
                if (path.containsTransitionCycle(newPath.getTransitionLastCycle())){
                    // Path already passed by this cycle
                    continue;
                }
            }
            list.add(newPath);
        }
        return list;
    }

    protected List<PathStruct> initialize(TestBundle bundle){
        
        List<PathStruct> list = new ArrayList<>();

        for (Transition t : bundle.getInitialTransitionList()){
            
            PathStruct ps = new PathStruct();
            ps.addTransition(t);
            list.add(ps);
        }

        return list;
    }

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

        Set<Transition> transitionRange = lts.getTransitionRange(ps.getInitialState());
        int visitedTransitionSize = ps.getVisitedTransitions().size();

        // Verify cases that all the transitions were visited
        if (visitedTransitionSize == transitionRange.size()){
            return true;
        }

        // Verify if there's some state to visit
        for (Transition t : lts.getTransitionRange(current)){
            if (!ps.getVisitedTransitions().contains(t)){
                return false;
            }
        }
        return true;
    }
}
