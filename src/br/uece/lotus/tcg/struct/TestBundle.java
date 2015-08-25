package br.uece.lotus.tcg.struct;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.stopcondition.StopConditionSet;
import java.util.ArrayList;
import java.util.List;

public class TestBundle{

    private List<Transition> mInitialTransitionList;
    private List<Transition> mAdditionalFinalTransitionList;
    private StopConditionSet mStopConditionList;

    public List<Transition> getInitialTransitionList(){
        return mInitialTransitionList;
    }

    public List<Transition> getAdditionalFinalTransitionList(){
        return mAdditionalFinalTransitionList;
    }

    public StopConditionSet getStopConditionSet(){
        return mStopConditionList;
    }

    public TestBundle(List<Transition> initialTransitionList,
            List<Transition> additionalFinalTransitions,
            StopConditionSet stopConditions){
        
        if (initialTransitionList != null){
            mInitialTransitionList = initialTransitionList;
        }else{
            mInitialTransitionList = new ArrayList<>();
        }

        if (additionalFinalTransitions != null){
            mAdditionalFinalTransitionList = additionalFinalTransitions;
        }else{
            mAdditionalFinalTransitionList = new ArrayList<>();
        }

        if (stopConditions != null){
            mStopConditionList = stopConditions;
        }else{
            mStopConditionList = new StopConditionSet(null);
        }
    }

}
