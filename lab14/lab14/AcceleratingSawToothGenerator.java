package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double growth;
    private int state;

    public AcceleratingSawToothGenerator r(int period, double growth) {
        this.period = period;
        this.growth = growth;
        state = 0;
    }

    @Override
    public double next() {
        state += 1;
        double res = normalize(state % period);
        if (res == 1) period = (int)(period * growth);
        return res;
    }

    private double normalize(int x) {
        return (double)x * 2 / (period - 1) - 1;
    }
}
