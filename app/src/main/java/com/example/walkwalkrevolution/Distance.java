package com.example.walkwalkrevolution;

public class Distance {
    private static final double strideRatio = 0.413;
    private static final int feetPerMile = 5280;
    private static final int inchesPerFeet = 12;

    private double strideLength;


    public Distance(int height) {
        // strideLength in inches = (height in inches) * (strideRatio)
        // strideLength in feet = strideLength in inches / 12
        this.strideLength = height * strideRatio / inchesPerFeet;
    }
    public double calculateDistance(long stepCount) {return stepCount * strideLength / feetPerMile;}
}