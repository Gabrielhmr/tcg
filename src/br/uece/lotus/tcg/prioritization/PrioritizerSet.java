package br.uece.lotus.tcg.prioritization;

import br.uece.lotus.tcg.prioritization.prioritizer.PathPrioritizer;
import br.uece.lotus.tcg.prioritization.prioritizer.PathNullPrioritizer;
import br.uece.lotus.tcg.prioritization.prioritizer.PathARTJaccardPrioritizer;
import br.uece.lotus.tcg.prioritization.prioritizer.PathARTManhattanPrioritizer;
import br.uece.lotus.tcg.prioritization.prioritizer.PathFixedWeightsPrioritizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrioritizerSet{

    protected Map<String, PathPrioritizer> mPrioritizerMap;

    public PrioritizerSet(){
        init();
    }

    protected List<PathPrioritizer> initPrioritizers(){
        
        List<PathPrioritizer> selList = new ArrayList<>();

        selList.add(new PathNullPrioritizer());
        selList.add(new PathARTJaccardPrioritizer());
        selList.add(new PathARTManhattanPrioritizer());
        selList.add(new PathFixedWeightsPrioritizer());

        return selList;
    }

    protected void init(){
        
        mPrioritizerMap = new LinkedHashMap<>();
        List<PathPrioritizer> selList = initPrioritizers();

        for (PathPrioritizer p : selList){
            mPrioritizerMap.put(p.getName(), p);
        }
    }

    public List<String> getPrioritizerList(){
        
        List<String> selList = new ArrayList<>();

        for (Map.Entry<String, PathPrioritizer> it : mPrioritizerMap.entrySet()){
            selList.add(it.getKey());
        }
        return selList;
    }

    public String getParameterText(String name){
        
        if (mPrioritizerMap.containsKey(name)){
            return mPrioritizerMap.get(name).getParameterText();
        }
        return null;
    }

    public String getDefaultParameterValue(String name){
        
        if (mPrioritizerMap.containsKey(name)){
            return mPrioritizerMap.get(name).getParameterDefaultValue();
        }
        return null;
    }

    public String getDescription(String name){
        
        if (mPrioritizerMap.containsKey(name)){
            return mPrioritizerMap.get(name).getDescription();
        }
        return null;
    }

    public boolean acceptParameter(String name){
        
        if (mPrioritizerMap.containsKey(name)){
            return mPrioritizerMap.get(name).acceptParameter();
        }
        return false;
    }

    PathPrioritizer getPrioritizer(String name){
        
        if (mPrioritizerMap.containsKey(name)){
            return mPrioritizerMap.get(name);
        }
        return null;
    }
}
