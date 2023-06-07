package com.app.labtronic.data.valuation;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class ValuationData {
    private final ObservableList<MeasRangeData> vdcRangeList;
    private final ObservableList<MeasRangeData> vacRangeList;
    private final ObservableList<MeasRangeData> idcRangeList;
    private final ObservableList<MeasRangeData> iacRangeList;
    private final ObservableList<MeasRangeData> rdcRangeList;

    private final LinkedHashMap<String, ObservableList<MeasRangeData>> vacExtraRangeLists;
    private final LinkedHashMap<String, ObservableList<MeasRangeData>> iacExtraRangeLists;
    private final List<String> vacExtraFrequencies;
    private final List<String> iacExtraFrequencies;

    private SimpleDoubleProperty observableVdcCost;
    private SimpleDoubleProperty observableVacCost;
    private SimpleDoubleProperty observableIdcCost;
    private SimpleDoubleProperty observableIacCost;
    private SimpleDoubleProperty observableRdcCost;

    private List<SimpleDoubleProperty> baseProperties;

    private final LinkedHashMap<String, SimpleDoubleProperty> vacExtraProperties;
    private final LinkedHashMap<String, SimpleDoubleProperty> iacExtraProperties;
//    private final List<SimpleDoubleProperty> vacExtraProperties;
//    private final List<SimpleDoubleProperty> iacExtraProperties;

    private SimpleDoubleProperty observableTotalCost;

    private ObservableList<String> activeMeasFunctions;

    private SimpleStringProperty baseVacFrequency;
    private SimpleStringProperty baseIacFrequency;

    public ValuationData() {
        this.vacExtraFrequencies = new LinkedList<>();
        this.iacExtraFrequencies = new LinkedList<>();

        this.vdcRangeList = FXCollections.observableArrayList();
        this.vacRangeList = FXCollections.observableArrayList();
        this.idcRangeList = FXCollections.observableArrayList();
        this.iacRangeList = FXCollections.observableArrayList();
        this.rdcRangeList = FXCollections.observableArrayList();

        this.vacExtraRangeLists = new LinkedHashMap<>();
        this.iacExtraRangeLists = new LinkedHashMap<>();
//        this.vacExtraRangeLists = new ArrayList<>();
//        this.iacExtraRangeLists = new ArrayList<>();

        this.baseVacFrequency = new SimpleStringProperty("50 Hz");
        this.baseIacFrequency = new SimpleStringProperty("50 Hz");

        this.observableVdcCost = new SimpleDoubleProperty();
        this.observableVacCost = new SimpleDoubleProperty();
        this.observableIdcCost = new SimpleDoubleProperty();
        this.observableIacCost = new SimpleDoubleProperty();
        this.observableRdcCost = new SimpleDoubleProperty();
        this.observableTotalCost = new SimpleDoubleProperty();

        this.baseProperties = List.of(observableVdcCost, observableVacCost, observableIdcCost, observableIacCost,
                observableRdcCost);
        this.vacExtraProperties = new LinkedHashMap<>();
        this.iacExtraProperties = new LinkedHashMap<>();
//        this.vacExtraProperties = new ArrayList<>();
//        this.iacExtraProperties = new ArrayList<>();

        // base 5 sections:
        initBaseSectionsCostProperties();

        this.activeMeasFunctions = FXCollections.observableArrayList();
    }

    // sets up listeners for all 5 base measurement functions (their range lists)
    private void initBaseSectionsCostProperties() {
        List<SimpleDoubleProperty> properties = List.of(observableVdcCost, observableVacCost, observableIdcCost,
                observableIacCost, observableRdcCost);
        List<ObservableList<MeasRangeData>> observableLists = List.of(vdcRangeList, vacRangeList,
                idcRangeList, iacRangeList, rdcRangeList);
        for (int i = 0; i < properties.size(); i++) {
            initFunctionCostProperty(observableLists.get(i), properties.get(i));
        }
    }

    // adds a listener to the range list that recalculates the cost of that measurement function whenever a range is
    // added/removed to/from it
    // also recalculates the total cost
    public void initFunctionCostProperty(ObservableList<MeasRangeData> rangeList,
                                         SimpleDoubleProperty functionCost) {
        if (functionCost != null && rangeList != null) {
            rangeList.addListener((ListChangeListener<? super MeasRangeData>) c -> {
                functionCost.set(calculateFunctionCost(rangeList));
                resetTotalCostProperty();
            });
        }
    }

    // calculates the cost of all measurement ranges within that measurement function/section
    private double calculateFunctionCost(ObservableList<MeasRangeData> rangeList) {
        double cost = 0;
        if (rangeList != null && !rangeList.isEmpty()) {
            for (MeasRangeData range : rangeList) {
                cost += range.getCost();
            }
        }
        return cost;
    }

    public void resetTotalCostProperty() {
        observableTotalCost.set(calculateTotalCost());
    }

    // TODO: inefficient (probably); find a better way of calculating the total cost
    private double calculateTotalCost() {
        double totalCost = 0;
        if (baseProperties != null && !baseProperties.isEmpty()) {
            for (SimpleDoubleProperty property : baseProperties) {
                totalCost += property.get();
            }
        }
        if (vacExtraProperties != null && !vacExtraProperties.isEmpty()) {
            for (Map.Entry<String, SimpleDoubleProperty> entry : vacExtraProperties.entrySet()) {
                totalCost += entry.getValue().get();
            }
        }
        if (iacExtraProperties != null && !iacExtraProperties.isEmpty()) {
            for (Map.Entry<String, SimpleDoubleProperty> entry : iacExtraProperties.entrySet()) {
                totalCost += entry.getValue().get();
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

    public LinkedHashMap<String, SimpleDoubleProperty> getVacExtraProperties() {
        return vacExtraProperties;
    }

    public LinkedHashMap<String, SimpleDoubleProperty> getIacExtraProperties() {
        return iacExtraProperties;
    }

    public List<SimpleDoubleProperty> getBaseProperties() {
        return baseProperties;
    }

    public List<String> getVacExtraFrequencies() {
        return vacExtraFrequencies;
    }

    public List<String> getIacExtraFrequencies() {
        return iacExtraFrequencies;
    }

    public String getBaseVacFrequency() {
        return baseVacFrequency.get();
    }

    public SimpleStringProperty baseVacFrequencyProperty() {
        return baseVacFrequency;
    }

    public void setBaseVacFrequency(String baseVacFrequency) {
        if (baseVacFrequency != null && !baseVacFrequency.isBlank()) {
            this.baseVacFrequency.set(baseVacFrequency);
        } else {
            System.out.println("ValuationData -> setBaseVacFrequency() -> invalid frequency value.");
        }
    }

    public String getBaseIacFrequency() {
        return baseIacFrequency.get();
    }

    public SimpleStringProperty baseIacFrequencyProperty() {
        return baseIacFrequency;
    }

    public void setBaseIacFrequency(String baseIacFrequency) {
        if (baseIacFrequency != null && !baseIacFrequency.isBlank()) {
            this.baseIacFrequency.set(baseIacFrequency);
        } else {
            System.out.println("ValuationData -> setBaseIacFrequency() -> invalid frequency value.");
        }
    }

    // results -> {first range exists check, duplicate range check}
    // both true -> addition successful
    public boolean[] addRange(ObservableList<MeasRangeData> rangeList, MeasRangeData range) {
        // TODO: some check to make sure you can only add to the class field arrays
        boolean[] results = {true, true};
        if (range != null && rangeList != null && !rangeList.contains(range)) {
            if (checkForExistingFirstRange(rangeList, range) != null) {
                results[0] = false;
            } else {
                if (checkForDuplicateRange(rangeList, range) != null) {
                    results[1] = false;
                } else {
                    rangeList.add(range);
                }
            }
        }
        return results;
    }

    // results -> {first range exists check, duplicate range check}
    // both true -> edition successful
    public boolean[] editRange(ObservableList<MeasRangeData> rangeList, MeasRangeData oldRange,
                               MeasRangeData newRange) {
        boolean[] results = {true, true};
        if (oldRange != null && newRange != null && rangeList != null && rangeList.contains(oldRange)
                && !rangeList.contains(newRange)) {
            int oldIndex = rangeList.indexOf(oldRange);
            MeasRangeData duplicateRange = checkForExistingFirstRange(rangeList, newRange);
            // TODO: duplicated if statement
            if (duplicateRange != null && rangeList.indexOf(duplicateRange) != oldIndex) {
                results[0] = false;
            } else {
                duplicateRange = checkForDuplicateRange(rangeList, newRange);
                if (duplicateRange != null && rangeList.indexOf(duplicateRange) != oldIndex) {
                    results[1] = false;
                } else {
                    rangeList.set(oldIndex, newRange);
                }
            }
        }
        return results;
    }

    public boolean removeRange(ObservableList<MeasRangeData> rangeList, MeasRangeData range) {
        if (range != null && rangeList != null && rangeList.contains(range)) {
            rangeList.remove(range);
            return true;
        } else {
            return false;
        }
    }

    // checks whether the range with the same numeric range value is present in that function's range list;
    // returns a duplicate range found in the range collection or null if there's no duplicates;
    // takes the unit into account (1 kV == 1000 V etc.);
    public MeasRangeData checkForDuplicateRange(ObservableList<MeasRangeData> rangeList, MeasRangeData range) {
        MeasRangeData duplicateRange = null;
        if (range != null && rangeList != null) {
            double rangeBaseUnitValue = range.getRangeBaseUnitValue();
            for (MeasRangeData rangeData : rangeList) {
                if (rangeData.getRangeBaseUnitValue() == rangeBaseUnitValue) {
                    duplicateRange = rangeData;
                    break;
                }
            }
        }
        return duplicateRange;
    }

    // checks whether the "first" range already exists in that function's range list;
    // returns a duplicate first range found in the range list or null if there's no first range present yet;
    public MeasRangeData checkForExistingFirstRange(ObservableList<MeasRangeData> rangeList,
                                                    MeasRangeData range) {
        MeasRangeData duplicateRange = null;
        if (range != null && rangeList != null) {
            String rangeType = range.getRangeType();
            for (MeasRangeData rangeData : rangeList) {
                if (rangeType.equals("First") && rangeData.getRangeType().equals("First")) {
                    duplicateRange = rangeData;
                    break;
                }
            }
        }
        return duplicateRange;
    }

    public boolean addExtraAcFreq(String function, String frequency) {
        // TODO: check for valid Strings (frequencies); pattern or something
        if (function != null && !function.isBlank() && frequency != null && !frequency.isBlank()) {
            switch (function.trim().toUpperCase()) {
                case "VAC" -> {
                    vacExtraFrequencies.add(frequency);
                    vacExtraRangeLists.put(frequency, FXCollections.observableArrayList());
                    vacExtraProperties.put(frequency, new SimpleDoubleProperty());
                }
                case "IAC" -> {
                    iacExtraFrequencies.add(frequency);
                    iacExtraRangeLists.put(frequency, FXCollections.observableArrayList());
                    iacExtraProperties.put(frequency, new SimpleDoubleProperty());
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
            LinkedHashMap<String, ObservableList<MeasRangeData>> freqLists = null;
            LinkedHashMap<String, SimpleDoubleProperty> properties = null;
            List<String> frequencies = null;
            switch (function.trim().toUpperCase()) {
                case "VAC":
                    frequencies = vacExtraFrequencies;
                    freqLists = vacExtraRangeLists;
                    properties = vacExtraProperties;
                    break;
                case "IAC":
                    frequencies = iacExtraFrequencies;
                    freqLists = iacExtraRangeLists;
                    properties = iacExtraProperties;
                    break;
            }
            if (freqLists != null && freqLists.size() != 0 && properties != null && properties.size() != 0
            && frequencies != null && !frequencies.isEmpty()) {
                String frequency = frequencies.get(frequencies.size() - 1);
                freqLists.remove(frequency);
                properties.remove(frequency);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // for the base 5 measurement functions:
    public ObservableList<MeasRangeData> getRangeList(String function) {
        ObservableList<MeasRangeData> list = null;
        if (function != null && !function.isBlank()) {
            switch (function.trim().toUpperCase()) {
                case "VDC":
                    list = vdcRangeList;
                    break;
                case "VAC":
                    list = vacRangeList;
                    break;
                case "IDC":
                    list = idcRangeList;
                    break;
                case "IAC":
                    list = iacRangeList;
                    break;
                case "RDC":
                    list = rdcRangeList;
                    break;
            }
        }
        return list;
    }

    // for additional frequency levels:
    public LinkedHashMap<String, ObservableList<MeasRangeData>> getExtraAcRangeLists(String function) {
        LinkedHashMap<String, ObservableList<MeasRangeData>> freqsMap = null;
        if (function != null && !function.isBlank()) {
            switch (function.trim()) {
                case "VAC" -> freqsMap = vacExtraRangeLists;
                case "IAC" -> freqsMap = iacExtraRangeLists;
            }
        }
        return freqsMap;
    }

    // for additional frequency levels:
    public LinkedHashMap<String, SimpleDoubleProperty> getExtraAcProperties(String function) {
        LinkedHashMap<String, SimpleDoubleProperty> properties = null;
        if (function != null && !function.isBlank()) {
            switch (function.trim()) {
                case "VAC" -> properties = vacExtraProperties;
                case "IAC" -> properties = iacExtraProperties;
            }
        }
        return properties;
    }

    public void addActiveMeasFunction(String function) {
        if (function != null && !function.isBlank()) {
            if (!activeMeasFunctions.contains(function)) {
                activeMeasFunctions.add(function);
            }
        } else {
            System.out.println("ValuationData -> addActiveMeasFunction() - invalid function string.");
        }
    }

    public void removeActiveMeasFunction(String function) {
        if (function != null && !function.isBlank()) {
            activeMeasFunctions.remove(function);
        } else {
            System.out.println("ValuationData -> removeActiveMeasFunction() - invalid function string.");
        }
    }

    public void sortActiveFunctions() {

    }

//    public class MeasFunctionSorter implements Comparator<String> {
//        private final String ORDER = "VIRDA";
//        private List<String> orderedList = activeMeasFunctions;
//
//        public void sortMeasFunctions() {
//            // code for base functions (IFs with contains() call)
//
//            // then code for vac and iac in between respectively (split into VAC/IAC and frequency value -> override
//            // compare)
//        }
//
//        @Override
//        public int compare(String o1, String o2) {
//            int pos1 = 0;
//            int pos2 = 0;
//            for (int i = 0; i < Math.min(o1.length(), o2.length()) && pos1 == pos2; i++) {
//                pos1 = ORDER.indexOf(o1.charAt(i));
//                pos2 = ORDER.indexOf(o2.charAt(i));
//            }
//
//            if (pos1 == pos2 && o1.length() != o2.length()) {
//                return o1.length() - o2.length();
//            }
//
//            return pos1 - pos2;
//        }
//    }
}
