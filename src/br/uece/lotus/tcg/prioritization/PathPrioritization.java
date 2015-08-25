package br.uece.lotus.tcg.prioritization;

import br.uece.lotus.Transition;
import java.util.ArrayList;

abstract public class PathPrioritization{

    abstract public String getName();

    abstract public String getParameterText();

    abstract public String getParameterDefaultValue();

    abstract public boolean acceptParameter();

    abstract public String getDescription();

    abstract public ArrayList<ArrayList<Transition>> prioritizePaths(ArrayList<Transition> ltsTransitionsList, ArrayList<ArrayList<Transition>> paths, String input);

    abstract public ArrayList<ArrayList<Transition>> prioritize(ArrayList<ArrayList<Transition>> paths, String input);

    abstract public boolean canExecute(ArrayList<Transition> ltsTransitions);

    @Override
    public String toString(){
        return getName();
    }
}
