package br.uece.lotus.tcg.struct;

import br.uece.lotus.State;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AllStatesPathSet extends PathSet {

    private Map<Set<State>, PathStruct> mPathMap;

    public AllStatesPathSet(){
        super();
        mPathMap = new HashMap<>();
    }

    @Override
    public void addPath(PathStruct ps){
        
        Set<State> stateSet = ps.getVisitedStates();

        if (mVisitedStates.containsAll(ps.getVisitedStates())){
            return;
        }

        if (mPathMap.containsKey(stateSet)){
            
            PathStruct currentPs = mPathMap.get(stateSet);
            
            if (ps.getPath().size() >= currentPs.getPath().size()){
                return;
            }
        }

        for (Map.Entry<Set<State>, PathStruct> it : mPathMap.entrySet()){
            
            if (it.getKey().size() <stateSet.size() && stateSet.containsAll(it.getKey())){
                
                mPathList.remove(it.getValue().getPath());
                mPathMap.remove(it);                
            }
        }

        mPathMap.put(stateSet, ps);

        mPathList.add(ps.getPath());

        mVisitedTransitions.addAll(ps.getVisitedTransitions());
        mVisitedStates.addAll(ps.getVisitedStates());

        mCycleListStates.addAll(ps.getStatesCycleList());
        mCyclesListTransitions.addAll(ps.getTransitionsCycleList());
    }
}
