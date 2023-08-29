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

    }

    static {
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

    public static String pickRefRange(String referenceStd, MeasPointData point) {
        if (referenceStd == null || referenceStd.trim().isBlank() || point == null) {
            System.out.println("RefStdRangePicker -> pickRefRange() -> invalid method argument.");
            return null;
        }

//        String referenceStandard = point.getUncertaintyData().getRefStandard();
        Map<String, List<String>> refStdRanges;
        switch (referenceStd) {
            case "4708":
                refStdRanges = REF_RANGES_4708;
                break;
            case "SQ7000":
                refStdRanges = REF_RANGES_SQ7000;
                break;
            default:
                System.out.println("RefStdRangePicker -> pickRefRange() -> invalid reference standard.");
                return null;
        }

        String pointUnit = point.getUnitProperty();

        double pointValue;
        try {
            pointValue = Double.parseDouble(point.getPointValueProperty());
        } catch (NumberFormatException e) {
            System.out.println("RefStdRangePicker -> pickRefRange() -> Can't parse the point's value.");
            return null;
        }

        UnitConverter.ConversionResult conversionResult = UnitConverter.convertValueToBaseUnit(pointValue, pointUnit);
        if (conversionResult == null) {
            System.out.println("RefStdRangePicker -> pickRefRange() -> error when converting a value to its base " +
                    "unit counterpart.");
            return null;
        }
        double pointValueInBaseUnit = conversionResult.getValue();

        // determine ref range:

        ValuationDlgController.Function function = point.getRange().getFunctionType();
        String range = null;
        switch (function) {
            case VDC:
                range = getVDCRange(pointValueInBaseUnit, referenceStd);
                break;
            case VAC:

                break;
            case IDC:

                break;
            case IAC:

                break;
            case RDC:

                break;
            default:
                System.out.println("RefStdRangePicker -> pickRefRange() -> invalid function type.");
                return null;
        }

        return range;
    }

    private static String getVDCRange(double pointValue, String referenceStandard) {
        if (pointValue < 0) {
            pointValue *= -1;
        }

        List<String> rangeList;
        String range = null;
        if (referenceStandard.equals("4708")) {
            rangeList = REF_RANGES_4708.get("VDC");
            if (pointValue >= 0.0001 && pointValue < 0.0002) {
                range = rangeList.get(0);
            } else if (pointValue >= 0.0002 && pointValue < 0.002) {
                range = rangeList.get(1);
            } else if (pointValue >= 0.002 && pointValue < 0.02) {
                range = rangeList.get(2);
            } else if (pointValue >= 0.02 && pointValue < 0.2) {
                range = rangeList.get(3);
            } else if (pointValue >= 0.2 && pointValue < 2) {
                range = rangeList.get(4);
            } else if (pointValue >= 2 && pointValue < 20) {
                range = rangeList.get(5);
            } else if (pointValue >= 20 && pointValue < 200) {
                range = rangeList.get(6);
            } else if (pointValue >= 200 && pointValue <= 1000) {
                range = rangeList.get(7);
            } else {
                System.out.println("RefStdRangePicker -> getVDCRange() -> no valid 4708 range present.");
            }
        } else if (referenceStandard.equals("SQ7000")) {
            rangeList = REF_RANGES_SQ7000.get("VDC");
            if (pointValue >= 0.0001 && pointValue < 0.0002) {
                range = rangeList.get(0);
            } else if (pointValue >= 0.0002 && pointValue < 0.002) {
                range = rangeList.get(1);
            } else if (pointValue >= 0.002 && pointValue < 0.02) {
                range = rangeList.get(2);
            } else if (pointValue >= 0.02 && pointValue < 0.2) {
                range = rangeList.get(3);
            } else if (pointValue >= 0.2 && pointValue < 2) {
                range = rangeList.get(4);
            } else if (pointValue >= 2 && pointValue < 20) {
                range = rangeList.get(5);
            } else if (pointValue >= 20 && pointValue < 200) {
                range = rangeList.get(6);
            } else if (pointValue >= 200 && pointValue <= 1000) {
                range = rangeList.get(7);
            } else {
                System.out.println("RefStdRangePicker -> getVDCRange() -> no valid SQ7000 range present.");
            }
        } else {
            System.out.println("RefStdRangePicker -> getVDCRange() -> invalid reference standard string.");
        }

        return range;
    }


    // TODO: no check for negative values? (see: MeasPointValidator)
    private static String getVACRange(double pointValue, String referenceStandard) {
        List<String> rangeList;
        String range = null;
        if (referenceStandard.equals("4708")) {
            rangeList = REF_RANGES_4708.get("VAC");
            if (pointValue >= 0.0002 && pointValue < 0.002) {
                range = rangeList.get(0);
            } else if (pointValue >= 0.002 && pointValue < 0.02) {
                range = rangeList.get(1);
            } else if (pointValue >= 0.02 && pointValue < 0.2) {
                range = rangeList.get(2);
            } else if (pointValue >= 0.2 && pointValue < 2) {
                range = rangeList.get(3);
            } else if (pointValue >= 2 && pointValue < 20) {
                range = rangeList.get(4);
            } else if (pointValue >= 20 && pointValue < 200) {
                range = rangeList.get(5);
            } else if (pointValue >= 200 && pointValue <= 1000) {
                range = rangeList.get(6);
            } else {
                System.out.println("RefStdRangePicker -> getVACRange() -> no valid 4708 range present.");
            }
        } else if (referenceStandard.equals("SQ7000")) {
            rangeList = REF_RANGES_SQ7000.get("VAC");
            if (pointValue >= 0.0001 && pointValue < 0.0002) {
                range = rangeList.get(0);
            } else if (pointValue >= 0.0002 && pointValue < 0.002) {
                range = rangeList.get(1);
            } else if (pointValue >= 0.002 && pointValue < 0.02) {
                range = rangeList.get(2);
            } else if (pointValue >= 0.02 && pointValue < 0.2) {
                range = rangeList.get(3);
            } else if (pointValue >= 0.2 && pointValue < 2) {
                range = rangeList.get(4);
            } else if (pointValue >= 2 && pointValue < 20) {
                range = rangeList.get(5);
            } else if (pointValue >= 20 && pointValue < 200) {
                range = rangeList.get(6);
            } else if (pointValue >= 200 && pointValue <= 1000) {
                range = rangeList.get(7);
            } else {
                System.out.println("RefStdRangePicker -> getVACRange() -> no valid SQ7000 range present.");
            }
        } else {
            System.out.println("RefStdRangePicker -> getVACRange() -> invalid reference standard string.");
        }

        return range;
    }

    private static String getIDCRange(double pointValue, String referenceStandard) {
        if (pointValue < 0) {
            pointValue *= -1;
        }

        List<String> rangeList;
        String range = null;
        if (referenceStandard.equals("4708")) {
            rangeList = REF_RANGES_4708.get("VDC");
            if (pointValue >= 0.0001 && pointValue < 0.0002) {
                range = rangeList.get(0);
            } else if (pointValue >= 0.0002 && pointValue < 0.002) {
                range = rangeList.get(1);
            } else if (pointValue >= 0.002 && pointValue < 0.02) {
                range = rangeList.get(2);
            } else if (pointValue >= 0.02 && pointValue < 0.2) {
                range = rangeList.get(3);
            } else if (pointValue >= 0.2 && pointValue < 2) {
                range = rangeList.get(4);
            } else if (pointValue >= 2 && pointValue < 20) {
                range = rangeList.get(5);
            } else if (pointValue >= 20 && pointValue < 200) {
                range = rangeList.get(6);
            } else if (pointValue >= 200 && pointValue <= 1000) {
                range = rangeList.get(7);
            } else {
                System.out.println("RefStdRangePicker -> getVDCRange() -> no valid 4708 range present.");
            }
        } else if (referenceStandard.equals("SQ7000")) {
            rangeList = REF_RANGES_SQ7000.get("VDC");
            if (pointValue >= 0.0001 && pointValue < 0.0002) {
                range = rangeList.get(0);
            } else if (pointValue >= 0.0002 && pointValue < 0.002) {
                range = rangeList.get(1);
            } else if (pointValue >= 0.002 && pointValue < 0.02) {
                range = rangeList.get(2);
            } else if (pointValue >= 0.02 && pointValue < 0.2) {
                range = rangeList.get(3);
            } else if (pointValue >= 0.2 && pointValue < 2) {
                range = rangeList.get(4);
            } else if (pointValue >= 2 && pointValue < 20) {
                range = rangeList.get(5);
            } else if (pointValue >= 20 && pointValue < 200) {
                range = rangeList.get(6);
            } else if (pointValue >= 200 && pointValue <= 1000) {
                range = rangeList.get(7);
            } else {
                System.out.println("RefStdRangePicker -> getVDCRange() -> no valid SQ7000 range present.");
            }
        } else {
            System.out.println("RefStdRangePicker -> getVDCRange() -> invalid reference standard string.");
        }

        return range;
    }


    public static HashMap<String, List<String>> getRefRanges4708() {
        return REF_RANGES_4708;
    }
}
