package br.uece.lotus.tcg.prioritization.prioritizer;

import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;

public abstract class PathPrioritizer{

    abstract public String getName();

    abstract public String getParameterText();

    abstract public String getParameterDefaultValue();

    abstract public boolean acceptParameter();

    abstract public String getDescription();

    abstract public PathSet select(LtsInfo lts, PathSet pathSet, String input);

    @Override
    public String toString(){
        return getName();
    }
}
