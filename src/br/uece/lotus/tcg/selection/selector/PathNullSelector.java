package br.uece.lotus.tcg.selection.selector;

import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;

public class PathNullSelector extends PathSelector{

    @Override
    public String getName(){
        return "None";
    }

    @Override
    public String getParameterText(){
        return null;
    }

    @Override
    public String getParameterDefaultValue(){
        return null;
    }

    @Override
    public boolean acceptParameter(){
        return false;
    }

    @Override
    public String getDescription(){
        return "No selector selected.";
    }

    @Override
    public PathSet select(LtsInfo lts, PathSet pathSet, String input){
        return pathSet;
    }
}
