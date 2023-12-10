package soot.jimple.infoflow.android.util;

import metrics.CallGraphConstructionMetrics;

import java.util.ArrayList;
import java.util.List;

public class CallGraphMetrics {

    private static CallGraphMetrics instance;

    private static int numberOfLeaks;

    public static CallGraphMetrics getInstance(){
        instance = new CallGraphMetrics();
        return instance;
    }

    public int getNumberOfLeaks() {
        return numberOfLeaks;
    }

    public void setNumberOfLeaks(int numberOfLeaks) {
        CallGraphMetrics.numberOfLeaks = numberOfLeaks;
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
