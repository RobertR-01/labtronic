package com.app.labtronic.data.budgets;

import javafx.beans.property.SimpleStringProperty;

public class UncertaintyComponent {
    private SimpleStringProperty symbol;
    private SimpleStringProperty estimatedValue;
    private SimpleStringProperty unit;
    private SimpleStringProperty stdUncertainty;
    private SimpleStringProperty probabilityDistribution;
    private SimpleStringProperty sensitivityCoefficient;
    private SimpleStringProperty convertedUncertainty; // taking sensitivity coefficient into account
    private SimpleStringProperty degreesOfFreedom;
    private SimpleStringProperty significance;

    public UncertaintyComponent() {
        this.symbol = new SimpleStringProperty();
        this.estimatedValue = new SimpleStringProperty();
        this.unit = new SimpleStringProperty();
        this.stdUncertainty = new SimpleStringProperty();
        this.probabilityDistribution = new SimpleStringProperty();
        this.sensitivityCoefficient = new SimpleStringProperty();
        this.convertedUncertainty = new SimpleStringProperty();
        this.degreesOfFreedom = new SimpleStringProperty();
        this.significance = new SimpleStringProperty();
    }

    public UncertaintyComponent(String symbol, ProbabilityDistribution probabilityDistribution) {
        this();
        if (symbol != null && !symbol.isBlank()) {
            this.symbol.set(symbol);
        } else {
            this.symbol.set("null");
            System.out.println("UncertaintyComponent -> constructor call related error - invalid symbol string.");
        }
        this.probabilityDistribution.set(String.valueOf(probabilityDistribution));
    }

    public UncertaintyComponent(String symbol, double estimatedValue, String unit, double stdUncertainty,
                                ProbabilityDistribution probabilityDistribution, double sensitivityCoefficient,
                                double degreesOfFreedom) {
        this(symbol, probabilityDistribution);
        this.estimatedValue.set(String.valueOf(estimatedValue));
        if (unit != null && !unit.isBlank()) {
            this.unit.set(unit);
        } else {
            this.unit.set("null");
            System.out.println("UncertaintyComponent -> constructor call related error - invalid unit string.");
        }
        if (stdUncertainty > 0) {
            this.stdUncertainty.set(String.valueOf(stdUncertainty));
        } else {
            this.stdUncertainty.set(String.valueOf(-1));
            System.out.println("UncertaintyComponent -> constructor call related error - invalid stdUncertainty " +
                    "value.");
        }
        this.sensitivityCoefficient.set(String.valueOf(sensitivityCoefficient));
        this.convertedUncertainty.set(String.valueOf(0));
        this.degreesOfFreedom.set(String.valueOf(degreesOfFreedom));
        this.significance.set(String.valueOf(0));
    }

    public void calculateConvertedU() {
        double coefficient = Double.parseDouble(sensitivityCoefficient.get());
        double uncertainty = Double.parseDouble(stdUncertainty.get());
        if (coefficient != 0 && uncertainty != 0) {
            convertedUncertainty.set(String.valueOf(coefficient * uncertainty));
        }
    }

    public enum ProbabilityDistribution {
        N,
        R
    }
}
