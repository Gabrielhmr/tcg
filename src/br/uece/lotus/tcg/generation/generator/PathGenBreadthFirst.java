package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.PathStruct;
import br.uece.lotus.tcg.struct.TestBundle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

abstract public class PathGenBreadthFirst extends PathGenerator{

    @Override
    public PathSet generate(LtsInfo lts, TestBundle bundle){
        
        PathSet pathSet = new PathSet();
        Queue<PathStruct> queue = new ArrayDeque<>();

        // Initialization
        queue.addAll(initialize(bundle));

        // Start stop condition set
        bundle.getStopConditionSet().start();

        while (!queue.isEmpty()){
            
            if (verifyAborted() || bundle.getStopConditionSet().satisfied()){
                break;
            }

            PathStruct topPath = queue.poll();

            if (isPathCompleted(lts, topPath, bundle.getAdditionalFinalTransitionList())){
                
                pathSet.addPath(topPath);

                if (isGenerationCompleted(lts, pathSet)){
                    break;
                }
            }
            // Expand topPath
            queue.addAll(expand(topPath));
        }
        return pathSet;
    }

    /**
     * This method shall verify based on LtsInfo and the PathSet passed to it,
     * if the process (of path generation) should continue or not.
     *
     * @param lts
     * @param pathSet
     * @return -> true, if the process of generations of paths is completed
     * false, case not
     */
    abstract protected boolean isGenerationCompleted(LtsInfo lts, PathSet pathSet);

    protected List<PathStruct> initialize(TestBundle bundle){
        
        List<PathStruct> list = new ArrayList<>();

        for (Transition t : bundle.getInitialTransitionList()){
            
            PathStruct ps = new PathStruct();
            ps.addTransition(t);

            list.add(ps);
        }
        return list;
    }

    /**
     * This method shall perform the expansion of the path passed, adding new
     * potential paths in the stack.
     *
     * @param path
     * @return
     */
    abstract protected List<PathStruct> expand(PathStruct path);

    /**
     *
     * @param lts
     * @param ps
     * @param additionalFinalTransitions
     * @return -> true, if the path can be considered a terminated path, it ends
     * in a halt state / transition or has reached an infinite cycle -> false,
     * case not
     */
    abstract protected boolean isPathCompleted(LtsInfo lts, PathStruct ps, List<Transition> additionalFinalTransitions);
}
