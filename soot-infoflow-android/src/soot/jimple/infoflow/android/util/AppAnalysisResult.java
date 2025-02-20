package soot.jimple.infoflow.android.util;

public class AppAnalysisResult {

    private String APP_NAME;

    private Metrics metrics;

    private String cg_name;

    private int num_of_reachable_methods;

    private long num_of_methods_propagated;

    private long num_edges_propagated;

    private long num_statements_propagated;

    public static AppAnalysisResult getInstance(){
        return new AppAnalysisResult();
    }

    private AppAnalysisResult(){
    }

    public String getAPP_NAME() {
        return APP_NAME;
    }


    public Metrics getMetrics() {
        return metrics;
    }

    public void setAPP_NAME(String APP_NAME) {
        this.APP_NAME = APP_NAME;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public String getCg_name() {
        return cg_name;
    }

    public void setCg_name(String cg_name) {
        this.cg_name = cg_name;
    }

    public void setNum_of_reachable_methods(int num_of_reachable_methods) {
        this.num_of_reachable_methods = num_of_reachable_methods;
    }

    public int getNum_of_reachable_methods() {
        return num_of_reachable_methods;
    }

    public long getNum_of_methods_propagated() {
        return num_of_methods_propagated;
    }

    public void setNum_of_methods_propagated(long num_of_methods_propagated) {
        this.num_of_methods_propagated = num_of_methods_propagated;
    }

    public long getNum_edges_propagated() {
        return num_edges_propagated;
    }

    public void setNum_edges_propagated(long num_edges_propagated) {
        this.num_edges_propagated = num_edges_propagated;
    }

    public long getNum_statements_propagated() {
        return num_statements_propagated;
    }

    public void setNum_statements_propagated(long num_statements_propagated) {
        this.num_statements_propagated = num_statements_propagated;
    }
}
