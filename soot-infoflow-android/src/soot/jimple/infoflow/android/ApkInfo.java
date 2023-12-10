package soot.jimple.infoflow.android;

public class ApkInfo {

    private static String apkName;
    private static float initializeSootTime;

    public static String getApkName() {
        return apkName;
    }

    public static void setApkName(String apkName) {
        ApkInfo.apkName = apkName;
    }

    public static float getInitializeSootTime() {
        return initializeSootTime;
    }

    public static void setInitializeSootTime(float initializeSootTime) {
        ApkInfo.initializeSootTime = initializeSootTime;
    }
}
