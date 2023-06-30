package com.app.labtronic.data.budgets;

import java.util.ArrayList;
import java.util.List;

public class UncertaintyData {
    private UncertaintyComponent repeatability;
    private UncertaintyComponent refStdUncertainty;
    private UncertaintyComponent resolution;
    private UncertaintyComponent refStdStability;

    // reference standard uncertainty for intermediate measurement points is predicted using linear regression;
    // the budget includes the uncertainty component related to the standard error of the predicted y-value for each x
    // in the regression;
    // the standard error is a measure of the amount of error in the prediction of y for an individual x;
    private UncertaintyComponent predictionError;

    private List<String> dutReadings;
    private double observedValueMean;
    private double trueValue;
    private double measError;
    private double combinedUncertainty;
    private double expandedUncertainty;
    private double cmc;
    private double cmcMaxUncertainty;
    private double finalUncertainty;
    private final double COVERAGE_FACTOR = 2;

    private List<UncertaintyComponent> components;

    public UncertaintyData() {
        this.repeatability = new UncertaintyComponent("X̄", UncertaintyComponent.ProbabilityDistribution.N);
        this.refStdUncertainty = new UncertaintyComponent("T", UncertaintyComponent.ProbabilityDistribution.N);
        this.resolution = new UncertaintyComponent("δX̄", UncertaintyComponent.ProbabilityDistribution.R);
        this.refStdStability = new UncertaintyComponent("δT", UncertaintyComponent.ProbabilityDistribution.R);
        this.predictionError = new UncertaintyComponent("δXreg", UncertaintyComponent.ProbabilityDistribution.N);
        this.components = List.of(repeatability, refStdUncertainty, resolution, refStdStability, predictionError);

        this.dutReadings = new ArrayList<>();
    }

    public List<UncertaintyComponent> getComponents() {
        return components;
    }

    // validation of the array contents done in BudgetsController
    public void setDutReadings(List<String> values) {
        if (values != null && values.size() == 10) {
            dutReadings = new ArrayList<>(values);
        }
    }

    public List<String> getDutReadings() {
        return dutReadings;
    }
}
