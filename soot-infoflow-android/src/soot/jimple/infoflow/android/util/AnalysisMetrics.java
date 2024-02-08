package soot.jimple.infoflow.android.util;

public class AnalysisMetrics {

    private static AnalysisMetrics instance;

    private AnalysisMetrics(){

    }

    private float analysisTime;

    private double memoryConsumed;


    public static AnalysisMetrics getInstance(){
        instance =  new AnalysisMetrics();
        return instance;
    }


    public float getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(float analysisTime) {
        this.analysisTime = analysisTime;
    }

    public double getMemoryConsumed() {
        return memoryConsumed;
    }

    public void setMemoryConsumed(double memoryConsumed) {
        this.memoryConsumed = memoryConsumed;
    }
}