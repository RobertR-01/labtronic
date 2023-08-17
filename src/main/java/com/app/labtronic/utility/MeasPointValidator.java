package com.app.labtronic.utility;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;

public class MeasPointValidator {
    private MeasPointValidator() {

    }

    public static boolean checkPoint(double value, String metricUnit, ValuationDlgController.Function function) {
        if (value < 0) {
            value *= -1;
        }
        boolean isPointOK = false;
        String baseUnit = null;
        if (metricUnit != null && !metricUnit.trim().isBlank() && function != null) {
            baseUnit = UnitConverter.getBaseUnit(metricUnit);
            double baseUnitValue = UnitConverter.convertValueToBaseUnit(value, metricUnit);
            double cmcFloor = 0;
            double cmcCeiling = 0;
            switch (function) {
                case VDC:
                    cmcFloor = 0.01;
                    cmcCeiling = 1000;
                    break;
                case VAC:
                    cmcFloor = 0.01;
                    cmcCeiling = 1000;
                    break;
                case IDC:
                    cmcFloor = 0.00001;
                    cmcCeiling = 10;
                    break;
                case IAC:
                    cmcFloor = 0.00001;
                    cmcCeiling = 10;
                    break;
                case RDC:
                    cmcFloor = 0.1;
                    cmcCeiling = 100000000;
                    break;
            }
            if (baseUnitValue >= cmcFloor && baseUnitValue <= cmcCeiling) {
                isPointOK = true;
            }
        }
        return isPointOK;
    }

    private static boolean checkVDCPoint(double baseUnitValue) {
        boolean isPointOK = false;
        if (baseUnitValue)
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
