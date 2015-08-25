package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.utils.StateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathGenStatisticalRandom extends PathGenRandom{

    protected Integer mParameter;
    protected Random mIntGen = new Random();

    @Override
    public String getName(){
        return "Statistical Random Paths";
    }

    @Override
    public String getDescription(){
        return "This generator will return a specified number of statistical random paths."
                + " The difference between this generator and the regular random generator is that the "
                + "statistical take in account the probability of the transition to expand a path.";
    }

    @Override
    protected PathStruct expand(PathStruct path){
        path.addTransition(getWeightedRandomTransition(path));
        return path;
    }

    protected Transition getWeightedRandomTransition(PathStruct path){
        
        List<Transition> list = new ArrayList<>(StateUtils.getOutgoingTransitions(path.getCurrentState()));
        List<Double> probList = new ArrayList<>();

        Double sumProbabilities = 0.0;
        Double w = 0.0;

        Transition ret = null;

        Random randomGen = new Random();

        for (int i = 0; i <list.size(); ++i){
            
            Transition t = list.get(i);
            double denominator = 1.0;

            if (path.getRepeatedStates().contains(t)){
                denominator = 0.1;
            }

            Double tProb = t.getProbability() * denominator;
            sumProbabilities += tProb;

            probList.add(tProb);
        }

        w = sumProbabilities * (randomGen.nextInt(101) % 100 + 1);

        while (w > 0){            
            int index = mIntGen.nextInt(list.size());
            ret = list.get(index);
            w -= probList.get(index);
        }

        return ret;
    }
}
