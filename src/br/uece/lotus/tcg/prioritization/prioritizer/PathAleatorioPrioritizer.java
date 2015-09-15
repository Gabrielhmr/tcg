package br.uece.lotus.tcg.prioritization.prioritizer;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class PathAleatorioPrioritizer extends PathPrioritizer{

    @Override
    public PathSet select(LtsInfo lts, PathSet pathSet, String input) {
        
        Collection<List<Transition>> tcsPrioritized =  new HashSet<>();
        Collection<List<Transition>> paths = new HashSet();
        Set<Transition> transitions = new HashSet();
        paths.addAll(pathSet.getPathList());
        
        int i = 0;
        
        while(paths.size() > 0){
            
            Collection<List<Transition>> conjCandidates = calculateCandidates(pathSet.getVisitedTransitions(), paths);
            List<Transition> nextTcs = selectNext(tcsPrioritized, conjCandidates, transitions);
            
            paths.remove(nextTcs);
            
            pathSet.addPathWeight(nextTcs, i++);
            tcsPrioritized.add(nextTcs);
            
        }
       
        return pathSet;
    }
    
    private Collection<List<Transition>> calculateCandidates(Set<Transition> transitionSet, Collection<List<Transition>> paths){
        
        List<List<Transition>> pathList = new ArrayList<>();
        pathList.addAll(paths);
        
        Collection<List<Transition>> conjCandidates = new HashSet<>();
        Set<Transition> transitions = new HashSet<>();
        
        while(pathList.size() > 0 && conjCandidates.size() < 10){
            
            Random r = new Random();
            
            int index = 0;
            
            if (pathList.size() > 1) {
                index = r.nextInt(pathList.size()-1);
            }
            
            List<Transition> aleatorio = pathList.get(index);
            
            if (!transitions.containsAll(aleatorio)) {
                
                transitions.addAll(aleatorio);                
                conjCandidates.add(aleatorio);
                pathList.remove(aleatorio);
            }else {
                break;
            }            
        }
                
        return conjCandidates;
    }
    
    
    private List<Transition> selectNext(Collection<List<Transition>> tcsPrioritized, Collection<List<Transition>> tcsCandidates, Set<Transition> transitions){
        
        if(tcsPrioritized.isEmpty()){
            return tcsCandidates.iterator().next();
        }else{
            
            double matrix[][] = calculateDistanceMatrix(tcsPrioritized, tcsCandidates, transitions);
            List<Transition> next = getMostDistantPath(matrix, tcsCandidates);
            
            return next;
        }
    }
    
    abstract protected double[][] calculateDistanceMatrix(Collection<List<Transition>> tcsPrioritized, Collection<List<Transition>> tcsCandidates, Set<Transition> transitions);
    
    private List<Transition> getMostDistantPath(double matrix[][], Collection<List<Transition>> tcsCandidates) {
        
        int maxIndexX = 0;
        int maxIndexY = 0;
        double max = 0;
        
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (matrix[i][j] > max) {
                    maxIndexX = i;
                    maxIndexY = j;
                    max = matrix[i][j];
                }
            }
        }
        
        List<Transition> ret = tcsCandidates.iterator().next();
        
        for (int i = 0; i < maxIndexY; ++i) {
            ret = tcsCandidates.iterator().next();
        }
        
        return ret;
    }
}
