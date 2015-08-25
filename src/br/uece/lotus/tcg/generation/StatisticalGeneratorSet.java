package br.uece.lotus.tcg.generation;

import br.uece.lotus.tcg.generation.generator.PathGenStatisticalRandom;
import br.uece.lotus.tcg.generation.generator.PathGenerator;
import java.util.List;

public class StatisticalGeneratorSet extends GeneratorSet{

    @Override
    protected List<PathGenerator> initGenerators(){
        
        List<PathGenerator> genList = super.initGenerators();
        genList.add(new PathGenStatisticalRandom());
        return genList;
        
    }
}
