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

    public static double convertValueToBaseUnit(double value, String metricUnit) {
        double valueInBaseUnit = value;
        if (metricUnit != null && !metricUnit.trim().isBlank() && (metricUnit.length() > 1)) {
            MetricPrefixData metricPrefixData = getMetricPrefix(metricUnit);
            if (metricPrefixData != null) {
                double multiplier = metricPrefixData.getMultiplier();
                String prefix = metricPrefixData.getPrefix();
                valueInBaseUnit *= multiplier;
            } else {
                System.out.println("UnitConverter -> convertToBaseUnit() -> \"metricPrefixData == null\". " +
                        "Returning -1.0.");
            }
        } else {
            System.out.println("UnitConverter -> convertToBaseUnit() -> invalid string argument. Returning -1.0.");
            valueInBaseUnit = -1d;
        }

//        return new ConversionResult(valueInBaseUnit);
        return valueInBaseUnit;
    }

    // metric unit passed must follow this pattern: mV, μA, MΩ etc.
    // (no delimiters between the metric prefix and the base unit)
    public static MetricPrefixData getMetricPrefix(String metricUnit) {
        MetricPrefixData prefixData = null;
        if (metricUnit != null && !metricUnit.trim().isBlank() && (metricUnit.length() > 1)) {
            double multiplier = 1;
            String prefix = null;

            if (metricUnit.length() > 2 && METRIC_PREFIX_MAP.containsKey(metricUnit.substring(0, 2))) {
                // deca (da) check:
                prefix = metricUnit.substring(0, 2);
            } else if (METRIC_PREFIX_MAP.containsKey(metricUnit.substring(0, 1))) {
                // any other prefix from the map:
                prefix = metricUnit.substring(0, 1);
            } else {
                System.out.println("UnitConverter -> getMetricPrefix() -> no valid metric prefix found. Returning " +
                        "null.");
            }

            if (prefix != null) {
                multiplier = METRIC_PREFIX_MAP.get(prefix);
                prefixData = new MetricPrefixData(prefix, multiplier);
            }
        } else {
            System.out.println("UnitConverter -> getMetricPrefix() -> invalid string argument. Returning null.");
        }

        return prefixData;
    }

    public static String getUnitWithoutMetricPrefix(String metricUnit) {
        String baseUnit = null;
        MetricPrefixData prefixData = null;

        if (metricUnit != null && !metricUnit.trim().isBlank() && (metricUnit.length() > 1)) {
            prefixData = getMetricPrefix(metricUnit);
            if (prefixData != null) {
                baseUnit = metricUnit.replaceAll(prefixData.getPrefix(), "");
            } else {
                System.out.println("UnitConverter -> getUnitWithoutMetricPrefix() -> \"prefixData == null\". " +
                        "Returning null.");
            }
        } else if (metricUnit != null && !metricUnit.trim().isBlank() && (metricUnit.length() == 1)) {
            baseUnit = metricUnit;
        } else {
            System.out.println("UnitConverter -> getUnitWithoutMetricPrefix() -> invalid string argument. " +
                    "Returning null.");
        }

        return baseUnit;
    }

//    public static class ConversionResult {
//        private final double value;
//
//        private ConversionResult(double value) {
//            this.value = value;
//        }
//
//        public double getValue() {
//            return value;
//        }
//    }

    public static class MetricPrefixData {
        private final String prefix;
        private final double multiplier;

        private MetricPrefixData(String prefix, double multiplier) {
            if (prefix != null && !prefix.trim().isBlank()) {
                this.prefix = prefix;
            } else {
                this.prefix = null;
            }
            this.multiplier = multiplier;
        }

        public String getPrefix() {
            return prefix;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }
}
