package com.app.labtronic.utility;

import com.app.labtronic.data.valuation.MeasPointData;
import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefStdRangePicker {
    private static final HashMap<String, List<String>> REF_RANGES_4708 = new HashMap<>();
    private static final HashMap<String, List<String>> REF_RANGES_SQ7000 = new HashMap<>();

    private RefStdRangePicker() {
        REF_RANGES_4708.put("VDC", List.of("100 μV", "1 mV", "10 mV", "100 mV", "1 V", "10 V", "100 V", "1000 V"));
        REF_RANGES_4708.put("VAC", List.of("1 mV", "10 mV", "100 mV", "1 V", "10 V", "100 V", "1000 V"));
        REF_RANGES_4708.put("IDC", List.of("100 μA", "1 mA", "10 mA", "100 mA", "1 A", "10 A"));
        REF_RANGES_4708.put("IAC", List.of("100 μA", "1 mA", "10 mA", "100 mA", "1 A", "10 A"));
        REF_RANGES_4708.put("RDC", List.of("10 Ω", "100 Ω", "1 kΩ", "10 kΩ", "100 kΩ", "1 MΩ", "10 MΩ", "100 MΩ"));

        REF_RANGES_SQ7000.put("VDC", List.of("0.2 mV", "2 mV", "20 mV", "200 mV", "2 V", "20 V", "200 V", "1000 V"));
        REF_RANGES_SQ7000.put("VAC", List.of("0.2 mV", "2 mV", "20 mV", "200 mV", "2 V", "20 V", "200 V", "1000 V"));
        REF_RANGES_SQ7000.put("IDC", List.of("0.2 mA", "2 mA", "20 mA", "200 mA", "2 A", "20 A"));
        REF_RANGES_SQ7000.put("IAC", List.of("0.2 mA", "2 mA", "20 mA", "200 mA", "2 A", "20 A"));
        REF_RANGES_SQ7000.put("RDC", List.of("20 Ω", "200 Ω", "2 kΩ", "20 kΩ", "200 kΩ", "2 MΩ", "20 MΩ", "200 MΩ"));
    }

    public static void pickRefRange(String referenceStd, MeasPointData point) {
        if (referenceStd == null || referenceStd.trim().isBlank() || point == null) {
            System.out.println("RefStdRangePicker -> pickDefaultRange() -> some error occurred while selecting range");
            return;
        }

        String referenceStandard = point.getUncertaintyData().getRefStandard();
        Map<String, List<String>> refStdRanges;
        switch (referenceStandard) {
            case "4708":
                refStdRanges = REF_RANGES_4708;
                break;
            case "SQ7000":
                refStdRanges = REF_RANGES_SQ7000;
                break;
            default:
                System.out.println("RefStdRangePicker -> pickRefRange() -> invalid reference standard.");
                return;
        }

        ValuationDlgController.Function function = point.getRange().getFunctionType();
        List<String> rangeList;
        switch (function) {
            case VDC:
                rangeList = refStdRanges.get("VDC");
                break;
            case VAC:
                rangeList = refStdRanges.get("VAC");
                break;
            case IDC:
                rangeList = refStdRanges.get("IDC");
                break;
            case IAC:
                rangeList = refStdRanges.get("IAC");
                break;
            case RDC:
                rangeList = refStdRanges.get("RDC");
                break;
            default:
                System.out.println("RefStdRangePicker -> pickRefRange() -> invalid function type.");
                return;
        }

        String pointUnit = point.getUnitProperty();
        char[] unitCharArray = pointUnit.toCharArray();
        char metricPrefix = '\u0000';
        if (unitCharArray.length > 1) {
            metricPrefix = unitCharArray[0];
        }

        double pointValue;
        try {
            pointValue = Double.parseDouble(point.getPointValueProperty());
        } catch (NumberFormatException e) {
            System.out.println("RefStdRangePicker -> pickDefaultRange() -> Can't parse the point's value.");
            return;
        }

        UnitConverter.ConversionResult conversionResult = UnitConverter.convertToBaseUnit(pointValue, metricPrefix);
        if (conversionResult == null) {
            System.out.println("RefStdRangePicker -> pickDefaultRange() -> error when converting a value to its base " +
                    "unit counterpart.");
            return;
        }
        double pointValueInBaseUnit = conversionResult.getValue();

        // determine ref range:

    }

    private String getDCVRange(double pointValue, String referenceStandard) {
        if (pointValue < 0) {
            pointValue *= -1;
        }

        String range = null;
        if (referenceStandard.equals("4708")) {
            if (pointValue >= 0 && pointValue < 0.0002) {

            } else if (pointValue >= 0.0002 && pointValue < 0.002) {

            } else if (pointValue >= 0.002 && pointValue < 0.02) {

            } else if (pointValue >= 0.02 && pointValue < 0.2) {

            } else if (pointValue >= 0.2 && pointValue < 2) {

            } else if (pointValue >= 2 && pointValue < 20) {

            } else if (pointValue >= 20 && pointValue < 200) {

            } else if (pointValue >= 200 && pointValue < 1100) {

            }



        } else if (referenceStandard.equals("SQ7000")) {





        } else {
            System.out.println("RefStdRangePicker -> getDCVRange() -> invalid reference standard string.");
        }

        return range;
    }
}
