package br.uece.lotus.tcg.selection.selector;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.List;

public class PathWeightedSimilarityPathSelector extends PathSimilaritySelector{

    @Override
    public String getName(){
        return "Weighted Similarity";
    }

    @Override
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
                Double weight1 = calculateWeighted(path);
                Double weight2 = calculateWeighted(path2);

                similarityMatrix[i][j] = value / weight1;
                similarityMatrix[j][i] = value / weight2;
                
            }
        }
        return similarityMatrix;
    }

    protected double calculateWeighted(List<Transition> path){

        Double ret = 1000.0;

        for (Transition t : path){
            ret *= t.getProbability();
        }

        return ret;
    }
}
