package br.uece.lotus.tcg.selection;

import br.uece.lotus.tcg.selection.selector.PathMinimumProbSelector;
import br.uece.lotus.tcg.selection.selector.PathMostProbableSelector;
import br.uece.lotus.tcg.selection.selector.PathSelector;
import br.uece.lotus.tcg.selection.selector.PathWeightedSimilarityPathSelector;
import java.util.List;

public class StatisticalSelectorSet extends SelectorSet {

    @Override
    protected List<PathSelector> initSelectors() {
        
        List<PathSelector> selList = super.initSelectors();

        selList.add(new PathWeightedSimilarityPathSelector());
        selList.add(new PathMinimumProbSelector());
        selList.add(new PathMostProbableSelector());

        return selList;
    }
}
