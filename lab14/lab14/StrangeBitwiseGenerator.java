package lab14;

import lab14lib.Generator;

public class SawToothGenerator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    public double next() {
        state += 1;
        int weirdState = state & (state >>> 3) % period;
        return normalize(weirdState);
    }

    private double normalize(int x) {
        return (double)x * 2 / (period - 1) - 1;
    }
}
