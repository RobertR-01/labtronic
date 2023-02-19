package com.app.labtronic.data.valuation;

import javafx.beans.property.SimpleStringProperty;

public class ValuationData {
    private final SimpleStringProperty range;
    private final SimpleStringProperty unit;
    private final SimpleStringProperty numberOfPoints;
    private final SimpleStringProperty type;
    private final SimpleStringProperty cost;

    public ValuationData(String range, String unit, String numberOfPoints, String type, String cost) {
        this.range = new SimpleStringProperty(range);
        this.unit = new SimpleStringProperty(unit);
        this.numberOfPoints = new SimpleStringProperty(numberOfPoints);
        this.type = new SimpleStringProperty(type);
        this.cost = new SimpleStringProperty(cost);
    }

    public ValuationData() {
        this(null, null, null, null, null);
    }

    // TODO: add validation for setters?

    public String getRange() {
        return range.get();
    }

    public SimpleStringProperty rangeProperty() {
        return range;
    }

    public void setRange(String range) {
        this.range.set(range);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public String getNumberOfPoints() {
        return numberOfPoints.get();
    }

    public SimpleStringProperty numberOfPointsProperty() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(String numberOfPoints) {
        this.numberOfPoints.set(numberOfPoints);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getCost() {
        return cost.get();
    }

    public SimpleStringProperty costProperty() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost.set(cost);
    }
}
