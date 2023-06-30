package com.app.labtronic.data.valuation;

import com.app.labtronic.data.budgets.UncertaintyData;
import javafx.beans.property.SimpleStringProperty;

public class MeasPointData {
    private SimpleStringProperty pointValueProperty;
    private SimpleStringProperty unitProperty;
    private UncertaintyData uncertaintyData;

    public MeasPointData(String pointValue, String unit) {
        this.pointValueProperty = new SimpleStringProperty();
        this.unitProperty = new SimpleStringProperty();
        if (pointValue != null) {
            this.pointValueProperty.set(pointValue);
        }
        if (unit != null) {
            this.unitProperty.set(unit);
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
}
