package soot.jimple.infoflow.android.util;

public class Metrics {
    private static Metrics instance;

    private AnalysisMetrics analysisMetrics;

    private CallGraphMetrics callGraphMetrics;
    private Metrics() {

    }

    public static Metrics getInstance(){
        instance = new Metrics();
        return instance;
    }

    public AnalysisMetrics getAnalysisMetrics() {
        return analysisMetrics;
    }

    public void setAnalysisMetrics(AnalysisMetrics analysisMetrics) {
        this.analysisMetrics = analysisMetrics;
    }

    public CallGraphMetrics getCallGraphMetrics() {
        return callGraphMetrics;
    }

    public void setCallGraphMetrics(CallGraphMetrics callGraphMetrics) {
        this.callGraphMetrics = callGraphMetrics;
    }

    public void clear() {
        instance = null;
    }
}