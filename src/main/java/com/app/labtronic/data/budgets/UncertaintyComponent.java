package com.app.labtronic.data.budgets;

public class UncertaintyComponent {
    private String symbol;
    private double estimatedValue;
    private double stdUncertainty;
    private ProbabilityDistribution probabilityDistribution;
    private double sensitivityCoefficient;
    private double convertedUncertainty; // taking sensitivity coefficient into account
    private double degreesOfFreedom;
    private double significance;

    private enum ProbabilityDistribution {
        N,
        R
    }
}
