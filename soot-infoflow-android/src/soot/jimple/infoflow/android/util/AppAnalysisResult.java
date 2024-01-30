package soot.jimple.infoflow.android.util;

public class AppAnalysisResult {

    private String APP_NAME;

    private Metrics metrics;

    private String cg_name;

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
}
