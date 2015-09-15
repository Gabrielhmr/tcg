package br.uece.lotus.tcg.prioritization.prioritizer;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathARTManhattanPrioritizer extends PathAleatorioPrioritizer{
    
    @Override
    public String getName(){
        return "ART Manhattan Distance";
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
        return "The Manhattan distance is calculated using two arrays of size equal to the amount of template transitions. The function evaluates the distance between two test cases for each of these two arrays is associated with each position and this has the number 1 if the test case has symbolized by the transition position and 0 otherwise.";
    }

    @Override
    protected double[][] calculateDistanceMatrix(Collection<List<Transition>> tcsPrioritized, Collection<List<Transition>> tcsCandidates, Set<Transition> transitions) {
        
        double matrix[][] = new double[tcsPrioritized.size()][tcsCandidates.size()];
        
        int i = 0;
        int inc = 0;
                
        for (List<Transition> prioritizedPath : tcsPrioritized) {
            
            int j = 0;
            
            int prioritVector[] = new int[transitions.size()];
            int candidateVector[] = new int[transitions.size()];
            
            int distance = 0;
                       
            for (List<Transition> candidatePath : tcsCandidates) {
                
                for(Transition t : transitions){
                                        
                    if(prioritizedPath.contains(t)){
                        prioritVector[i] = 1;
                    }else{
                        prioritVector[i] = 0;
                    }

                    if(candidatePath.contains(t)){
                        candidateVector[i] = 1;
                    }else{
                        candidateVector[i] = 0;
                    }
                    
                    ++inc;            
                    
                }
                
                distance = 0;
                
                for(int n = 0; n < prioritVector.length; n++){
                    
                    distance += Math.abs(prioritVector[n] - candidateVector[n]);
                            
                }
                
                matrix[i][j] = distance;                
            }            
            ++i;
        }
        
        return matrix;
    }
}
