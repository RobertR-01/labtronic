package com.app.labtronic.data.valuation;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;

import java.util.ArrayList;

public class MeasRangeData {
    private double range;
    private String unit;
    private String rangeType;
    private ValuationDlgController.Function functionType;
    private ArrayList<Double> points;
    private int numberOfPoints;
    private double cost;
    private int resolution;

    public MeasRangeData(double range, String rangeType, String unit, ValuationDlgController.Function functionType,
                         ArrayList<Double> points, int resolution) {
        // TODO: validation
        this.range = range;
        this.rangeType = rangeType;
        this.unit = unit;
        this.functionType = functionType;
        this.points = points;
        this.numberOfPoints = points.size();
        this.cost = 0;
        this.resolution = resolution;
    }

    public double getRange() {
        return range;
    }

    public String getRangeType() {
        return rangeType;
    }

    public String getUnit() {
        return unit;
    }

    public ValuationDlgController.Function getFunctionType() {
        return functionType;
    }

    public ArrayList<Double> getPoints() {
        return new ArrayList<>(points);
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public double getCost() {
        return cost;
    }

    public int getResolution() {
        return resolution;
    }

    // TODO: validation
    public double calculateCost() {
        int numberOfExtraPoints = 0;
        int number0fBasePoints = 0;
        double extraPointCost = 0;
        double baseRangeCost = 0;
        switch (resolution) {
            case 4:
                if (rangeType.equalsIgnoreCase("first")) {
                    baseRangeCost = 150;
                    number0fBasePoints = 5;
                } else {
                    baseRangeCost = 20;
                    number0fBasePoints = 4;
                }
                extraPointCost = 10;
                break;
            case 5:
                if (rangeType.equalsIgnoreCase("first")) {
                    baseRangeCost = 225;
                    number0fBasePoints = 8;
                } else {
                    baseRangeCost = 30;
                    number0fBasePoints = 5;
                }
                extraPointCost = 15;
                break;
            case 7:
                if (rangeType.equalsIgnoreCase("first")) {
                    baseRangeCost = 300;
                    number0fBasePoints = 8;
                } else {
                    baseRangeCost = 40;
                    number0fBasePoints = 5;
                }
                extraPointCost = 20;
                break;
        }
        if (numberOfPoints > number0fBasePoints) {
            numberOfExtraPoints = numberOfPoints - number0fBasePoints;
        }
        cost = baseRangeCost + numberOfExtraPoints * extraPointCost;
        return cost;
    }
}
