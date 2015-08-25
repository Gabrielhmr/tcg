package br.uece.lotus.tcg.struct;

import br.uece.lotus.Transition;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class AllTransitionsPathSet extends PathSet{

    private Map<Set<Transition>, PathStruct> mPathMap;

    public AllTransitionsPathSet(){
        super();
        mPathMap = new HashMap<>();
    }

    @Override
    public void addPath(PathStruct ps){
        
        Set<Transition> transitionSet = ps.getVisitedTransitions();

        if (mVisitedTransitions.containsAll(ps.getVisitedTransitions())){
            return;
        }

        if (mPathMap.containsKey(transitionSet)){
            
            PathStruct currentPs = mPathMap.get(transitionSet);

            if (ps.getPath().size() >= currentPs.getPath().size()){
                return;
            }
        }

        for (Map.Entry<Set<Transition>, PathStruct> it : mPathMap.entrySet()){
            
            if (it.getKey().size() < transitionSet.size() && transitionSet.containsAll(it.getKey())){
                mPathList.remove(it.getValue().getPath());
                mPathMap.remove(it);
            }
            
        }

        mPathMap.put(transitionSet, ps);

        mPathList.add(ps.getPath());

        mVisitedTransitions.addAll(ps.getVisitedTransitions());
        mVisitedStates.addAll(ps.getVisitedStates());

        mCycleListStates.addAll(ps.getStatesCycleList());
        mCyclesListTransitions.addAll(ps.getTransitionsCycleList());
    }
}
