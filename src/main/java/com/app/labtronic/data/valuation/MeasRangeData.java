package com.app.labtronic.data.valuation;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;

public class MeasRangeData {
    private double range;
    private String unit;
    private String rangeType;
    private ValuationDlgController.Function functionType;
    private double[] points;

    public MeasRangeData(double range, String rangeType, String unit, ValuationDlgController.Function functionType,
                         double[] points) {
        // TODO: validation
        this.range = range;
        this.rangeType = rangeType;
        this.unit = unit;
        this.functionType = functionType;
        this.points = points;
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

    public double[] getPoints() {
        return points;
    }
}
