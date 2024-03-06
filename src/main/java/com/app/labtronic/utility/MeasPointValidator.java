package com.app.labtronic.utility;

import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;

public class MeasPointValidator {
    private MeasPointValidator() {

    }

    // TODO: multiple exit points
    public static boolean checkPoint(double value, String metricUnit, ValuationDlgController.Function function) {
        double originalValue = value;
        if (value < 0) {
            value *= -1;
        }
        boolean isPointOK = false;
        String baseUnit = null;
        if (metricUnit != null && !metricUnit.trim().isBlank() && function != null) {
            baseUnit = UnitConverter.getBaseUnit(metricUnit);
            UnitConverter.ConversionResult conversionResult = UnitConverter.convertValueToBaseUnit(value, metricUnit);
            if (conversionResult == null) {
                System.out.println("MeasPointValidator -> checkPoint() -> error when converting a point's value to " +
                        "its base unit counterpart. Returning null");
                return false;
            }
            double baseUnitValue = conversionResult.getValue();
            double cmcFloor = 0;
            double cmcCeiling = 0;
            switch (function) {
                case VDC:
                    cmcFloor = 0.0001;
                    cmcCeiling = 1000;
                    break;
                case VAC:
                    if (originalValue < 0) {
                        System.out.println("MeasPointValidator -> checkPoint() -> negative VAC value.");
                        return false;
                    }
                    cmcFloor = 0.0002;
                    cmcCeiling = 1000;
                    break;
                case IDC, IAC:
                    if (originalValue < 0 && function == ValuationDlgController.Function.IAC) {
                        System.out.println("MeasPointValidator -> checkPoint() -> negative IAC value.");
                        return false;
                    }
                    cmcFloor = 0.00001;
                    cmcCeiling = 10;
                    break;
                case RDC:
                    if (originalValue < 0) {
                        System.out.println("MeasPointValidator -> checkPoint() -> negative RDC value.");
                        return false;
                    }
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
}
