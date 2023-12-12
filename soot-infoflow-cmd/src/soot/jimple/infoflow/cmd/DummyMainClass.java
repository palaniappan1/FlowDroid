package soot.jimple.infoflow.cmd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyMainClass {

    public static void main(String[] args) {
        decomposeJsonFile(0);
    }

    private static void decomposeJsonFile(int ground_truth_for_this_apk) {
        ObjectMapper objectMapper = new ObjectMapper();
        int leaksMax = Integer.MIN_VALUE;
        int cgEdgesMax = Integer.MIN_VALUE;
        double constructionTimeSum = 0.0;
        double memoryConsumedSum = 0.0;
        double analysisTimeSum = 0.0;
        double analysisMemorySum = 0.0;
        try {
            EvaluationConfig.setCurrentlyProcessingApkName("fakeappstore");
            JsonNode jsonNode = objectMapper.readTree(new File(EvaluationConfig.getJSON_FILE_PATh()));
            Map<String, List<JsonNode>> groupedData = new HashMap<>();
            for (JsonNode objNode : jsonNode) {
                int iteration = objNode.get("iteration").asInt();
                JsonNode metricsNode = objNode.path("metrics");
                if (metricsNode.isArray() && !metricsNode.isEmpty()) {
                    // Iterate through metrics
                    for (JsonNode metricNode : metricsNode) {
                        // Iterate through metric types (CHA, RTA, VTA, etc.)
                        metricNode.fieldNames().forEachRemaining(metricType -> {
                            // If the metric type is nested, you might need to adjust the code accordingly
                            JsonNode nestedMetricNode = metricNode.path(metricType);
                            groupedData.computeIfAbsent(metricType, k -> new ArrayList<>()).add(nestedMetricNode);
                        });
                    }
                }
            }
            for (Map.Entry<String, List<JsonNode>> entry : groupedData.entrySet()) {
                String metricType = entry.getKey();
                List<JsonNode> metricObjects = entry.getValue();

                // Reset variables for each group
                leaksMax = Integer.MIN_VALUE;
                cgEdgesMax = Integer.MIN_VALUE;
                constructionTimeSum = 0.0;
                memoryConsumedSum = 0.0;
                int cg_construction_number = 0;

                for (JsonNode objNode : metricObjects) {
                    JsonNode cgMetricNode = objNode.path("cg_metric");
                    JsonNode analysisMetricNode = objNode.path("analysis_metric");

                    // Calculate maximum for "leaks" and "cg_edges"
                    for (JsonNode metric : cgMetricNode) {
                        cg_construction_number += 1;
                        leaksMax = Math.max(leaksMax, metric.get("leaks").asInt());
                        cgEdgesMax = Math.max(cgEdgesMax, metric.get("cg_edges").asInt());

                        constructionTimeSum += metric.get("construction_time").asDouble();
                        memoryConsumedSum += metric.get("memory_consumed").asDouble();
                    }

                    analysisMemorySum += analysisMetricNode.get("analysis_memory").asDouble();
                    analysisTimeSum += analysisMetricNode.get("analysis_time").asDouble();
                }
                // Calculate averages
                double constructionTimeAvg = constructionTimeSum / cg_construction_number;
                double memoryConsumedAvg = memoryConsumedSum / cg_construction_number;
                double analysisMemoryAvg = analysisMemorySum / metricObjects.size();
                double analysisTimeAvg = analysisTimeSum / metricObjects.size();
                createDecomposedJsonObj(leaksMax, cgEdgesMax, constructionTimeAvg, memoryConsumedAvg, analysisMemoryAvg, analysisTimeAvg, metricType, ground_truth_for_this_apk);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDecomposedJsonObj(int leaksMax, int cgEdgesMax, double constructionTimeAvg, double memoryConsumedAvg, double analysisMemoryAvg, double analysisTimeAvg, String key, int ground_truth_for_this_apk) {
        JSONObject outerJsonObject = new JSONObject();
        JSONObject analysis_metric = new JSONObject();
        outerJsonObject.put("app_name", EvaluationConfig.getCurrentlyProcessingApkName());
        outerJsonObject.put("ground_truth", ground_truth_for_this_apk);
        analysis_metric.put("analysis_time", analysisTimeAvg);
        analysis_metric.put("analysis_memory", analysisMemoryAvg);
        JSONObject cg_construction_metric = new JSONObject();
        cg_construction_metric.put("cg_edges", cgEdgesMax);
        cg_construction_metric.put("leaks", leaksMax);
        cg_construction_metric.put("construction_time", constructionTimeAvg);
        cg_construction_metric.put("memory_consumed", memoryConsumedAvg);
        outerJsonObject.put("analysis_metric", analysis_metric);
        outerJsonObject.put("cg_metric", cg_construction_metric);
    }
}
