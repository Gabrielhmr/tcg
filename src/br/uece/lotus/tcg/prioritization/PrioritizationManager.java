package br.uece.lotus.tcg.prioritization;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.utils.DebugLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class PrioritizationManager{

    private final String CLASS_NAME = "PrioritizationManager";

    protected LinkedHashMap<String, PathPrioritization> mPrioritizationMap;

    public PrioritizationManager(){
        
        ArrayList<PathPrioritization> priorizeList = new ArrayList<>();
        mPrioritizationMap = new LinkedHashMap<>();

        priorizeList.add(new ARTJaccardPrioritization());
        priorizeList.add(new WeightedSimilarityPrioritization());

        for (PathPrioritization s : priorizeList){
            mPrioritizationMap.put(s.getName(), s);
        }
    }

    public String getPrioritizationDescription(String name){
        return mPrioritizationMap.get(name).getDescription();
    }

    public String getDefaultParameterValue(String name){
        return mPrioritizationMap.get(name).getParameterDefaultValue();
    }

    public boolean acceptParameter(String name){
        return mPrioritizationMap.get(name).acceptParameter();
    }

    public ArrayList<ArrayList<Transition>> prioritizePaths(String name, ArrayList<Transition> lts, ArrayList<ArrayList<Transition>> paths, String input){
        
        DebugLog.printLog(CLASS_NAME, "prioritizePaths - Start");

        if (!mPrioritizationMap.containsKey(name)){
            DebugLog.printLog("PrioritizationManager.prioritizePaths", "generator null!");
            return null;
        }

        ArrayList<ArrayList<Transition>> ret = mPrioritizationMap.get(name).prioritizePaths(lts, paths, input);

        DebugLog.printLog(CLASS_NAME, "prioritizePaths - Final");
        return ret;
    }

    public String getParameterText(String name){
        
        String ret = "";

        if (mPrioritizationMap.containsKey(name)){
            ret = mPrioritizationMap.get(name).getParameterText();
        }

        return ret;
    }

    public ArrayList<String> getPrioritizationList(){
        
        ArrayList<String> strList = new ArrayList<>();
        Iterator it = mPrioritizationMap.entrySet().iterator();

        while (it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry) it.next();
            strList.add(pair.getKey().toString());
        }

        return strList;
    }

}
