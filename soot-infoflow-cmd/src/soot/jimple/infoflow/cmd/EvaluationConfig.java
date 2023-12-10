package soot.jimple.infoflow.cmd;

public class EvaluationConfig {
    private static final String GROUND_TRUTH_DROIDBENCH_FILE = "/Users/palaniappanmuthuraman/Documents/Thesis/taintBench_results_for_38.json";

    private static final String APK_DIRECTORY_PATH = "/Users/palaniappanmuthuraman/Documents/Thesis/Evaluation_TaintBench/backflash";


    private static final String JSON_STRING = ".json";
    private static final String CSV_STRING = ".csv";


    private static String CURRENTLY_PROCESSING_APK_NAME = "";

    private static final int number_of_Iterations = 5;

    private static final int k_configuration_for_QILIN = 2;

    public static int getK_configuration_for_QILIN(){
        return k_configuration_for_QILIN;
    }

    public static String getGroundTruthDroidbenchFile(){
        return GROUND_TRUTH_DROIDBENCH_FILE;
    }

    public static String getApkDirectoryPath(){
        return APK_DIRECTORY_PATH;
    }

    public static int getNumber_of_Iterations(){
        return number_of_Iterations;
    }


    public static String getJSON_FILE_PATh() {
        return APK_DIRECTORY_PATH + "/" + CURRENTLY_PROCESSING_APK_NAME + JSON_STRING;
    }

    public static String getCurrentlyProcessingApkName() {
        return CURRENTLY_PROCESSING_APK_NAME;
    }

    public static void setCurrentlyProcessingApkName(String currentlyProcessingApkName) {
        CURRENTLY_PROCESSING_APK_NAME = currentlyProcessingApkName;
    }

    public static String getCSV_FILE_PATh() {
        return APK_DIRECTORY_PATH + "/"+ CURRENTLY_PROCESSING_APK_NAME + CSV_STRING;
    }

}
