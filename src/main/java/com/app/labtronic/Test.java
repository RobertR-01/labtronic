package com.app.labtronic;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;
import com.app.labtronic.utility.MeasPointValidator;

public class Test {
    public static void main(String[] args) {
//        SimpleDoubleProperty doubleProperty = new SimpleDoubleProperty(10);
//        NumberBinding binding = doubleProperty.add(1);
//        doubleProperty.set(binding.doubleValue());
//        System.out.println(doubleProperty.get());
//        System.out.println(binding.getValue());

//        UnitConverter.MetricPrefixData metricPrefixData = UnitConverter.getMetricPrefix("");
//        if (metricPrefixData == null) {
//            System.out.println("----------------------------------------------");
//            System.out.println("| Test -> main() -> metricPrefixData == null |");
//            System.out.println("----------------------------------------------");
//        } else {
//            System.out.println(metricPrefixData.getPrefix());
//            System.out.println(metricPrefixData.getMultiplier());
//        }
//        System.out.println("*fin*");

        // μ
//        String unit = "kA";
//        double value = 200;
//        double valueInBaseUnit = UnitConverter.convertValueToBaseUnit(value, unit);
//        String baseUnit = UnitConverter.getBaseUnit(unit);
//        System.out.println(value + " " + unit + " = " + valueInBaseUnit + " " + baseUnit);

        double value = 0.000001;
        String metricUnit = "kV";
        ValuationDlgController.Function function = ValuationDlgController.Function.IDC;
        boolean result = MeasPointValidator.checkPoint(value, metricUnit, function);
        String string = (result) ? " OK." : " not OK.";
        System.out.println("Point: " + value + " " + metricUnit + " of " + function.name() + " function is " + string);
    }
}
