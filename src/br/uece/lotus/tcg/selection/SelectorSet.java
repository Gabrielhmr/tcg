package br.uece.lotus.tcg.selection;

import br.uece.lotus.tcg.selection.selector.PathNullSelector;
import br.uece.lotus.tcg.selection.selector.PathSelector;
import br.uece.lotus.tcg.selection.selector.PathSimilaritySelector;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectorSet{

    protected Map<String, PathSelector> mSelectorMap;

    public SelectorSet(){
        init();
    }

    protected List<PathSelector> initSelectors(){
        
        List<PathSelector> selList = new ArrayList<>();

        selList.add(new PathNullSelector());
        selList.add(new PathSimilaritySelector());

        return selList;
    }

    protected void init(){
        
        mSelectorMap = new LinkedHashMap<>();
        List<PathSelector> selList = initSelectors();

        for (PathSelector p : selList){
            mSelectorMap.put(p.getName(), p);
        }
    }

    public List<String> getSelectorList(){
        
        List<String> selList = new ArrayList<>();

        for (Map.Entry<String, PathSelector> it : mSelectorMap.entrySet()){
            selList.add(it.getKey());
        }
        return selList;
    }

    public String getParameterText(String name){
        
        if (mSelectorMap.containsKey(name)){
            return mSelectorMap.get(name).getParameterText();
        }
        return null;
    }

    public String getDefaultParameterValue(String name){
        
        if (mSelectorMap.containsKey(name)){
            return mSelectorMap.get(name).getParameterDefaultValue();
        }
        return null;
    }

    public String getDescription(String name){
        
        if (mSelectorMap.containsKey(name)){
            return mSelectorMap.get(name).getDescription();
        }
        return null;
    }

    public boolean acceptParameter(String name){
        
        if (mSelectorMap.containsKey(name)){
            return mSelectorMap.get(name).acceptParameter();
        }
        return false;
    }

    PathSelector getSelector(String name){
        
        if (mSelectorMap.containsKey(name)){
            return mSelectorMap.get(name);
        }
        return null;
    }
}
