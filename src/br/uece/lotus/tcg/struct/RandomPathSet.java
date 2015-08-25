package br.uece.lotus.tcg.struct;

import java.util.ArrayList;
import java.util.HashSet;

public class RandomPathSet extends PathSet{

    public RandomPathSet(){
        
        mPathList = new ArrayList<>();
        mVisitedStates = new HashSet<>();
        mVisitedTransitions = new HashSet<>();
        mCyclesListTransitions = new HashSet<>();
        mCycleListStates = new HashSet<>();
        
    }
}
