package br.uece.lotus.tcg.prioritization.prioritizer;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PathFixedWeightsPrioritizer extends PathPrioritizer{

    @Override
    public String getName(){
        return "Fixed Weights";
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
        
        Map<State, Integer> stateMap = new HashMap<>();
        Map<Transition, Integer> transitionMap = new HashMap<>();
        
        for (List<Transition> c : pathSet.getPathList()){
            
            Integer weight = 0;
            Set<Transition> transitionSet = new HashSet<>();
            Set<State> stateSet = new HashSet<>();
            
            for(Transition t : c){
                Integer w = 0;
                
                if (!transitionSet.contains(t)) {
                    if (transitionMap.containsKey(t)) {
                        w += transitionMap.get(t);
                        
                        if (!stateSet.contains(t.getSource())){
                            w += stateMap.get(t.getSource());
                            stateSet.add(t.getSource());
                        }
                        
                        if (!stateSet.contains(t.getDestiny())){
                            w += stateMap.get(t.getDestiny());
                            stateSet.add(t.getDestiny());
                        }
                        
                    }
                    
                    else {
                        if (!stateSet.contains(t.getSource())){
                            int x = 1;
                            
                            if (!stateMap.containsKey(t.getSource())) {
                                
                                if(t.getSource().getOutgoingTransitionsCount() > 1){
                                    x = 2;
                                }
                                
                                stateMap.put(t.getSource(), x);
                                
                            }
                            
                            else {
                                x = stateMap.get(t.getSource());
                            }
                            
                            w += x;
                            stateSet.add(t.getSource());
                        }
                        
                        if (!stateSet.contains(t.getDestiny())){
                            int x = 1;
                            
                            if (!stateMap.containsKey(t.getDestiny())) {
                                
                                if(t.getDestiny().getOutgoingTransitionsCount() > 1){
                                    x = 2;
                                }
                                
                                stateMap.put(t.getDestiny(), x);
                                
                            }
                            
                            else {
                                x = stateMap.get(t.getDestiny());
                            }
                            
                            w += x;
                            stateSet.add(t.getDestiny());
                        }
                        
                        if (transitionMap.containsKey(t)) {
                            w += transitionMap.get(t);
                        }
                        
                        else {
                            int x = t.getSource().getIncomingTransitionsCount() * t.getDestiny().getOutgoingTransitionsCount();
                            transitionMap.put(t, x);
                            
                            w += x;
                        }
                        
                        transitionSet.add(t);
                    }
                    
                }
                
                weight += w;
          }
            
          pathSet.addPathWeight(c, weight);
        }

        return pathSet;
    }
    
}
