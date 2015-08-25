package br.uece.lotus.tcg.utils;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PathHelper {

    public static final char PATH_LABEL_SEPARATOR = '@';

    public static String toInternString(ArrayList<Transition> path) {
        
        String ret = "";

        for (Transition t : path){
            ret += PATH_LABEL_SEPARATOR + t.getLabel().toLowerCase() + PATH_LABEL_SEPARATOR;
        }

        return ret;
    }

    public static String toString(List<Transition> path){
        
        String ret = "";

        for (Transition t : path) {
            ret += t.getLabel() + ", ";
        }

        if (!ret.isEmpty()){
            ret = ret.substring(0, ret.length() - 2);
        }

        return ret;
    }

    public static boolean hasSomeNewPlaceToGoProb(List<Transition> path, List<Transition> x) {

        if (path.isEmpty()){
            return false;
        }

        ArrayList<Transition> list = new ArrayList<>();
        list.add(path.get(path.size() - 1));
        ArrayList<Transition> visited = new ArrayList<>();

        HashMap< Transition, ArrayList<Transition>> tMap = new HashMap<>();

        ArrayList<ArrayList<Transition>> newPaths = new ArrayList<>();

        while (!list.isEmpty()){
            
            Transition currentTransition = list.get(0);
            State currentNode = list.get(0).getDestiny();

            list.remove(0);
            visited.add(currentTransition);

            for (Transition t : currentNode.getOutgoingTransitions()){
                
                if (!path.contains(t) && t.getProbability() > 0.0){
                    
                    ArrayList<Transition> y = new ArrayList<>();

                    if (tMap.containsKey(currentTransition)){
                        y.addAll(tMap.get(currentTransition));
                    }

                    y.add(t);
                    newPaths.add(y);
                    visited.add(t);
                    continue;
                }

                if (!visited.contains(t)){

                    ArrayList<Transition> k = new ArrayList<>();

                    if (tMap.containsKey(currentTransition)) {
                        k.addAll(tMap.get(currentTransition));
                    }

                    k.add(t);
                    tMap.put(t, k);
                    list.add(t);
                }
            }

            if (tMap.containsKey(currentTransition)){
                tMap.remove(currentTransition);
            }
        }

        if (newPaths.size() > 0){
            x.addAll(selectProbPath(newPaths));
            return true;
        }

        return false;
    }

    public static boolean hasSomeNewPlaceToGo(ArrayList<Transition> path, ArrayList<Transition> x) {

        if (path.isEmpty()){
            return false;
        }

        ArrayList<Transition> list = new ArrayList<>();
        list.add(path.get(path.size() - 1));
        ArrayList<Transition> visited = new ArrayList<>();

        HashMap<Transition, ArrayList<Transition>> tMap = new HashMap<>();

        ArrayList<ArrayList<Transition>> newPaths = new ArrayList<>();

        while (!list.isEmpty()){
            
            Transition currentTransition = list.get(0);
            State currentNode = list.get(0).getDestiny();

            list.remove(0);
            visited.add(currentTransition);

            for (Transition t : currentNode.getOutgoingTransitions()){
                
                if (!path.contains(t)) {
                    
                    ArrayList<Transition> y = new ArrayList<>();

                    if (tMap.containsKey(currentTransition)){
                        y.addAll(tMap.get(currentTransition));
                    }

                    y.add(t);
                    newPaths.add(y);
                    visited.add(t);
                    continue;
                }

                if (!visited.contains(t)){

                    ArrayList<Transition> k = new ArrayList<>();

                    if (tMap.containsKey(currentTransition)) {
                        k.addAll(tMap.get(currentTransition));
                    }

                    k.add(t);
                    tMap.put(t, k);
                    list.add(t);
                }
            }

            if (tMap.containsKey(currentTransition)){
                tMap.remove(currentTransition);
            }
        }

        if (newPaths.size() > 0){
            x.addAll(selectPath(newPaths));
            return true;
        }

        return false;
    }

    public static boolean hasSomeNewPlaceToGo(ArrayList<Transition> path){
        
        if (path.isEmpty()) {
            return false;
        }

        ArrayList<Transition> list = new ArrayList<>();
        list.add(path.get(path.size() - 1));
        ArrayList<Transition> visited = new ArrayList<>();

        while (!list.isEmpty()) {
            
            Transition currentTransition = list.get(0);
            State currentNode = list.get(0).getDestiny();
            list.remove(0);
            visited.add(currentTransition);

            for (Transition t : currentNode.getOutgoingTransitions()){
                
                if (!path.contains(t)){
                    return true;
                }

                if (!visited.contains(t)){
                    list.add(t);
                }
            }
        }
        
        return false;
    }

    public static ArrayList<Transition> IdentifyLastCycle(ArrayList<Transition> path){
        
        ArrayList<Transition> lastCycle = new ArrayList<>();

        State cycleNode = path.get(path.size() - 1).getDestiny();

        for (int i = path.size() - 1; i >= 0; --i){
            
            Transition t = path.get(i);
            lastCycle.add(0, t);

            if (t.getSource() == cycleNode){
                return lastCycle;
            }
        }

        return null;
    }

    public static boolean isHaltState(ArrayList<Transition> graph, State currentNode, int visitedTransitions){
        
        if (currentNode.isFinal()
                || currentNode.isError()
                || currentNode.getOutgoingTransitionsCount() == 0
                || visitedTransitions == graph.size()) {
            return true;
        }

        return false;
    }

    public static boolean isSelfLoopState(State node){
        
        if (node.getOutgoingTransitionsCount() == 1){
            
            State destiny = ((ArrayList<Transition>) node.getOutgoingTransitions()).get(0).getDestiny();

            if (destiny == node){
                return true;
            }
        }

        return false;
    }

    public static Transition selectProbTransition(State node){
        
        ArrayList<Transition> list = (ArrayList<Transition>) node.getOutgoingTransitions();
        Double sumProbabilities = 0.0;
        Double w = 0.0;
        Transition ret = null;
        Random randomGen = new Random();

        for (Transition t : node.getOutgoingTransitions()){
            sumProbabilities += t.getProbability();
        }

        w = sumProbabilities * (randomGen.nextInt(101) % 100 + 1);

        while (w > 0){
            ret = list.get((randomGen.nextInt(list.size())));
            w -= ret.getProbability();
        }

        return ret;
    }

    public static ArrayList<Transition> selectProbPath(ArrayList<ArrayList<Transition>> pathList) {
        
        ArrayList<Transition> ret = null;
        Double sumProbabilities = 0.0;
        Double w = 0.0;
        Random randomGen = new Random();
        ArrayList<Double> probArr = new ArrayList<>();

        for (ArrayList<Transition> t : pathList) {
            Double p = calculatePathProbability(t);
            sumProbabilities += p;
            probArr.add(p);
        }

        w = sumProbabilities * (randomGen.nextInt(101) % 100 + 1);

        while (w > 0){
            int index = randomGen.nextInt(pathList.size());
            ret = pathList.get(index);
            w -= probArr.get(index);
        }

        return ret;
    }

    public static ArrayList<Transition> selectPath(ArrayList<ArrayList<Transition>> pathList){
        
        ArrayList<Transition> ret = null;
        Random randomGen = new Random();

        int index = randomGen.nextInt(pathList.size());
        ret = pathList.get(index);

        return ret;
    }

    public static Transition selectProbTransition(ArrayList<Transition> transitions) {
        
        ArrayList<Transition> list = transitions;
        Double sumProbabilities = 0.0;
        Double w = 0.0;
        Transition ret = null;
        Random randomGen = new Random();

        for (Transition t : list){
            sumProbabilities += t.getProbability();
        }

        w = sumProbabilities * (randomGen.nextInt(101) % 100 + 1);

        while (w > 0){
            ret = list.get((randomGen.nextInt(list.size())));
            w -= ret.getProbability();
        }

        return ret;
    }

    public static Transition selectRandomTransition(State node){
        
        ArrayList<Transition> list = (ArrayList< Transition>) node.getOutgoingTransitions();
        Random randomGen = new Random();
        
        return list.get((randomGen.nextInt(list.size())));
    }

    public static Transition selectRandomTransition(ArrayList<Transition> list){
        
        Random randomGen = new Random();
        return list.get((randomGen.nextInt(list.size())));
    }

    public static boolean isStateFinal(State st){
        return (st.isFinal()
                || st.getOutgoingTransitionsCount() == 0
                || st.isError());
    }

    public static Double calculatePathProbability(List<Transition> path){

        Double probability = 1.0;

        for (Transition t : path) {
            probability *= t.getProbability();
        }

        return probability;
    }
}
