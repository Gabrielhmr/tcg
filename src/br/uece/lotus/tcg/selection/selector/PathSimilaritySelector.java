package br.uece.lotus.tcg.selection.selector;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.struct.RandomPathSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PathSimilaritySelector extends PathSelector{

    protected List<List<Transition>> mPathList;

    @Override
    public String getName(){
        return "Path Similarity";
    }

    @Override
    public String getParameterText(){
        return "Coverage (%)";
    }

    @Override
    public String getParameterDefaultValue(){
        return "50.0";
    }

    @Override
    public boolean acceptParameter(){
        return true;
    }

    @Override
    public String getDescription(){
        return "Filter paths accordingly with a matrix of similarity between the paths.";
    }

    @Override
    public PathSet select(LtsInfo lts, PathSet pathSet, String input){
        
        if (input == null || input.isEmpty()){
            input = "100.0";
        }

        mPathList = new ArrayList<>();

        for (List<Transition> c : pathSet.getPathList()){
            mPathList.add(c);
        }

        Double[][] matrix = mountSimiliratyMatrix(pathSet);
        PathSet ret = new RandomPathSet();

        Set<Integer> removeList = new HashSet<>();

        int numPathsToRemove = (int) ((1.0 - (Double.parseDouble(input) / 100.0)) * pathSet.getPathsCount());

        if (numPathsToRemove <pathSet.getPathsCount()){
            
            for (int i = 0; i <numPathsToRemove; ++i){
                
                //Get the paths with bigger similarity
                int pathToRemoveIndex = getBiggerSimilarityPathIndex(pathSet, matrix);

                for (int j = 0; j <pathSet.getPathsCount(); ++j){
                    matrix[pathToRemoveIndex][j] = -1.0;
                    matrix[j][pathToRemoveIndex] = -1.0;
                }

                removeList.add(pathToRemoveIndex);
            }

            for (int index = 0; index <mPathList.size(); ++index){
                
                if (removeList.contains(index)){
                    removeList.remove(index);
                }else{
                    ret.addPath(new PathStruct(mPathList.get(index)));
                }
                
            }
        }
        return ret;
    }

    protected Double[][] mountSimiliratyMatrix(PathSet pathSet){
        
        List<Transition> path = null;
        List<Transition> path2 = null;

        int numPaths = pathSet.getPathsCount();

        Double[][] similarityMatrix = new Double[numPaths][numPaths];

        for (Integer i = 0; i <numPaths; ++i){
            
            similarityMatrix[i][i] = -1.0;
            path = mPathList.get(i);

            for (Integer j = i + 1; j <numPaths; ++j){
                
                path2 = mPathList.get(j);
                Double value = calculateSimilarity(path, path2);

                similarityMatrix[i][j] = value;
                similarityMatrix[j][i] = value;
            }
        }

        return similarityMatrix;
    }

    protected Double calculateSimilarity(List<Transition> path, List<Transition> path2){
        
        Double ret = 0.0;
        Double med = (path.size() + path2.size()) / 2.0;

        for (Transition t : path){
            for (Transition t2 : path2){
                if (t == t2){
                    ret += 1.0;
                    break;
                }
            }
        }

        return ret / med;
    }

    int getBiggerSimilarityPathIndex(PathSet pathSet, Double[][] similarityMatrix){
        
        List<Transition> path = null;
        int index = -1;

        Double value = -1.0;

        int numPaths = pathSet.getPathsCount();

        for (int i = 0; i <numPaths - 1; ++i){
            
            for (int j = i + 1; j <numPaths; ++j){
                
                List<Transition> path1 = mPathList.get(i);
                List<Transition> path2 = mPathList.get(j);

                if (value <similarityMatrix[i][j]){
                    
                    value = similarityMatrix[i][j];

                    if (path1.size() > path2.size()){
                        path = path1;
                        index = j;
                    }else if (path2.size() > path1.size()){
                        path = path2;
                        index = i;
                    }else{ 
                        // Equal size - get a path index randomly
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
}
