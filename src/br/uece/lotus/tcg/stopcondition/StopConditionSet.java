package br.uece.lotus.tcg.stopcondition;

import br.uece.lotus.tcg.utils.DebugLog;
import java.util.ArrayList;

public class StopConditionSet{

    private ArrayList<StopCondition> mList;

    public StopConditionSet(ArrayList<StopCondition> array){
        
        mList = new ArrayList<>();
        if (array != null){
            mList.addAll(array);
        }
    }

    public int getStopConditionCount(){
        return mList.size();
    }

    public void start(){
        for (StopCondition sc : mList){
            sc.startMonitoring();
        }
    }

    public boolean satisfied(){
        
        for (StopCondition sc : mList){
            if (sc.satisfied()){
                DebugLog.printLog("StopConditionSet - CONDITION SATISFIED!");
                return true;
            }
        }
        return false;
    }
}
