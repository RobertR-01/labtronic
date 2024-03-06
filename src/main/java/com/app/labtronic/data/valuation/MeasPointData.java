package com.app.labtronic.data.valuation;

import com.app.labtronic.data.budgets.UncertaintyData;
import javafx.beans.property.SimpleStringProperty;

public class MeasPointData {
    private SimpleStringProperty pointValueProperty;
    private SimpleStringProperty unitProperty;
    private UncertaintyData uncertaintyData;
    private MeasRangeData range;

    public MeasPointData(String pointValue, String unit) {
        this.pointValueProperty = new SimpleStringProperty();
        this.unitProperty = new SimpleStringProperty();
        if (pointValue != null) {
            this.pointValueProperty.set(pointValue.trim());
        }
        if (unit != null) {
            this.unitProperty.set(unit.trim());
        }
        this.uncertaintyData = new UncertaintyData();
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

    public UncertaintyData getUncertaintyData() {
        return uncertaintyData;
    }

    public void setUncertaintyData(UncertaintyData uncertaintyData) {
        this.uncertaintyData = uncertaintyData;
    }

    public MeasRangeData getRange() {
        return range;
    }

    public void setRange(MeasRangeData range) {
        if (range != null) {
            this.range = range;
        }
    }

    @Override
    public boolean equals(Object point) {
        boolean result = false;
        if (point instanceof MeasPointData) {
            try {
                double oldValue = Double.parseDouble(this.getPointValueProperty());
                double newValue = Double.parseDouble(((MeasPointData) point).getPointValueProperty());
                if (oldValue == newValue && this.getUnitProperty().equals(((MeasPointData) point).getUnitProperty())) {
                    result = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("MeasPointData -> equals() error");
                e.printStackTrace();
            }
        }

        return result;
    }
}
