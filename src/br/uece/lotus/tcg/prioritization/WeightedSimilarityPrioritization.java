package br.uece.lotus.tcg.prioritization;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.utils.DebugLog;
import java.util.ArrayList;
import java.util.Random;

public class WeightedSimilarityPrioritization extends PathPrioritization{

    @Override
    public String getName(){
        return "Weighted Similarity";
    }

    public String getParameterDefaultValue(){
        return "0.5";
    }

    @Override
    public String getParameterText(){
        return "Coverage (%):";
    }

    @Override
    public boolean acceptParameter(){
        return true;
    }

    @Override
    public String getDescription(){
        return "";
    }

    @Override
    public boolean canExecute(ArrayList<Transition> ltsTransitions){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<ArrayList<Transition>> prioritizePaths(ArrayList<Transition> ltsTransitionsList, ArrayList<ArrayList<Transition>> paths, String input){

        if (input == null || input.isEmpty()){
            input = "1.0";
        }

        Double[][] matrix = mountSimiliratyMatrix(paths);
        ArrayList<ArrayList<Transition>> coverityList = new ArrayList<>();
        
        int numPathsToRemove = (int) ((1.0 - Double.parseDouble(input)) * paths.size());

        if (numPathsToRemove <paths.size()){
            
            coverityList.addAll(paths);
            
            for (int i = 0; i <numPathsToRemove; ++i){
                //Get the paths with bigger similarity
                int pathToRemoveIndex = getBiggerSimilarityPathIndex(paths, matrix);

                for (int j = 0; j <paths.size(); ++j){
                    matrix[pathToRemoveIndex][j] = -1.0;
                    matrix[j][pathToRemoveIndex] = -1.0;
                }

                ArrayList<Transition> pathToRemove = paths.get(pathToRemoveIndex);
                coverityList.remove(pathToRemove);
            }
        }
        return coverityList;
    }

    protected Double[][] mountSimiliratyMatrix(ArrayList<ArrayList<Transition>> allPaths){

        ArrayList<Transition> path = null;
        ArrayList<Transition> path2 = null;
        Double[][] similarityMatrix = new Double[allPaths.size()][allPaths.size()];

        for (Integer i = 0; i <allPaths.size(); ++i){

            similarityMatrix[i][i] = -1.0;
            path = allPaths.get(i);

            for (Integer j = i + 1; j <allPaths.size(); ++j){

                path2 = allPaths.get(j);
                Double value = calculateSimilarity(path, path2);
                Double weight1 = calculateWeighted(path);
                Double weight2 = calculateWeighted(path2);

                DebugLog.printTransitionArray(path);
                DebugLog.printLog(" Similarity: " + value);
                DebugLog.printLog(" Similarity Weighted: " + value / weight1);

                DebugLog.printTransitionArray(path2);
                DebugLog.printLog(" Similarity2: " + value);
                DebugLog.printLog(" Similarity Weighted2: " + value / weight2);

                similarityMatrix[i][j] = value / weight1;
                similarityMatrix[j][i] = value / weight2;

            }
        }

        return similarityMatrix;
    }

    protected Double calculateSimilarity(ArrayList<Transition> path, ArrayList<Transition> path2){
        
        Double ret = 0.0;

        for (Transition t : path){
            for (Transition t2 : path2){
                if (t == t2){
                    ret += 1.0;
                    break;
                }
            }
        }

        return ret;
    }

    protected double calculateWeighted(ArrayList<Transition> path){

        Double ret = 1000.0;

        for (Transition t : path){
            ret *= t.getProbability();
        }

        DebugLog.printTransitionArray(path);
        DebugLog.printLog("Weight: " + ret);

        return ret;
    }

    int getBiggerSimilarityPathIndex(ArrayList<ArrayList<Transition>> allPaths, Double[][] similarityMatrix){

        ArrayList<Transition> path = null;
        int index = -1;

        Double value = -1.0;

        for (int i = 0; i <allPaths.size(); ++i){

            for (int j = 0; j <allPaths.size(); ++j){

                if (i == j){
                    continue;
                }

                ArrayList<Transition> path1 = allPaths.get(i);
                ArrayList<Transition> path2 = allPaths.get(j);

                if (value <similarityMatrix[i][j]){

                    value = similarityMatrix[i][j];

                    if (path1.size() > path2.size()){
                        path = path1;
                        index = j;
                    } else if (path2.size() > path1.size()){
                        path = path2;
                        index = i;
                    } else{
                        Random randomGenerator = new Random();
                        int randomInt = randomGenerator.nextInt(2);
                        path = path1;
                        index = i;

                        if (randomInt == 1){
                            path = path2;
                            index = j;
                        }
                    }

                }
            }
        }

        return index;
    }

    @Override
    public ArrayList<ArrayList<Transition>> prioritize(ArrayList<ArrayList<Transition>> paths, String input){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
