package com.app.labtronic.data.valuation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.LinkedList;
import java.util.List;

public class ValuationData {
    private final ObservableList<MeasRangeData> vdcObservableArray;
    private final ObservableList<MeasRangeData> vacObservableArray;
    private final ObservableList<MeasRangeData> idcObservableArray;
    private final ObservableList<MeasRangeData> iacObservableArray;
    private final ObservableList<MeasRangeData> rdcObservableArray;

    private final List<ObservableList<MeasRangeData>> vacExtraArrays;
    private final List<ObservableList<MeasRangeData>> iacExtraArrays;

    public ValuationData() {
        this.vdcObservableArray = FXCollections.observableArrayList();
        this.vacObservableArray = FXCollections.observableArrayList();
        this.idcObservableArray = FXCollections.observableArrayList();
        this.iacObservableArray = FXCollections.observableArrayList();
        this.rdcObservableArray = FXCollections.observableArrayList();

        this.vacExtraArrays = new LinkedList<>();
        this.iacExtraArrays = new LinkedList<>();
    }

    public boolean addRange(ObservableList<MeasRangeData> observableArray, MeasRangeData range) {
        // TODO: some check to make sure you can only add to the class field arrays
        boolean result = false;
        if (range != null && observableArray != null && !observableArray.contains(range)) {
            if (checkForExistingFirstRange(observableArray, range) != null) {
                fireExistingFirstRangeAlert();
            } else {
                if (checkForDuplicateRange(observableArray, range) != null) {
                    fireDuplicateRangeValueAlert();
                } else {
                    observableArray.add(range);
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean editRange(ObservableList<MeasRangeData> observableArray, MeasRangeData oldRange,
                             MeasRangeData newRange) {
        boolean result = false;
        if (oldRange != null && newRange != null && observableArray != null && observableArray.contains(oldRange)
                && !observableArray.contains(newRange)) {
            int oldIndex = observableArray.indexOf(oldRange);
            MeasRangeData duplicateRange = checkForExistingFirstRange(observableArray, newRange);
            // TODO: duplicate if statement
            if (duplicateRange != null && observableArray.indexOf(duplicateRange) != oldIndex) {
                fireExistingFirstRangeAlert();
            } else {
                duplicateRange = checkForDuplicateRange(observableArray, newRange);
                if (duplicateRange != null && observableArray.indexOf(duplicateRange) != oldIndex) {
                    fireDuplicateRangeValueAlert();
                } else {
                    observableArray.set(oldIndex, newRange);
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean removeRange(ObservableList<MeasRangeData> observableArray, MeasRangeData range) {
        if (range != null && observableArray != null && observableArray.contains(range)) {
            observableArray.remove(range);
            return true;
        } else {
            return false;
        }
    }

    private void fireDuplicateRangeValueAlert() {
        Alert alert = initializeAlert("Range already exists within that function.",
                "Range not added - try different range value.");
        alert.showAndWait();
    }

    private void fireExistingFirstRangeAlert() {
        Alert alert = initializeAlert("The \"First\" range is already present for that function.",
                "Range not added - change the range type to \"Additional\" or delete the existing \"First\" range.");
        alert.showAndWait();
    }

    private Alert initializeAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error when adding measurement range");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    // checks whether the range with the same numeric range value is present in that function's array;
    // returns a duplicate range found in the range collection or null;
    // takes the unit into account;
    // TODO: additional check for the combination of numeric value * unit (e.g. 1 kV is the same as 1000 V)
    public MeasRangeData checkForDuplicateRange(ObservableList<MeasRangeData> observableArray, MeasRangeData range) {
        MeasRangeData duplicateRange = null;
        if (range != null && observableArray != null) {
            double rangeBaseUnitValue = range.getRangeBaseUnitValue();
            for (MeasRangeData rangeData : observableArray) {
                if (rangeData.getRangeBaseUnitValue() == rangeBaseUnitValue) {
                    duplicateRange = rangeData;
                    break;
                }
            }
        }
        return duplicateRange;
    }

    public MeasRangeData checkForExistingFirstRange(ObservableList<MeasRangeData> observableArray,
                                                    MeasRangeData range) {
        MeasRangeData duplicateRange = null;
        if (range != null && observableArray != null) {
            String rangeType = range.getRangeType();
            for (MeasRangeData rangeData : observableArray) {
                if (rangeType.equals("First") && rangeData.getRangeType().equals("First")) {
                    duplicateRange = rangeData;
                    break;
                }
            }
        }
        return duplicateRange;
    }

    public boolean addExtraAcFreq(String function) {
        // TODO: check for valid Strings (frequencies); pattern or something
        if (function != null && !function.isBlank()) {
            switch (function.trim().toUpperCase()) {
                case "VAC" -> vacExtraArrays.add(FXCollections.observableArrayList());
                case "IAC" -> iacExtraArrays.add(FXCollections.observableArrayList());
            }
            return true;
        }
        return false;
    }

    // TODO: do something about those multiple exit points
    public boolean removeExtraAcFreq(String function) {
        if (function != null && !function.isBlank()) {
            List<ObservableList<MeasRangeData>> freqsList = null;
            switch (function.trim().toUpperCase()) {
                case "VAC":
                    freqsList = vacExtraArrays;
                    break;
                case "IAC":
                    freqsList = iacExtraArrays;
                    break;
            }
            if (freqsList != null && freqsList.size() != 0) {
                freqsList.remove(freqsList.get(freqsList.size() - 1));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // for the base 5 functions:
    public ObservableList<MeasRangeData> getObservableArray(String function) {
        ObservableList<MeasRangeData> array = null;
        if (function != null && !function.isBlank()) {
            switch (function.trim().toUpperCase()) {
                case "VDC":
                    array = vdcObservableArray;
                    break;
                case "VAC":
                    array = vacObservableArray;
                    break;
                case "IDC":
                    array = idcObservableArray;
                    break;
                case "IAC":
                    array = iacObservableArray;
                    break;
                case "RDC":
                    array = rdcObservableArray;
                    break;
            }
        }
        return array;
    }

    // for additional frequency levels:
    public List<ObservableList<MeasRangeData>> getExtraAcFreqs(String function) {
        List<ObservableList<MeasRangeData>> freqsList = null;
        if (function != null && !function.isBlank()) {
            switch (function.trim()) {
                case "VAC" -> freqsList = vacExtraArrays;
                case "IAC" -> freqsList = iacExtraArrays;
            }
        }
        return freqsList;
    }
}
