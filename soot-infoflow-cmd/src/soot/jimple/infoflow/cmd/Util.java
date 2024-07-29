package soot.jimple.infoflow.cmd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import metrics.CallGraphConstructionMetrics;
import qilin.util.Pair;
import soot.jimple.infoflow.android.EvaluationConfig;
import soot.jimple.infoflow.android.util.AnalysisMetrics;
import soot.jimple.infoflow.android.util.AppAnalysisResult;
import soot.jimple.infoflow.android.util.CallGraphMetrics;
import soot.jimple.infoflow.android.util.Metrics;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static soot.jimple.infoflow.android.EvaluationConfig.*;

public class Util {

    public static int expectedFlow = 0;

    public static int unExpectedFlow = 0;
    public static void writeToCsv(AppAnalysisResult appAnalysisResult, String csv_file_path){
        checkForGroundTruth();
        writeJsonResult();
        int getGroundTruthLeaks = getGroundTruthLeaks(appAnalysisResult.getAPP_NAME() + ".apk");
        String app_name = appAnalysisResult.getAPP_NAME();
        String cg_name = appAnalysisResult.getCg_name();
        Metrics metrics = appAnalysisResult.getMetrics();
        int numOfReachableMethods = appAnalysisResult.getNum_of_reachable_methods();
        long numOfMethodsPropagated = appAnalysisResult.getNum_of_methods_propagated();
        long numOfEdgesPropagated = appAnalysisResult.getNum_edges_propagated();
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
        record.put("reachable_methods", numOfReachableMethods);
        record.put("num_of_methods_propagated", numOfMethodsPropagated);
        record.put("num_edges_propagated", numOfEdgesPropagated);
        record.put("analysis_time", formatDecimalValues(analysisMetrics.getAnalysisTime()));
        record.put("analysis_memory", formatDecimalValues(analysisMetrics.getMemoryConsumed()));
        record.put("expected_leaks", expectedFlow);
        record.put("unexpected_leaks", unExpectedFlow);
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

    private static void writeJsonResult() {
        Pair[] pairsArray = getSourceSinkList().toArray(new Pair[0]);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(pairsArray);

        // Write JSON string to file
        try (FileWriter writer = new FileWriter("output.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkForGroundTruth(){
        JsonObject json;
        try {
            JsonParser parser = new JsonParser();
            json = parser.parse(new FileReader(getGroundTruthSourceSinkFile())).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Parse the JSON into expected and unexpected flows
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExpectedFlow[] expectedFlows = gson.fromJson(json.getAsJsonArray("expected"), ExpectedFlow[].class);
        ExpectedFlow[] unExpectedFlows = gson.fromJson(json.getAsJsonArray("unExpected"), ExpectedFlow[].class);

        ArrayList<Pair<String, String>> sourceSinkList = EvaluationConfig.getSourceSinkList();
        int number_of_iterations = 0;

        for (Pair<String, String> entry : sourceSinkList) {
            // Iterate through the expected flows
            number_of_iterations++;
            String source = entry.getFirst().replace("$", "");
            String sink = entry.getSecond().replace("$", "");
            for (ExpectedFlow flow : expectedFlows) {
                String flowSource = flow.getSource().replace("$", "");
                String flowSink = flow.getSink().replace("$", "");
                if (source.equals(flowSource) && sink.equals(flowSink)) {
                    expectedFlow++;
                    break;
                }
            }
            for(ExpectedFlow flow : unExpectedFlows){
                String flowSource = flow.getSource().replace("$", "");
                String flowSink = flow.getSink().replace("$", "");
                if (source.equals(flowSource) && sink.equals(flowSink)) {
                    unExpectedFlow++;
                    break;
                }
            }
        }

        System.out.println("Expected Flows: " + expectedFlow);
        System.out.println("Unexpected Flows: " + unExpectedFlow);
    }



    static class ExpectedFlow {
        private String source;
        private String sink;

        public String getSource() {
            return source;
        }

        public String getSink() {
            return sink;
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
