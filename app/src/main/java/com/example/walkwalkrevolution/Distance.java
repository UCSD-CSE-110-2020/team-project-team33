package com.example.walkwalkrevolution;

public class Distance {
    private static final double STRIDE_RATIO = 0.413;
    private static final int FEET_PER_MILE = 5280;
    private static final int INCHES_PER_FOOT = 12;

    private double strideLength;


    public Distance(int height) {
        // strideLength in inches = (height in inches) * (strideRatio)
        // strideLength in feet = strideLength in inches / 12
        this.strideLength = (height * STRIDE_RATIO) / INCHES_PER_FOOT;
    }
    public double calculateDistance(long stepCount) {return Math.abs(stepCount * strideLength / FEET_PER_MILE);}
}
