package com.app.labtronic.data.valuation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        if (range != null && observableArray != null) {
            observableArray.add(range);
            return true;
        } else {
            return false;
        }
    }

    public boolean editRange(ObservableList<MeasRangeData> observableArray, MeasRangeData oldRange,
                             MeasRangeData newRange) {
        if (oldRange != null && newRange != null && observableArray != null && observableArray.contains(oldRange)) {
            int index = observableArray.indexOf(oldRange);
            observableArray.set(index, newRange);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeRange(ObservableList<MeasRangeData> observableArray, MeasRangeData range) {
        if (range != null && observableArray != null && observableArray.contains(range)) {
            observableArray.remove(range);
            return true;
        } else {
            return false;
        }
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

    // for base 5 functions:
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
                case "VAC":
                    freqsList = vacExtraArrays;
                    break;
                case "IAC":
                    freqsList = iacExtraArrays;
                    break;
            }
        }
        return freqsList;
    }
}
