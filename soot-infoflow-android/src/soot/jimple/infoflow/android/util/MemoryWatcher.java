package soot.jimple.infoflow.android.util;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Timer;
import java.util.TimerTask;

public class MemoryWatcher extends Timer {
    private final long pid;
    private final String name;
    private final long[] maxMemory;
    private static volatile MemoryWatcher instance;

    public MemoryWatcher(String name) {
        this.pid = getProcessId();
        this.name = name;
        this.maxMemory = new long[1];
    }

    public static MemoryWatcher getInstance(String name) {
        if (instance == null) {
            synchronized (MemoryWatcher.class) {
                if (instance == null) {
                    instance = new MemoryWatcher(name);
                }
            }
        }
        return instance;
    }

    private static long getProcessId() {
        // This approach uses Java's internal classes, so it is platform-dependent
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    private TimerTask task;

    public void start() {
        this.maxMemory[0] = 0;
        task = new TimerTask() {
            @Override
            public void run() {
                SystemInfo si = new SystemInfo();
                OperatingSystem os = si.getOperatingSystem();
                OSProcess process = os.getProcess((int) pid);
                long mem = process.getResidentSetSize();
                if (mem > maxMemory[0]) {
                    maxMemory[0] = mem;
                }
            }
        };
        schedule(task, 0, 100);
    }

    public void stop() {
        task.cancel();
        this.cancel();
    }

    public double getCurrentMemoryUsageInMegaByte() {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        OSProcess process = os.getProcess((int) pid);
        return process.getResidentSetSize() / (1024.0 * 1024.0);
    }

    public double inKiloByte() {
        return maxMemory[0] / 1024.0;
    }

    public double inMegaByte() {
        return maxMemory[0] / (1024.0 * 1024.0);
    }

    public double inGigaByte() {
        return maxMemory[0] / (1024.0 * 1024.0 * 1024.0);
    }

    @Override
    public String toString() {
        return String.format("%s consumed memory: %.2f MB", this.name, this.inMegaByte());
    }
}
