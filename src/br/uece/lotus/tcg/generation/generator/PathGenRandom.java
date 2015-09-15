package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.struct.RandomPathSet;
import br.uece.lotus.tcg.struct.TestBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class PathGenRandom extends PathGenerator{

    protected Integer mParameter;
    protected Random mIntGen = new Random();

    @Override
    public String getName(){
        return "Random Paths";
    }

    @Override
    public String getDescription(){
        return "This generator will return a specified number of random paths.";
    }

    @Override
    public PathSet generate(LtsInfo lts, TestBundle bundle){
        
        if (mParameter == null || mParameter == 0){
            mParameter = 10;
        }

        PathSet pathSet = new RandomPathSet();
        Stack<PathStruct> stack = new Stack<>();

        // Initialization
        stack.addAll(initialize(bundle));

        // Start stop condition set
        bundle.getStopConditionSet().start();

        while (!stack.isEmpty()){
            pathSet.addPath(generatePath(lts, bundle, stack.pop()));
        }

        return pathSet;
    }

    protected PathStruct generatePath(LtsInfo lts, TestBundle bundle, PathStruct path){
        
        while (!isPathCompleted(lts, path, bundle.getAdditionalFinalTransitionList())){
            
            if (verifyAborted() || bundle.getStopConditionSet().satisfied()){
                break;
            }

            path = expand(path);
        }

        return path;
    }

    protected List<PathStruct> initialize(TestBundle bundle){
        
        List<PathStruct> list = new ArrayList<>();

        for (int i = 0; i <mParameter; ++i){

            int index = mIntGen.nextInt(bundle.getInitialTransitionList().size());

            PathStruct ps = new PathStruct();
            ps.addTransition(bundle.getInitialTransitionList().get(index));

            list.add(ps);
        }

        return list;
    }

    protected PathStruct expand(PathStruct path){
        
        State currentState = path.getCurrentState();
        
        int index = mIntGen.nextInt(currentState.getOutgoingTransitionsCount());

        path.addTransition(((List<Transition>) currentState.getOutgoingTransitions()).get(index));

        return path;
    }

    public boolean isPathCompleted(LtsInfo lts, PathStruct ps, List<Transition> additionalFinalTransitions){
        
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

        // The getStateRange will only contain the state passed as parameter if there's a cycle returning to it
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

    protected boolean isGenerationCompleted(LtsInfo lts, PathSet pathSet){
        return pathSet.getPathsCount() == mParameter;
    }

    @Override
    public boolean acceptParameter(){
        return true;
    }

    @Override
    public String getParameterText(){
        return "Number of paths:";
    }

    @Override
    public void setParameter(String value){
        if (value == null || value.isEmpty()){
            mParameter = 10;
        } else{
            mParameter = Integer.parseInt(value);
        }
    }

    @Override
    public boolean acceptPurpose(){
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
