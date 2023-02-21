package com.app.labtronic.data.valuation;

import javafx.beans.property.SimpleStringProperty;

public class MeasPointData {
    private SimpleStringProperty pointValueProperty;
    private SimpleStringProperty unitProperty;

    public MeasPointData(String pointValue, String unit) {
        pointValueProperty = new SimpleStringProperty();
        unitProperty = new SimpleStringProperty();
        if (pointValue != null) {
            this.pointValueProperty.set(pointValue);
        }
        if (unit != null) {
            this.unitProperty.set(unit);
        }
    }

    public String getPointValueProperty() {
        return pointValueProperty.get();
    }

    public SimpleStringProperty pointValuePropertyProperty() {
        return pointValueProperty;
    }

    public void setPointValueProperty(String pointValueProperty) {
        this.pointValueProperty.set(pointValueProperty);
    }

    public String getUnitProperty() {
        return unitProperty.get();
    }

    public SimpleStringProperty unitPropertyProperty() {
        return unitProperty;
    }

    public void setUnitProperty(String unitProperty) {
        this.unitProperty.set(unitProperty);
    }
}
