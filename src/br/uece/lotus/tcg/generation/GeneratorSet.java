package br.uece.lotus.tcg.generation;

import br.uece.lotus.tcg.generation.generator.PathGenerator;
import br.uece.lotus.tcg.generation.generator.PathGenAllFreeLoop;
import br.uece.lotus.tcg.generation.generator.PathGenAllOneLoop;
import br.uece.lotus.tcg.generation.generator.PathGenAllPaths;
import br.uece.lotus.tcg.generation.generator.PathGenAllStates;
import br.uece.lotus.tcg.generation.generator.PathGenAllTransitions;
import br.uece.lotus.tcg.generation.generator.PathGenAllTransitionsPairs;
import br.uece.lotus.tcg.generation.generator.PathGenRandom;
import br.uece.lotus.tcg.generation.generator.PathGenShortest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorSet{

    protected Map<String, PathGenerator> mGeneratorMap;

    public GeneratorSet(){
        init();
    }
    
    public boolean acceptPrioritizer(String name){
        
        if (mGeneratorMap.containsKey(name)){
            return mGeneratorMap.get(name).acceptPrioritizer();
        }

        return false;
    }

    public boolean acceptSelector(String name){
        
        if (mGeneratorMap.containsKey(name)){
            return mGeneratorMap.get(name).acceptSelector();
        }

        return false;
    }

    public List<String> getGeneratorList(){
        
        List<String> genList = new ArrayList<>();

        for (Map.Entry<String, PathGenerator> it : mGeneratorMap.entrySet()){
            genList.add(it.getKey());
        }

        return genList;
    }

    boolean acceptPurpose(String name){
        
        if (mGeneratorMap.containsKey(name)){
            return mGeneratorMap.get(name).acceptPurpose();
        }
        return false;
    }

    boolean acceptParameter(String name){
        
        if (mGeneratorMap.containsKey(name)){
            return mGeneratorMap.get(name).acceptParameter();
        }
        return false;
    }

    String getParameterText(String name){
        
        if (mGeneratorMap.containsKey(name)){
            return mGeneratorMap.get(name).getParameterText();
        }
        return null;
    }

    void setParameter(String name, String paramValue){
        
        if (mGeneratorMap.containsKey(name)){
            mGeneratorMap.get(name).setParameter(paramValue);
        }
    }

    PathGenerator getGenerator(String name){
        
        if (mGeneratorMap.containsKey(name)){
            return mGeneratorMap.get(name);
        }
        return null;
    }

    String getGeneratorDescription(String name){
        
        PathGenerator pg = getGenerator(name);
        String desc = null;

        if (pg != null){
            desc = pg.getDescription();
        }
        return desc;
    }

    protected List<PathGenerator> initGenerators(){
        
        ArrayList<PathGenerator> genList = new ArrayList<>();

        genList.add(new PathGenAllPaths());
        genList.add(new PathGenAllFreeLoop());
        genList.add(new PathGenAllOneLoop());
        genList.add(new PathGenShortest());
        genList.add(new PathGenAllStates());
        genList.add(new PathGenAllTransitions());
        genList.add(new PathGenRandom());
        genList.add(new PathGenAllTransitionsPairs());

        return genList;
    }

    protected void init(){
        
        mGeneratorMap = new HashMap<>();
        List<PathGenerator> genList = initGenerators();

        for (PathGenerator p : genList){
            mGeneratorMap.put(p.getName(), p);
        }
    }
}
