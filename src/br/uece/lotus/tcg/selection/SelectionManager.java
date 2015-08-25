package br.uece.lotus.tcg.selection;

import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.List;

public class SelectionManager{

    SelectorSet mSelectorSet;

    public SelectionManager(SelectorSet selSet){
        mSelectorSet = selSet;
    }

    public List< String> getSelectorList(){
        return mSelectorSet.getSelectorList();
    }

    public String getParameterText(String name){
        return mSelectorSet.getParameterText(name);
    }

    public String getDefaultParameterValue(String name){
        return mSelectorSet.getDefaultParameterValue(name);
    }

    public String getDescription(String name){
        return mSelectorSet.getDescription(name);
    }

    public boolean acceptParameter(String name){
        return mSelectorSet.acceptParameter(name);
    }

    public PathSet select(String selName, LtsInfo lts, PathSet pathSet, String input){
        return mSelectorSet.getSelector(selName).select(lts, pathSet, input);
    }
}
