package com.app.labtronic.utility;

public class UnitConverter {
    private UnitConverter() {

    }

    public static ConversionResult convertToBaseUnit(double value, char metricPrefix) {
        double valueInBaseUnit = value;
        double multiplier;
        switch (metricPrefix) {
            case 'μ':
                multiplier = 0.000001;
                break;
            case 'm':
                multiplier = 0.001;
                break;
            case 'k':
                multiplier = 1000;
                break;
            case 'M':
                multiplier = 1000000;
                break;
            default:
                System.out.println("UnitConverter -> convertToBaseUnit() -> invalid metric prefix.");
                return null;
        }
        valueInBaseUnit *= multiplier;

        return new ConversionResult(valueInBaseUnit);
    }

    public static class ConversionResult {
        private final double value;

        private ConversionResult(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }
}
