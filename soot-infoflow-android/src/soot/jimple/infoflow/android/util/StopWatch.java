package soot.jimple.infoflow.android.util;

public class StopWatch {
    private final String name;
    private long elapsedTime;

    private long startTime;
    private boolean inCounting;

    public static StopWatch newAndStart(final String name) {
        StopWatch stopwatch = new StopWatch(name);
        stopwatch.start();
        return stopwatch;
    }

    private StopWatch(final String name) {
        this.elapsedTime = 0L;
        this.inCounting = false;
        this.name = name;
    }

    private void start() {
        if (!this.inCounting) {
            this.inCounting = true;
            this.startTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        if (this.inCounting) {
            this.elapsedTime += System.currentTimeMillis() - this.startTime;
            this.inCounting = false;
        }
    }

    public float elapsed() {
        return this.elapsedTime / 1000.0f;
    }

    public void reset() {
        this.elapsedTime = 0L;
        this.inCounting = false;
    }

    public void restart() {
        reset();
        start();
    }

    @Override
    public String toString() {
        return String.format("%s elapsed time: %.2fs", this.name, this.elapsed());
    }
}
