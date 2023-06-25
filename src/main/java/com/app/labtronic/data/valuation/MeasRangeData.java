package com.app.labtronic.data.valuation;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class MeasRangeData {
    private double range;
    private String unit;
    private String rangeType;
    private ValuationDlgController.Function functionType;
    private ArrayList<MeasPointData> points;
    private int numberOfPoints;
    private double cost;
    private int resolutionCategory;
    private double rangeBaseUnitValue;
    private String frequency;
    private int resolution;

    // for the TableView in ValuationController
    private SimpleStringProperty rangeProperty;
    private SimpleStringProperty unitProperty;
    private SimpleStringProperty numberOfPointsProperty;
    private SimpleStringProperty rangeTypeProperty;
    private SimpleStringProperty costProperty;

    public MeasRangeData(double range, String rangeType, String unit, ValuationDlgController.Function functionType,
                         ArrayList<MeasPointData> points, int resolutionCategory, int resolution) {
        // TODO: validation
        this.range = range;
        this.rangeType = rangeType;
        this.unit = unit;
        this.functionType = functionType;
        this.points = points;
        this.numberOfPoints = points.size();
        this.resolutionCategory = resolutionCategory;
        this.cost = 0;
        this.rangeBaseUnitValue = calculateRangeBaseUnitValue();
        this.frequency = null;
        this.resolution = resolution;
    }

    public double getRange() {
        return range;
    }

    public double getRangeBaseUnitValue() {
        return rangeBaseUnitValue;
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

    public ArrayList<MeasPointData> getPoints() {
        return new ArrayList<>(points);
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public double getCost() {
        return cost;
    }

    public int getResolutionCategory() {
        return resolutionCategory;
    }

    public String getRangeProperty() {
        return rangeProperty.get();
    }

    public SimpleStringProperty rangePropertyProperty() {
        return rangeProperty;
    }

    public String getUnitProperty() {
        return unitProperty.get();
    }

    public SimpleStringProperty unitPropertyProperty() {
        return unitProperty;
    }

    public String getNumberOfPointsProperty() {
        return numberOfPointsProperty.get();
    }

    public SimpleStringProperty numberOfPointsPropertyProperty() {
        return numberOfPointsProperty;
    }

    public String getRangeTypeProperty() {
        return rangeTypeProperty.get();
    }

    public SimpleStringProperty rangeTypePropertyProperty() {
        return rangeTypeProperty;
    }

    public String getCostProperty() {
        return costProperty.get();
    }

    public SimpleStringProperty costPropertyProperty() {
        return costProperty;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public void setFunctionType(ValuationDlgController.Function functionType) {
        this.functionType = functionType;
    }

    public void setPoints(ArrayList<MeasPointData> points) {
        this.points = points;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setResolutionCategory(int resolutionCategory) {
        this.resolutionCategory = resolutionCategory;
    }

    public String getFrequency() {
        return frequency;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public void setFrequency(String frequency) {
        if ((functionType.toString().equals("VAC") || functionType.toString().equals("IAC")) &&frequency != null
                && !frequency.isBlank()) {
            this.frequency = frequency;
        } else {
            System.out.println("MeasRangeData -> setFrequency() -> invalid frequency string.");
        }
    }

    // range value converted to the corresponding base SI unit
    private double calculateRangeBaseUnitValue() {
        double multiplier = 0;
        switch (unit) {
            case "µV", "µA", "µΩ" -> multiplier = 0.000001;
            case "mV", "mA", "mΩ" -> multiplier = 0.001;
            case "V", "A", "Ω" -> multiplier = 1;
            case "kV", "kA", "kΩ" -> multiplier = 1000;
            case "MV", "MA", "MΩ" -> multiplier = 1000000;
        }
        return range * multiplier;
    }

    // TODO: validation
    public double calculateCost() {
        int numberOfExtraPoints = 0;
        int number0fBasePoints = 0;
        double extraPointCost = 0;
        double baseRangeCost = 0;
        switch (resolutionCategory) {
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

    public void initializeProperties() {
        rangeProperty = new SimpleStringProperty(String.valueOf(range));
        unitProperty = new SimpleStringProperty(unit);
        numberOfPointsProperty = new SimpleStringProperty(String.valueOf(numberOfPoints));
        rangeTypeProperty = new SimpleStringProperty(rangeType);
        costProperty = new SimpleStringProperty(String.valueOf(cost));
    }
}
