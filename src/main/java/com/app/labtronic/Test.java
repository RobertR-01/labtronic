package com.app.labtronic;

import com.app.labtronic.utility.UnitConverter;

public class Test {
    public static void main(String[] args) {
//        SimpleDoubleProperty doubleProperty = new SimpleDoubleProperty(10);
//        NumberBinding binding = doubleProperty.add(1);
//        doubleProperty.set(binding.doubleValue());
//        System.out.println(doubleProperty.get());
//        System.out.println(binding.getValue());

        UnitConverter.MetricPrefixData metricPrefixData = UnitConverter.getMetricPrefix("");
        if (metricPrefixData == null) {
            System.out.println("----------------------------------------------");
            System.out.println("| Test -> main() -> metricPrefixData == null |");
            System.out.println("----------------------------------------------");
        } else {
            System.out.println(metricPrefixData.getPrefix());
            System.out.println(metricPrefixData.getMultiplier());
        }
        System.out.println("*fin*");
    }
}
