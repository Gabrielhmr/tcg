package br.uece.lotus.tcg.stopcondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StopConditionManager{

    private HashMap<String,StopCondition> mStopConditionsMap;

    public StopConditionManager(){
        
        mStopConditionsMap = new HashMap<>();
        TimeStopCondition timeStopCondition = new TimeStopCondition();
        mStopConditionsMap.put(timeStopCondition.getName(), timeStopCondition);
        
    }

    public String getDescription(String name){
        return mStopConditionsMap.get(name).getDescription();
    }

    public String getDefaultParameterValue(String name){
        return mStopConditionsMap.get(name).getDefaultValue();
    }

    public String getParameterText(String name){
        
        String ret = "";

        if (mStopConditionsMap.containsKey(name)){
            ret = mStopConditionsMap.get(name).getPreText();
        }

        return ret;
    }

    public ArrayList<String> getStopConditionList(){
        
        ArrayList<String> strList = new ArrayList<>();
        Iterator it = mStopConditionsMap.entrySet().iterator();

        while (it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry) it.next();
            strList.add(pair.getKey().toString());
        }

        return strList;
    }

    public StopConditionSet mountSet(Map<String,String> conditionsSelected){
        
        Iterator it = conditionsSelected.entrySet().iterator();
        ArrayList< StopCondition> arr = new ArrayList<>();

        while (it.hasNext()){
            
            HashMap.Entry pair = (HashMap.Entry) it.next();
            
            StopCondition sc = mStopConditionsMap.get(pair.getKey());
            
            sc.setParameterValue((String) pair.getValue());
            arr.add(sc);
            
        }
        
        return new StopConditionSet(arr);
    }
}
