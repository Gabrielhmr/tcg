package br.uece.lotus.tcg.prioritization.prioritizer;

import br.uece.lotus.Transition;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathARTJaccardPrioritizer extends PathAleatorioPrioritizer{

    @Override
    public String getName(){
        return "ART Jaccard Distance";
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
    protected double[][] calculateDistanceMatrix(Collection<List<Transition>> tcsPrioritized, Collection<List<Transition>> tcsCandidates, Set<Transition> transitions) {
        
        double matrix[][] = new double[tcsPrioritized.size()][tcsCandidates.size()];
                
        int i = 0;
        
        for (List<Transition> prioritizedPath : tcsPrioritized) {
            
            int j = 0;
            
            int numCommonTransitions = 0;
            int numDifferentTransitions = 0;
            
            Set<Transition> visitedSet = new HashSet<>();
            
            for (List<Transition> candidate : tcsCandidates) {
                
                for (Transition t : candidate) {
                    
                    if (!visitedSet.contains(t)) {
                        
                        if (prioritizedPath.contains(t)) {
                            ++numCommonTransitions;
                        }else {
                            ++numDifferentTransitions;
                        }
                        
                        visitedSet.add(t);
                    }
                }
                
                if (numDifferentTransitions > 0) {
                   matrix[i][j] = 1 - (numCommonTransitions / numDifferentTransitions);   
               }else {
                    matrix[i][j] = 0;
               }
                
                ++j;
            }                     
            ++i;
        }
        
        return matrix;
    }
}
