package com.app.labtronic.data.valuation;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.List;

public class ValuationData {
    private final ObservableList<MeasRangeData> vdcObservableArray;
    private final ObservableList<MeasRangeData> vacObservableArray;
    private final ObservableList<MeasRangeData> idcObservableArray;
    private final ObservableList<MeasRangeData> iacObservableArray;
    private final ObservableList<MeasRangeData> rdcObservableArray;

    // backup for cost recalculation when a section is hidden:
    private ObservableList<MeasRangeData> vdcBackup;
    private ObservableList<MeasRangeData> vacBackup;
    private ObservableList<MeasRangeData> idcBackup;
    private ObservableList<MeasRangeData> iacBackup;
    private ObservableList<MeasRangeData> rdcBackup;
    private List<ObservableList<MeasRangeData>> backupList;

    private final List<ObservableList<MeasRangeData>> vacExtraArrays;
    private final List<ObservableList<MeasRangeData>> iacExtraArrays;

    private final List<SimpleDoubleProperty> vacExtraProperties;
    private final List<SimpleDoubleProperty> iacExtraProperties;

    private double vdcCost;
    private SimpleDoubleProperty observableVdcCost;
    private double vacCost;
    private SimpleDoubleProperty observableVacCost;
    private double idcCost;
    private SimpleDoubleProperty observableIdcCost;
    private double iacCost;
    private SimpleDoubleProperty observableIacCost;
    private double rdcCost;
    private SimpleDoubleProperty observableRdcCost;

    private List<SimpleDoubleProperty> baseProperties;

    private ObservableList<ObservableList<MeasRangeData>> observableLists;
    private double totalServiceCost;
    private SimpleDoubleProperty observableTotalCost;

    public ValuationData() {
        this.vdcObservableArray = FXCollections.observableArrayList();
        this.vacObservableArray = FXCollections.observableArrayList();
        this.idcObservableArray = FXCollections.observableArrayList();
        this.iacObservableArray = FXCollections.observableArrayList();
        this.rdcObservableArray = FXCollections.observableArrayList();

        this.vacExtraArrays = new LinkedList<>();
        this.iacExtraArrays = new LinkedList<>();

        this.observableVdcCost = new SimpleDoubleProperty();
        this.observableVacCost = new SimpleDoubleProperty();
        this.observableIdcCost = new SimpleDoubleProperty();
        this.observableIacCost = new SimpleDoubleProperty();
        this.observableRdcCost = new SimpleDoubleProperty();
        this.observableTotalCost = new SimpleDoubleProperty();

        this.baseProperties = List.of(observableVdcCost, observableVacCost, observableIdcCost, observableIacCost,
                observableRdcCost);
        this.vacExtraProperties = new LinkedList<>();
        this.iacExtraProperties = new LinkedList<>();

        this.observableLists = FXCollections.observableArrayList();

        this.vdcBackup = FXCollections.observableArrayList();
        this.vacBackup = FXCollections.observableArrayList();
        this.idcBackup = FXCollections.observableArrayList();
        this.iacBackup = FXCollections.observableArrayList();
        this.rdcBackup = FXCollections.observableArrayList();
        this.backupList = List.of(vdcBackup, vacBackup, idcBackup, iacBackup, rdcBackup);


        // base 5 sections:
        initBaseSectionsCostProperties();
    }

    private void initBaseSectionsCostProperties() {
        List<SimpleDoubleProperty> properties = List.of(observableVdcCost, observableVacCost, observableIdcCost,
                observableIacCost, observableRdcCost);
        List<ObservableList<MeasRangeData>> observableLists = List.of(vdcObservableArray, vacObservableArray,
                idcObservableArray, iacObservableArray, rdcObservableArray);
        for (int i = 0; i < properties.size(); i++) {
            initFunctionObservableCost(observableLists.get(i), properties.get(i));
        }
    }

    public void initFunctionObservableCost(ObservableList<MeasRangeData> observableList,
                                           SimpleDoubleProperty observableCost) {
        if (observableCost != null && observableList != null) {
            observableList.addListener((ListChangeListener<? super MeasRangeData>) c -> {
                observableCost.set(calculateFunctionCost(observableList));
                resetTotalCostObservable();
            });
        }
    }

    private double calculateFunctionCost(ObservableList<MeasRangeData> observableList) {
        double cost = 0;
        if (observableList != null && !observableList.isEmpty()) {
            for (MeasRangeData range : observableList) {
                cost += range.getCost();
            }
        }
        return cost;
    }

    public void resetTotalCostObservable() {
        observableTotalCost.set(calculateTotalCost());
    }

    public void backupList(ObservableList<MeasRangeData> list, ObservableList<MeasRangeData> backup) {
        if (list != null) {
            backup.clear();
            backup.addAll(list);
        }
    }

    public List<ObservableList<MeasRangeData>> getBackupList() {
        return backupList;
    }

    // TODO: inefficient (probably)
    private double calculateTotalCost() {
        double totalCost = 0;
        if (baseProperties != null && !baseProperties.isEmpty()) {
            for (SimpleDoubleProperty property : baseProperties) {
                totalCost += property.get();
            }
        }
        if (vacExtraProperties != null && !vacExtraProperties.isEmpty()) {
            for (SimpleDoubleProperty property : vacExtraProperties) {
                totalCost += property.get();
            }
        }
        if (iacExtraProperties != null && !iacExtraProperties.isEmpty()) {
            for (SimpleDoubleProperty property : iacExtraProperties) {
                totalCost += property.get();
            }
        }


        return totalCost;
    }

    public double getObservableVdcCost() {
        return observableVdcCost.get();
    }

    public SimpleDoubleProperty observableVdcCostProperty() {
        return observableVdcCost;
    }

    public void setObservableVdcCost(double observableVdcCost) {
        this.observableVdcCost.set(observableVdcCost);
    }

    public double getObservableVacCost() {
        return observableVacCost.get();
    }

    public SimpleDoubleProperty observableVacCostProperty() {
        return observableVacCost;
    }

    public void setObservableVacCost(double observableVacCost) {
        this.observableVacCost.set(observableVacCost);
    }

    public double getObservableIdcCost() {
        return observableIdcCost.get();
    }

    public SimpleDoubleProperty observableIdcCostProperty() {
        return observableIdcCost;
    }

    public void setObservableIdcCost(double observableIdcCost) {
        this.observableIdcCost.set(observableIdcCost);
    }

    public double getObservableIacCost() {
        return observableIacCost.get();
    }

    public SimpleDoubleProperty observableIacCostProperty() {
        return observableIacCost;
    }

    public void setObservableIacCost(double observableIacCost) {
        this.observableIacCost.set(observableIacCost);
    }

    public double getObservableRdcCost() {
        return observableRdcCost.get();
    }

    public SimpleDoubleProperty observableRdcCostProperty() {
        return observableRdcCost;
    }

    public void setObservableRdcCost(double observableRdcCost) {
        this.observableRdcCost.set(observableRdcCost);
    }

    public double getObservableTotalCost() {
        return observableTotalCost.get();
    }

    public SimpleDoubleProperty observableTotalCostProperty() {
        return observableTotalCost;
    }

    public void setObservableTotalCost(double observableTotalCost) {
        this.observableTotalCost.set(observableTotalCost);
    }

    public List<SimpleDoubleProperty> getVacExtraProperties() {
        return vacExtraProperties;
    }

    public List<SimpleDoubleProperty> getIacExtraProperties() {
        return iacExtraProperties;
    }

    public List<SimpleDoubleProperty> getBaseProperties() {
        return baseProperties;
    }

    // results -> {first range exists check, duplicate range check}
    // both true -> edition successful
    public boolean[] addRange(ObservableList<MeasRangeData> observableArray, MeasRangeData range) {
        // TODO: some check to make sure you can only add to the class field arrays
        boolean[] results = {true, true};
        if (range != null && observableArray != null && !observableArray.contains(range)) {
            if (checkForExistingFirstRange(observableArray, range) != null) {
                results[0] = false;
            } else {
                if (checkForDuplicateRange(observableArray, range) != null) {
                    results[1] = false;
                } else {
                    System.out.println("Adding range.");
                    observableArray.add(range);
                }
            }
        }
        return results;
    }

    // results -> {first range exists check, duplicate range check}
    // both true -> edition successful
    public boolean[] editRange(ObservableList<MeasRangeData> observableArray, MeasRangeData oldRange,
                               MeasRangeData newRange) {
        boolean[] results = {true, true};
        if (oldRange != null && newRange != null && observableArray != null && observableArray.contains(oldRange)
                && !observableArray.contains(newRange)) {
            int oldIndex = observableArray.indexOf(oldRange);
            MeasRangeData duplicateRange = checkForExistingFirstRange(observableArray, newRange);
            // TODO: duplicated if statement
            if (duplicateRange != null && observableArray.indexOf(duplicateRange) != oldIndex) {
                results[0] = false;
            } else {
                duplicateRange = checkForDuplicateRange(observableArray, newRange);
                if (duplicateRange != null && observableArray.indexOf(duplicateRange) != oldIndex) {
                    results[1] = false;
                } else {
                    System.out.println("Editing range.");
                    observableArray.set(oldIndex, newRange);
                }
            }
        }
        return results;
    }

    public boolean removeRange(ObservableList<MeasRangeData> observableArray, MeasRangeData range) {
        if (range != null && observableArray != null && observableArray.contains(range)) {
            observableArray.remove(range);
            return true;
        } else {
            return false;
        }
    }

    // checks whether the range with the same numeric range value is present in that function's array;
    // returns a duplicate range found in the range collection or null;
    // takes the unit into account;
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
                case "VAC" -> {
                    vacExtraArrays.add(FXCollections.observableArrayList());
                    vacExtraProperties.add(new SimpleDoubleProperty());
                }
                case "IAC" -> {
                    iacExtraArrays.add(FXCollections.observableArrayList());
                    iacExtraProperties.add(new SimpleDoubleProperty());
                }
            }
            return true;
        }
        return false;
    }

    // TODO: do something about those multiple exit points
    // TODO: performing the same operations on two separate lists - code duplication
    public boolean removeExtraAcFreq(String function) {
        if (function != null && !function.isBlank()) {
            List<ObservableList<MeasRangeData>> freqsList = null;
            List<SimpleDoubleProperty> propertiesList = null;
            switch (function.trim().toUpperCase()) {
                case "VAC":
                    freqsList = vacExtraArrays;
                    propertiesList = vacExtraProperties;
                    break;
                case "IAC":
                    freqsList = iacExtraArrays;
                    propertiesList = iacExtraProperties;
                    break;
            }
            if (freqsList != null && freqsList.size() != 0 && propertiesList != null && propertiesList.size() != 0) {
                freqsList.remove(freqsList.get(freqsList.size() - 1));
                propertiesList.remove(propertiesList.get(propertiesList.size() - 1));
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

    // for additional frequency levels:
    // TODO: duplicate code from getExtraAcFreqs()
    public List<SimpleDoubleProperty> getExtraAcProperties(String function) {
        List<SimpleDoubleProperty> properties = null;
        if (function != null && !function.isBlank()) {
            switch (function.trim()) {
                case "VAC" -> properties = vacExtraProperties;
                case "IAC" -> properties = iacExtraProperties;
            }
        }
        return properties;
    }
}
