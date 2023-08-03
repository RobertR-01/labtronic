package com.app.labtronic.utility;

import java.util.HashMap;
import java.util.Map;

public class UnitConverter {
    private static final Map<String, Double> METRIC_PREFIX_MAP = new HashMap<>() {{
        put("Q", 1000000000000000000000000000000D); // quetta
        put("R", 1000000000000000000000000000D); // ronna
        put("Y", 1000000000000000000000000D); // yotta
        put("Z", 1000000000000000000000D); // zetta
        put("E", 1000000000000000000D); // exa
        put("P", 1000000000000000D); // peta
        put("T", 1000000000000D); // tera
        put("G", 1000000000D); // giga
        put("M", 1000000D); // mega
        put("k", 1000D); // kilo
        put("h", 100D); // hecto
        put("da", 10D); // deca
        put("d", 0.1); // deci
        put("c", 0.01); // centi
        put("m", 0.001); // milli
        put("μ", 0.000001); // micro
        put("n", 0.000000001); // nano
        put("p", 0.000000000001); // pico
        put("f", 0.000000000000001); // femto
        put("a", 0.000000000000000001); // atto
        put("z", 0.000000000000000000001); // zepto
        put("y", 0.000000000000000000000001); // yocto
        put("r", 0.000000000000000000000000001); // ronto
        put("q", 0.000000000000000000000000000001); // quecto
    }};

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

    // metric unit passed must follow this pattern: mV, μA, MΩ etc.
    // (no delimiters between the metric prefix and the base unit)
    public static Map.Entry<String, Double> getMetricPrefix(String metricUnit) {
        if (metricUnit != null && !metricUnit.trim().isBlank() && (metricUnit.length() > 1)) {
            Map.Entry<String, Double> prefixEntry;
            String prefix;

            // deca check
            if (metricUnit.length() > 2) {

            }
            prefix = metricUnit.substring(0, 1);


            char[] charArray = metricUnit.toCharArray();
            if (charArray.length > 1) {
                metricUnit.
            } else if (charArray.length > 2) {
                // deca
            }
        }
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
