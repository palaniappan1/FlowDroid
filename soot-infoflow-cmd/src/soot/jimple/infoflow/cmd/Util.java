package soot.jimple.infoflow.cmd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import metrics.CallGraphConstructionMetrics;
import soot.jimple.infoflow.android.util.AnalysisMetrics;
import soot.jimple.infoflow.android.util.AppAnalysisResult;
import soot.jimple.infoflow.android.util.CallGraphMetrics;
import soot.jimple.infoflow.android.util.Metrics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.HashMap;

import static soot.jimple.infoflow.android.EvaluationConfig.*;

public class Util {
    public static void writeToCsv(AppAnalysisResult appAnalysisResult, String csv_file_path){
        int getGroundTruthLeaks = getGroundTruthLeaks(appAnalysisResult.getAPP_NAME() + ".apk");
        String app_name = appAnalysisResult.getAPP_NAME();
        String cg_name = appAnalysisResult.getCg_name();
        Metrics metrics = appAnalysisResult.getMetrics();
        CallGraphMetrics callGraphMetrics = metrics.getCallGraphMetrics();
        int no_of_leaks = callGraphMetrics.getNumberOfLeaks();
        float cg_construction_time_sum = 0;
        int cg_edges = -1;
        for (CallGraphConstructionMetrics callGraphConstructionMetric : callGraphMetrics.getCallGraphConstructionMetrics()) {
            cg_construction_time_sum += callGraphConstructionMetric.getConstructionTime();
            if(callGraphConstructionMetric.getCg_Edges() > cg_edges){
                cg_edges = callGraphConstructionMetric.getCg_Edges();
            }
        }
        AnalysisMetrics analysisMetrics = metrics.getAnalysisMetrics();
        HashMap<String, Object> record = new HashMap<>();
        record.put("cg_construction_time", formatDecimalValues(cg_construction_time_sum));
        record.put("no_of_leaks", no_of_leaks);
        record.put("cg_edges", cg_edges);
        record.put("ground_truth", getGroundTruthLeaks);
        record.put("app_name", app_name);
        record.put("cg_name", cg_name);
        record.put("analysis_time", formatDecimalValues(analysisMetrics.getAnalysisTime()));
        record.put("analysis_memory", formatDecimalValues(analysisMetrics.getMemoryConsumed()));
        Path path = Paths.get(csv_file_path);
        if(Files.exists(path)){
            try {
                String dataLine = String.join(",", record.values().stream().map(Object::toString).toArray(String[]::new));
                Files.write(path,
                        (dataLine + System.lineSeparator()).getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                System.out.println(path);
                new File(path.toString()).createNewFile();
                String header = String.join(",", record.keySet());

                Files.write(path,
                        (header + System.lineSeparator()).getBytes(),
                        StandardOpenOption.WRITE);
                String dataLine = String.join(",", record.values().stream().map(Object::toString).toArray(String[]::new));
                Files.write(path,
                        (dataLine + System.lineSeparator()).getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String formatDecimalValues(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    public static int getGroundTruthLeaks(String apk_name){
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file into JsonNode
        try {
            JsonNode rootNode = objectMapper.readTree(new File(getGroundTruthFile()));
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
