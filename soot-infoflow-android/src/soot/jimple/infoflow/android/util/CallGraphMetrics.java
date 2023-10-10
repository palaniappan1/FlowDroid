package soot.jimple.infoflow.android.util;

import metrics.CallGraphConstructionMetrics;

import java.util.ArrayList;
import java.util.List;

public class CallGraphMetrics {

    private static CallGraphMetrics instance;

    public static CallGraphMetrics getInstance(){
        instance = new CallGraphMetrics();
        return instance;
    }

    public void clear(){
        instance = null;
        callGraphConstructionMetrics = new ArrayList<>();
    }

    private List<CallGraphConstructionMetrics> callGraphConstructionMetrics;

    private CallGraphMetrics() {
        callGraphConstructionMetrics = new ArrayList<>();
    }


    public List<CallGraphConstructionMetrics> getCallGraphConstructionMetrics() {
        return callGraphConstructionMetrics;
    }

    public void setCallGraphConstructionMetrics(CallGraphConstructionMetrics callGraphConstructionMetrics) {
        this.callGraphConstructionMetrics.add(callGraphConstructionMetrics);
    }

}
