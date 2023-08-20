package com.app.labtronic.ui.caltab.valuation;

import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasPointData;
import com.app.labtronic.data.valuation.MeasRangeData;
import com.app.labtronic.utility.MeasPointValidator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class ValuationDlgController {
    @FXML
    private Label functionL;
    @FXML
    private TextField rangeTF;
    @FXML
    private ComboBox<String> unitCB;
    @FXML
    private ComboBox<String> rangeTypeCB;
    @FXML
    private Button eurametButton;
    @FXML
    private Label pointsL;
    @FXML
    private TextArea pointsTA;
    @FXML
    private Spinner<Integer> resSpinner;

    private Function functionType;
    private ArrayList<MeasPointData> pointsList;
    private String pointsString;
    private List<Node> nodeList;
    private double range;
    private List<Node> emptyFields;
    private int resCategory;
    private int resolution;

    private CalData calData;

    public ValuationDlgController(String function, String resolution) {
        // TODO: validation?
        switch (function.trim().toUpperCase()) {
            case "VDC" -> functionType = Function.VDC;
            case "VAC" -> functionType = Function.VAC;
            case "IDC" -> functionType = Function.IDC;
            case "IAC" -> functionType = Function.IAC;
            case "RDC" -> functionType = Function.RDC;
            default -> functionType = null;
        }
        setResCategory(resolution);
    }

    @FXML
    private void initialize() {
        pointsList = new ArrayList<>();
        nodeList = new ArrayList<>();

        functionL.setText(functionType.toString() + " range:");
        pointsL.setText("Points:\n(separate values with a comma)");

        // range combo box:
        switch (functionType) {
            case IDC, IAC -> {
                unitCB.getItems().setAll("µA", "mA", "A");
                unitCB.setValue("mA");
            }
            case RDC -> {
                unitCB.getItems().setAll("mΩ", "Ω", "kΩ", "MΩ");
                unitCB.setValue("Ω");
            }
        }

        nodeList = List.of(rangeTF, pointsTA, rangeTypeCB);
        // removes red outline from invalid fields upon typing:
        for (Node node : nodeList) {
            if (node instanceof TextField || node instanceof TextArea) {
                ((TextInputControl) node).textProperty().addListener((observable, oldValue, newValue) -> {
                    if (((!newValue.isEmpty() && oldValue.isEmpty()) || (!newValue.equalsIgnoreCase(oldValue)))
                            && node.getStyle().equals("-fx-border-color: red;")) {
                        node.setStyle("");
                    }
                });
            } else if (node instanceof ComboBox<?>) {
                ((ComboBox<String>) node).valueProperty().addListener((observable, oldValue, newValue) -> {
                    if ((!newValue.equalsIgnoreCase(oldValue)) && node.getStyle().equals("-fx-border-color: red;")) {
                        node.setStyle("");
                    }
                });
            }
        }

        eurametButton.setOnAction(event -> setEurametPoints());
    }

    // TODO: move it to the MeasRangeData class
    @FXML
    private void setEurametPoints() {
        if (validateRange()) {
            List<Double> eurametPointsList = new ArrayList<>();
            List<Integer> defaultValues = new ArrayList<>();
            switch (functionType) {
                case VDC:
                    // TODO: some refinement probably needed to replace that if-chain
                    if (rangeTypeCB.getValue().equals("First")) {
                        if (resCategory >= 5) {
                            defaultValues = List.of(-90, -50, -10, 10, 30, 50, 70, 90);
                        } else {
                            defaultValues = List.of(-90, -10, 10, 50, 90);
                        }
                    } else {
                        if (resCategory == 7) {
                            defaultValues = List.of(-90, 10, 50, 90);
                        } else {
                            defaultValues = List.of(-90, 10, 90);
                        }
                    }
                    break;
                case VAC:
                    if (rangeTypeCB.getValue().equals("First")) {
                        defaultValues = List.of(10, 50, 90);
                    } else {
                        defaultValues = List.of(10, 90);
                    }
                    break;
                case IDC:
                    // range check for >= 1A:
                    double rangeCheck;
                    switch (unitCB.getValue()) {
                        case "µA":
                            rangeCheck = range * 0.000001;
                            break;
                        case "mA":
                            rangeCheck = range * 0.001;
                            break;
                        default:
                            rangeCheck = range;
                            break;
                    }

                    if (rangeTypeCB.getValue().equals("First")) {
                        if (rangeCheck >= 1) {
                            defaultValues = List.of(-90, 10, 50, 90);
                        } else {
                            defaultValues = List.of(-90, 10, 90);
                        }
                    } else {
                        if (rangeCheck >= 1) {
                            defaultValues = List.of(10, 50, 90);
                        } else {
                            defaultValues = List.of(10, 90);
                        }
                    }
                    break;
                case IAC, RDC:
                    defaultValues = List.of(10, 90);
                    break;
            }

            for (Integer defaultValue : defaultValues) {
                eurametPointsList.add(defaultValue.doubleValue() / 100 * range);
            }

            StringBuilder builder = new StringBuilder();
            for (Double point : eurametPointsList) {
                builder.append(point);
                if (eurametPointsList.indexOf(point) != eurametPointsList.size() - 1) {
                    builder.append(", ");
                }
            }
            pointsTA.setText(builder.toString());
        } else {
            // TODO: duplicate code - alert initialization
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid range error");
            alert.setHeaderText("Error while setting up default EURAMET points.");
            alert.setContentText("Enter proper range value.");
            alert.showAndWait();
        }
    }

    // true -> ok
    private boolean validatePoints() {
        List<MeasPointData> pointsCopy = new ArrayList<>(pointsList);
        pointsList.clear(); // to prevent multiple sets of points being added to the list
        boolean result = false;
        String string = pointsTA.getText().trim();
        String[] stringArray = string.split(",");
        for (String point : stringArray) {
            try {
                if (point != null) {
                    // checking if the point value string is valid (number):
                    double pointValue = Double.parseDouble(point);
                    String pointUnit = unitCB.getValue();
                    MeasPointData newPoint = new MeasPointData(point, pointUnit);
                    if (!pointsList.contains(newPoint)) {
                        pointsList.add(newPoint);
                    } else {
                        result = false;
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                System.out.println(e.getMessage());
                //e.printStackTrace();
                result = false;
                pointsList = new ArrayList<>(pointsCopy);
                break;
            }
            result = true;
        }
        return result;
    }

    // call after validate points (pointsList must be properly filled)
    private boolean checkIfPointsInCMCRange() {
        boolean result = true;
        String unit = unitCB.getValue();
        if (pointsList != null && !pointsList.isEmpty()) {
            double pointValue;
            double pointValueInBaseUnit;
            for (MeasPointData point : pointsList) {
                // TODO: check for NumberFormatException
                pointValue = Double.parseDouble(point.getPointValueProperty());
                if (!MeasPointValidator.checkPoint(pointValue, unit, functionType)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    // true -> ok
    private boolean validateRange() {
        boolean result = false;
        String rangeString = rangeTF.getText().trim();
        try {
            if (!rangeString.isEmpty()) {
                range = Double.parseDouble(rangeString);
                result = true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        return result;
    }

    // false -> there are empty fields
    private boolean checkForEmptyFields() {
        boolean result = true;
        emptyFields = new ArrayList<>();
        for (Node node : nodeList) {
            if (((node instanceof TextField || node instanceof TextArea)
                    && (((TextInputControl) node).getText().trim().isEmpty()))) {
                emptyFields.add(node);
                result = false;
            }
        }
        return result;
    }

    // false -> zero range present (invalid)
    // must be called after validateRange()
    private boolean checkForZeroRange() {
        return !((range == 0) && !rangeTF.getText().isBlank());
    }

    // order of values in the returned list is: empty fields check, range TF check, points TA check, zero range check
    // false value means there's a problem with a corresponding check
    public List<Boolean> validateForms() {
        List<Boolean> results = new ArrayList<>();
        results.add(checkForEmptyFields());
        results.add(validateRange());
        results.add(validatePoints());
        results.add(checkForZeroRange());
        results.add(checkIfPointsInCMCRange());
        return results;
    }

    public void addRedOutline(Node node) {
        node.setStyle("-fx-border-color: red;");
    }

    public MeasRangeData exportData() {
        if (range != 0 && rangeTypeCB.getValue() != null && unitCB.getValue() != null && functionType != null
                && pointsList != null && !pointsList.isEmpty() && resCategory != 0) {
            return new MeasRangeData(range, rangeTypeCB.getValue(), unitCB.getValue(), functionType, pointsList,
                    resCategory, resSpinner.getValueFactory().getValue());
        } else {
            return null;
        }
    }

    // TODO: validation
    public void loadData(String range, String rangeType, String unit, String pointsList, int defaultRangeResolution) {
        rangeTF.setText(range);
        rangeTypeCB.setValue(rangeType);
        unitCB.setValue(unit);
        pointsTA.setText(pointsList);
        resSpinner.getValueFactory().setValue(defaultRangeResolution);
    }

    private void setResCategory(String resolution) {
        switch (resolution) {
            case "⋜ 4 8/9 digit" -> resCategory = 4;
            case "5 1/2 ÷ 6 1/2 digit" -> resCategory = 5;
            case "⋝ 7 1/2 digit" -> resCategory = 7;
            default -> resCategory = -1;
        }
    }

    public TextField getRangeTF() {
        return rangeTF;
    }

    public TextArea getPointsTA() {
        return pointsTA;
    }

    public ComboBox<String> getRangeTypeCB() {
        return rangeTypeCB;
    }

    public List<Node> getEmptyFields() {
        return emptyFields;
    }

    public Function getFunctionType() {
        return functionType;
    }

//    public void updateDefaultResolutionSpinner(int resolution) {
//        if (resolution >= 0 && resolution <= 8) {
//            resSpinner.getValueFactory().setValue(resolution);
//        } else {
//            resSpinner.getValueFactory().setValue(0);
//        }
//    }

    public enum Function {
        VDC,
        VAC,
        IDC,
        IAC,
        RDC
    }
}
