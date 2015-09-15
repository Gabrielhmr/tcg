package br.uece.lotus.tcg.prioritization;

import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.List;

public class PriorizationManager{

    PrioritizerSet mPrioritizerSet;

    public PriorizationManager(PrioritizerSet selSet){
        mPrioritizerSet = selSet;
    }

    public List< String> getPrioritizerList(){
        return mPrioritizerSet.getPrioritizerList();
    }

    public String getParameterText(String name){
        return mPrioritizerSet.getParameterText(name);
    }

    public String getDefaultParameterValue(String name){
        return mPrioritizerSet.getDefaultParameterValue(name);
    }

    public String getDescription(String name){
        return mPrioritizerSet.getDescription(name);
    }

    public boolean acceptParameter(String name){
        return mPrioritizerSet.acceptParameter(name);
    }

    public PathSet select(String selName, LtsInfo lts, PathSet pathSet, String input){
        return mPrioritizerSet.getPrioritizer(selName).select(lts, pathSet, input);
    }
}
