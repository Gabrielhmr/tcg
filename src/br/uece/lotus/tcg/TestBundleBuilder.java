package br.uece.lotus.tcg;

import br.uece.lotus.Transition;
import br.uece.lotus.tcg.purpose.TestPurposeSuite;
import br.uece.lotus.tcg.stopcondition.StopConditionSet;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.TestBundle;
import br.uece.lotus.tcg.utils.DebugLog;
import br.uece.lotus.tcg.utils.StateUtils;
import java.util.ArrayList;
import java.util.List;

public class TestBundleBuilder {

    static public TestBundle build(LtsInfo lts, TestPurposeSuite suite, StopConditionSet stopConditions) {
        List<Transition> initTransitions = new ArrayList<>();
        List<Transition> additionalFinalTransitions = new ArrayList<>();

        StopConditionSet scSet = stopConditions;

        if (scSet == null){
            scSet = new StopConditionSet(null);
        }

        DebugLog.printLog("Stop condition set size: " + scSet.getStopConditionCount());

        if (suite == null){
            initTransitions.addAll(StateUtils.getOutgoingTransitions(lts.getRootState()));
        } else {
            if (suite.getInitialTransitions() == null){
                initTransitions.addAll(StateUtils.getOutgoingTransitions(lts.getRootState()));
            }else{
                initTransitions.addAll(suite.getInitialTransitions());
            }

            if (suite.getAdditionalFinalTransitions() != null) {
                additionalFinalTransitions.addAll(suite.getAdditionalFinalTransitions());
            }
        }

        TestBundle bundle = new TestBundle(initTransitions,additionalFinalTransitions,scSet);

        return bundle;
    }
}
