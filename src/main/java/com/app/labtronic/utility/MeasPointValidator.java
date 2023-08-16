package com.app.labtronic.utility;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;

public class MeasPointValidator {
    private MeasPointValidator() {

    }

    public static boolean checkPoint(double value, String metricUnit, ValuationDlgController.Function function) {
        boolean isPointOK = false;
        String baseUnit = null;
        if (metricUnit != null && !metricUnit.trim().isBlank() && function != null) {
            baseUnit = UnitConverter.getBaseUnit(metricUnit);
            double baseUnitValue = UnitConverter.convertValueToBaseUnit(value, metricUnit);
            switch (function) {
                case VDC:
                    isPointOK = checkVDCPoint(baseUnitValue);
                    break;
                case VAC:
                    isPointOK = checkVACPoint(baseUnitValue);
                    break;
                case IDC:
                    isPointOK = checkIDCPoint(baseUnitValue);
                    break;
                case IAC:
                    isPointOK = checkIACPoint(baseUnitValue);
                    break;
                case RDC:
                    isPointOK = checkRDCPoint(baseUnitValue);
                    break;
            }
        }
        return isPointOK;
    }

    private static boolean checkVDCPoint(double baseUnitValue) {

    }

    private static boolean checkVACPoint(double baseUnitValue) {

    }

    private static boolean checkIDCPoint(double baseUnitValue) {

    }

    private static boolean checkIACPoint(double baseUnitValue) {

    }

    private static boolean checkRDCPoint(double baseUnitValue) {

    }
}
