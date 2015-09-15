package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.AllStatesPathSet;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.struct.TestBundle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class PathGenAllStates extends PathGenBreadthFirst{

    private Set<State> mStatesCoverage;

    @Override
    public String getName(){
        return "All States";
    }

    @Override
    public String getDescription(){
        return "Returns a set of paths in which the union of them cover all the states of the current LTS.";
    }

    @Override
    public final PathSet generate(LtsInfo lts, TestBundle bundle){
        
        PathSet pathSet = new AllStatesPathSet();
        Queue<PathStruct> queue = new ArrayDeque<>();

        // Initialization
        queue.addAll(initialize(lts, bundle));

        // Start stop condition set
        bundle.getStopConditionSet().start();

        while (!queue.isEmpty()){
            
            if (verifyAborted() || bundle.getStopConditionSet().satisfied()){
                break;
            }

            PathStruct topPath = queue.poll();

            if (isPathCompleted(lts, topPath, bundle.getAdditionalFinalTransitionList())){
                pathSet.addPath(topPath);

                if (isGenerationCompleted(lts, pathSet)){
                    break;
                }
            }
            // Expand topPath
            queue.addAll(expand(topPath));
        }
        return pathSet;
    }

    @Override
    public String getParameterText(){
        return null;
    }

    @Override
    public void setParameter(String value){
    }

    @Override
    public boolean acceptPurpose(){
        return true;
    }

    /**
     * This method shall verify based on LtsInfo and the PathSet passed to it,
     * if the process (of path generation) should continue or not.
     *
     * @param lts
     * @param pathSet
     * @return -> true, if the process of generations of paths is completed
     * false, case not
     */
    @Override
    protected boolean isGenerationCompleted(LtsInfo lts, PathSet pathSet){
        
        if (pathSet.getVisitedStatesCount() == mStatesCoverage.size()){
            return true;
        }

        return false;
    }

    protected List<PathStruct> initialize(LtsInfo lts, TestBundle bundle){
        
        List<PathStruct> list = new ArrayList<>();
        mStatesCoverage = new HashSet<>();

        for (Transition t : bundle.getInitialTransitionList()){
            
            PathStruct ps = new PathStruct();
            ps.addTransition(t);

            mStatesCoverage.add(t.getSource());
            mStatesCoverage.add(t.getDestiny());
            mStatesCoverage.addAll(lts.getStateRange(t.getDestiny()));

            list.add(ps);
        }
        return list;
    }

    /**
     * This method shall perform the expansion of the path passed, adding new
     * potential paths in the stack.
     *
     * @param path
     * @return
     */
    protected List<PathStruct> expand(PathStruct path){
        
        List<PathStruct> list = new ArrayList<>();

        for (Transition t : path.getCurrentState().getOutgoingTransitions()){
            
            State lastState = t.getDestiny();

            if (lastState == path.getCurrentState()){
                continue;
            }

            PathStruct newPath = new PathStruct(path);
            newPath.addTransition(t);

            list.add(newPath);
        }
        return list;
    }

    /**
     *
     * @param lts
     * @param ps
     * @param additionalFinalTransitions
     * @return -> true, if the path can be considered a terminated path, it ends
     * in a halt state / transition or has reached an infinite cycle -> false,
     * case not
     */
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
    public boolean acceptParameter(){
        return false;
    }

    @Override
    public boolean acceptSelector(){
        return true;
    }

    @Override
    public boolean acceptPrioritizer() {
        return false;
    }
}
