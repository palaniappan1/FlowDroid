package soot.jimple.infoflow.cmd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import config.CallGraphAlgorithm;
import org.json.JSONArray;
import org.json.JSONObject;
import soot.jimple.infoflow.android.util.AppAnalysisResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static final JSONArray DECOMPOSED_JSON_ARRAY = new JSONArray();

    public static JSONArray getDecomposedJsonArray(){
        return DECOMPOSED_JSON_ARRAY;
    }
    public static JSONObject createJsonObject(AppAnalysisResult appAnalysisResult, int ground_truth_for_this_apk, int number_of_iterations) {
        JSONObject outerJsonObject = new JSONObject();
        JSONObject analysis_metric = new JSONObject();
        outerJsonObject.put("app_name", appAnalysisResult.getAPP_NAME());
        outerJsonObject.put("iteration", number_of_iterations);
        JSONArray jsonObjectArray = new JSONArray();
        appAnalysisResult.getMetrics().forEach(stringMetricsHashMap -> {
            JSONObject jsonObject = new JSONObject();
            final String[] callGraphAlgorithm = {CallGraphAlgorithm.CHA.toString()};
            JSONArray cg_construction_metric_array = new JSONArray();
            stringMetricsHashMap.values().forEach(value -> {
                value.getCallGraphMetrics().getCallGraphConstructionMetrics().forEach(callGraphConstructionMetrics -> {
                    JSONObject cg_construction_metric = new JSONObject();
                    callGraphAlgorithm[0] = callGraphConstructionMetrics.getCallGraphAlgorithm();
                    cg_construction_metric.put("cg_edges", callGraphConstructionMetrics.getCg_Edges());
                    cg_construction_metric.put("leaks", value.getCallGraphMetrics().getNumberOfLeaks());
                    cg_construction_metric.put("construction_time", callGraphConstructionMetrics.getConstructionTime());
                    cg_construction_metric.put("memory_consumed", callGraphConstructionMetrics.getMemoryConsumed());
                    cg_construction_metric_array.put(cg_construction_metric);
                });
                analysis_metric.put("analysis_time", value.getAnalysisMetrics().getAnalysisTime());
                analysis_metric.put("analysis_memory", value.getAnalysisMetrics().getMemoryConsumed());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("cg_metric", cg_construction_metric_array);
                jsonObject1.put("analysis_metric", analysis_metric);
                assert callGraphAlgorithm[0] != null;
                jsonObject.put(callGraphAlgorithm[0].toString(), jsonObject1);
                jsonObjectArray.put(jsonObject);
            });
        });
        outerJsonObject.put("metrics",jsonObjectArray);
        outerJsonObject.put("ground_truth", ground_truth_for_this_apk);
        return outerJsonObject;
    }

    public static void writeToJsonFile(JSONArray jsonArray, String fileName) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(jsonArray.toString(4));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeDecomposedJsonToCSVFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(new File(EvaluationConfig.getDecomposedJSON_FILE_PATh()));
            String csvFilePath = EvaluationConfig.getDecomposedCSV_FILE_PATh();
            List<Map<String, Object>> records = new ArrayList<>();
            for(JsonNode entry : jsonNode){
                String appName = entry.get("app_name").asText();
                int ground_truth = entry.get("ground_truth").asInt();
                String cg_name = entry.get("cg_name").asText();
                JsonNode cg_metrics = entry.get("cg_metric");
                JsonNode analysis_metric = entry.get("analysis_metric");
                Map record = new HashMap();
                record.put("app_name", appName);
                record.put("ground_truth", ground_truth);
                record.put("cg_name", cg_name);
                record.put("leaks", cg_metrics.get("leaks"));
                record.put("cg_edges", cg_metrics.get("cg_edges"));
                record.put("cg_construction_time_avg", cg_metrics.get("construction_time").asDouble());
                record.put("cg_memory_consumed_avg", cg_metrics.get("memory_consumed").asDouble());
                record.put("analysis_time_avg", analysis_metric.get("analysis_time").asDouble());
                record.put("analysis_memory_consumed_avg", analysis_metric.get("analysis_memory").asDouble());
                records.add(record);
            }
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder()
                    .addColumn("app_name")
                    .addColumn("ground_truth")
                    .addColumn("cg_name")
                    .addColumn("cg_edges")
                    .addColumn("leaks")
                    .addColumn("cg_construction_time_avg")
                    .addColumn("cg_memory_consumed_avg")
                    .addColumn("analysis_time_avg")
                    .addColumn("analysis_memory_consumed_avg");
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(Map.class)
                    .with(csvSchema)
                    .writeValues(new File(csvFilePath))
                    .writeAll(records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToCSVFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(new File(EvaluationConfig.getJSON_FILE_PATh()));
            String csvFilePath = EvaluationConfig.getCSV_FILE_PATh();
            List<Map<String, Object>> records = new ArrayList<>();
            for (JsonNode entry : jsonNode) {
                String appName = entry.get("app_name").asText();
                int ground_truth = entry.get("ground_truth").asInt();
                JsonNode metrics = entry.get("metrics");
                for (JsonNode metric : metrics) {
                    String cgName = metric.fieldNames().next(); // Get the CG name

                    JsonNode cgMetrics = metric.get(cgName).get("cg_metric");
                    JsonNode analysisMetric = metric.get(cgName).get("analysis_metric");

                    for (JsonNode cgMetric : cgMetrics) {
                        Map record = new HashMap();
                        record.put("cg_construction_time", cgMetric.get("construction_time"));
                        record.put("cg_memory_consumed", cgMetric.get("memory_consumed"));
                        record.put("no_of_leaks", cgMetric.get("leaks"));
                        record.put("cg_edges", cgMetric.get("cg_edges"));
                        record.put("ground_truth", ground_truth);
                        record.put("app_name", appName);
                        record.put("cg_name", cgName);
                        record.put("analysis_time", analysisMetric.get("analysis_time").asDouble());
                        record.put("analysis_memory", analysisMetric.get("analysis_memory").asDouble());
                        records.add(record);
                    }
                }
            }
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder()
                    .addColumn("app_name")
                    .addColumn("ground_truth")
                    .addColumn("cg_name")
                    .addColumn("cg_edges")
                    .addColumn("no_of_leaks")
                    .addColumn("cg_construction_time")
                    .addColumn("cg_memory_consumed")
                    .addColumn("analysis_time")
                    .addColumn("analysis_memory");
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(Map.class)
                    .with(csvSchema)
                    .writeValues(new File(csvFilePath))
                    .writeAll(records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void decomposeJsonFile(int ground_truth_for_this_apk) {
        ObjectMapper objectMapper = new ObjectMapper();
        int leaksMax;
        int cgEdgesMax;
        double constructionTimeSum;
        double memoryConsumedSum;
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

                for (JsonNode objNode : metricObjects) {
                    JsonNode cgMetricNode = objNode.path("cg_metric");
                    JsonNode analysisMetricNode = objNode.path("analysis_metric");
                    analysisMemorySum += analysisMetricNode.get("analysis_memory").asDouble();
                    analysisTimeSum += analysisMetricNode.get("analysis_time").asDouble();

                    // Calculate maximum for "leaks" and "cg_edges"
                    for (JsonNode metric : cgMetricNode) {
                        leaksMax = Math.max(leaksMax, metric.get("leaks").asInt());
                        cgEdgesMax = Math.max(cgEdgesMax, metric.get("cg_edges").asInt());

                        constructionTimeSum += metric.get("construction_time").asDouble();
                        memoryConsumedSum += metric.get("memory_consumed").asDouble();
                    }
                }
                // Calculate averages
                // TODO : Discuss with Kadiray about the average of the results.
                double constructionTimeAvg = constructionTimeSum / metricObjects.size();
                double memoryConsumedAvg = memoryConsumedSum / metricObjects.size();
                double analysisMemoryAvg = analysisMemorySum / metricObjects.size();
                double analysisTimeAvg = analysisTimeSum / metricObjects.size();
                createDecomposedJsonObj(leaksMax, cgEdgesMax, constructionTimeAvg, memoryConsumedAvg, analysisMemoryAvg, analysisTimeAvg, metricType, ground_truth_for_this_apk);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDecomposedJsonObj(int leaksMax, int cgEdgesMax, double constructionTimeAvg, double memoryConsumedAvg, double analysisMemoryAvg, double analysisTimeAvg, String metricType, int ground_truth_for_this_apk) {
        JSONObject outerJsonObject = new JSONObject();
        JSONObject analysis_metric = new JSONObject();
        outerJsonObject.put("app_name", EvaluationConfig.getCurrentlyProcessingApkName());
        outerJsonObject.put("cg_name", metricType);
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
        DECOMPOSED_JSON_ARRAY.put(outerJsonObject);
    }

    public static int getGroundTruthLeaks(String apk_name){
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file into JsonNode
        try {
            JsonNode rootNode = objectMapper.readTree(new File(EvaluationConfig.getGroundTruthFile()));
            if (rootNode.isArray()) {
                // Iterate over elements in the array
                for (JsonNode node : rootNode) {
                    // Check if the node has the specified apkName
                    if (node.has("apkName") && node.get("apkName").asText().equals(apk_name)) {
                        // Return the "number_of_Leaks" value for the given apkName
                        return node.get("number_of_Leaks").asInt();
                    }
                }
            }
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
