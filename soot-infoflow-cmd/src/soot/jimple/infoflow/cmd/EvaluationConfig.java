package soot.jimple.infoflow.cmd;

public class EvaluationConfig {
    private static String GROUND_TRUTH_FILE;

    private static String APK_DIRECTORY_PATH;

    private static String source_sink_file;

    private static String platform_directory;


    private static final String JSON_STRING = ".json";
    private static final String CSV_STRING = ".csv";

    private static final String DECOMPOSED = "decomposed";


    private static String CURRENTLY_PROCESSING_APK_NAME = "";

    private static int number_of_Iterations;

    private static int k_configuration_for_QILIN;

    public static void setK_configuration_for_QILIN(int k){
        k_configuration_for_QILIN = k;
    }

    public static int getK_configuration_for_QILIN(){
        return k_configuration_for_QILIN;
    }

    public static String getGroundTruthFile(){
        return GROUND_TRUTH_FILE;
    }

    public static String getApkDirectoryPath(){
        return APK_DIRECTORY_PATH;
    }

    public static void setNumber_of_Iterations(int numberOfIterations){
        number_of_Iterations = numberOfIterations;
    }

    public static int getNumber_of_Iterations(){
        return number_of_Iterations;
    }


    public static String getJSON_FILE_PATh() {
        return APK_DIRECTORY_PATH + "/" + CURRENTLY_PROCESSING_APK_NAME + JSON_STRING;
    }

    public static String getDecomposedJSON_FILE_PATh(){
        return APK_DIRECTORY_PATH + "/" + CURRENTLY_PROCESSING_APK_NAME + DECOMPOSED + JSON_STRING;
    }

    public static String getDecomposedCSV_FILE_PATh(){
        return APK_DIRECTORY_PATH + "/" + CURRENTLY_PROCESSING_APK_NAME + DECOMPOSED + CSV_STRING;
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

    public static void set_APK_DIRECTORY_PATH(String apk_directory_path){
        APK_DIRECTORY_PATH = apk_directory_path;
    }

    public static String get_APK_DIRECTORY_PATH(){
        return APK_DIRECTORY_PATH;
    }

    public static void set_RESULT_JSON_FILE(String resultJsonFile){
        GROUND_TRUTH_FILE = resultJsonFile;
    }

    public static String get_GROUND_TRUTH_FILE(){
        return GROUND_TRUTH_FILE;
    }

    public static String getSource_sink_file() {
        return source_sink_file;
    }

    public static void setSource_sink_file(String source_sink_file) {
        EvaluationConfig.source_sink_file = source_sink_file;
    }

    public static String getPlatform_directory() {
        return platform_directory;
    }

    public static void setPlatform_directory(String platform_directory) {
        EvaluationConfig.platform_directory = platform_directory;
    }
}
