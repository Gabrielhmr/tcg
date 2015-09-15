package br.uece.lotus.tcg.generation;

import br.uece.lotus.tcg.generation.generator.PathGenerator;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.TestBundle;
import br.uece.lotus.tcg.struct.PathSet;
import java.util.List;

public class GenerationManager{

    protected GeneratorSet mGeneratorSet;
    protected PathGenerator mGeneratorExecuting;

    public GenerationManager(GeneratorSet set){
        mGeneratorSet = set;
    }

    public List<String> getGeneratorList(){
        return mGeneratorSet.getGeneratorList();
    }

    public void setParameter(String name, String value){
        mGeneratorSet.setParameter(name, value);
    }

    public String getGeneratorDescription(String name){
        return mGeneratorSet.getGeneratorDescription(name);
    }

    public boolean acceptPrioritizer(String name){
        return mGeneratorSet.acceptPrioritizer(name);
    }
    
    public boolean acceptSelector(String name){
        return mGeneratorSet.acceptSelector(name);
    }
        
    public boolean acceptPurpose(String name){
        return mGeneratorSet.acceptPurpose(name);
    }

    public boolean acceptParamater(String name){
        return mGeneratorSet.acceptParameter(name);
    }

    public String getParameterText(String name){
        return mGeneratorSet.getParameterText(name);
    }

    public PathSet generate(String generatorName, LtsInfo lts, TestBundle bundle){
        
        mGeneratorExecuting = mGeneratorSet.getGenerator(generatorName);

        if (mGeneratorExecuting != null){
            PathSet ps = mGeneratorExecuting.startGeneration(lts, bundle);
            mGeneratorExecuting = null;
            return ps;
        }
        return null;
    }

    public void abortGeneration(){
        if (mGeneratorExecuting != null){
            mGeneratorExecuting.abortGeneration();
        }
    }

}
