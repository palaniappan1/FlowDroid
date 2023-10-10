package soot.jimple.infoflow.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppAnalysisResult {

    private String APP_NAME;

    private List<HashMap<String, Metrics>> metrics;

    private static AppAnalysisResult instance;

    public static AppAnalysisResult getInstance(){
        instance = new AppAnalysisResult();
        return instance;
    }

    private AppAnalysisResult(){
        metrics = new ArrayList<>();
    }

    public String getAPP_NAME() {
        return APP_NAME;
    }

//    public Metrics(String APP_NAME, long ANALYSIS_TIME, double MEMORY_CONSUMPTION_OF_ANALYSIS, String CALL_GRAPH_TECHNIQUE,
//                   long CG_CONSTRUCTION_TIME_1, long CG_CONSTRUCTION_TIME_2, double MEMORY_CONSUMPTION_OF_CG_1, double MEMORY_CONSUMPTION_OF_CG_2) {
//
//        this.APP_NAME = APP_NAME;
//        this.ANALYSIS_TIME = ANALYSIS_TIME;
//        this.MEMORY_CONSUMPTION_OF_ANALYSIS = MEMORY_CONSUMPTION_OF_ANALYSIS;
//        this.CALL_GRAPH_TECHNIQUE = CALL_GRAPH_TECHNIQUE;
//        this.CG_CONSTRUCTION_TIME_1 = CG_CONSTRUCTION_TIME_1;
//        this.CG_CONSTRUCTION_TIME_2 = CG_CONSTRUCTION_TIME_2;
//        this.MEMORY_CONSUMPTION_OF_CG_1 = MEMORY_CONSUMPTION_OF_CG_1;
//        this.MEMORY_CONSUMPTION_OF_CG_2 = MEMORY_CONSUMPTION_OF_CG_2;
//    }


    public List<HashMap<String, Metrics>> getMetrics() {
        return metrics;
    }

    public void setAPP_NAME(String APP_NAME) {
        this.APP_NAME = APP_NAME;
    }

    public void setMetrics(String cg_name, Metrics metrics) {
        HashMap<String, Metrics> hashMap = new HashMap<>();
        hashMap.put(cg_name, metrics);
        this.metrics.add(hashMap);
    }
}
