package br.uece.lotus.tcg.prioritization;

import br.uece.lotus.Transition;
import java.util.ArrayList;

public class ARTJaccardPrioritization extends PathPrioritization{

    @Override
    public String getName(){
        return "ART Jaccard";
    }

    @Override
    public String getParameterText(){
        return "";
    }

    @Override
    public String getParameterDefaultValue(){
        return "";
    }

    @Override
    public boolean acceptParameter(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<ArrayList<Transition>> prioritizePaths(ArrayList<Transition> ltsTransitionsList, ArrayList<ArrayList<Transition>> paths, String input){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<ArrayList<Transition>> prioritize(ArrayList<ArrayList<Transition>> paths, String input){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canExecute(ArrayList<Transition> ltsTransitions){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
