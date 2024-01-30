package soot.jimple.infoflow.android;

public class EvaluationConfig {
    private static String GROUND_TRUTH_FILE;

    private static String OUTPUT_DIRECTORY_PATH;

    private static final String CSV_STRING = ".csv";

    private static long memory_consumed;


    private static String CURRENTLY_PROCESSING_APK_NAME = "";

    public static String getGroundTruthFile(){
        return GROUND_TRUTH_FILE;
    }

    public static String getCurrentlyProcessingApkName(){
        return CURRENTLY_PROCESSING_APK_NAME;
    }


    public static void setCurrentlyProcessingApkName(String currentlyProcessingApkName) {
        CURRENTLY_PROCESSING_APK_NAME = currentlyProcessingApkName.replace(".apk", "");
    }

    public static String getCSV_FILE_PATh() {
        return OUTPUT_DIRECTORY_PATH + "/"+ CURRENTLY_PROCESSING_APK_NAME + CSV_STRING;
    }

    public static void set_output_directory_path(String outputDirectoryPath){
        OUTPUT_DIRECTORY_PATH = outputDirectoryPath;
    }

    public static void set_RESULT_JSON_FILE(String resultJsonFile){
        GROUND_TRUTH_FILE = resultJsonFile;
    }

    public static long getMemory_consumed() {
        return memory_consumed;
    }

    public static void setMemory_consumed(long memory_consumed) {
        EvaluationConfig.memory_consumed = memory_consumed;
    }
}
