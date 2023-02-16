package com.app.labtronic.ui.caltab.valuation;

import com.app.labtronic.data.CalData;
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

    private Function functionType;
    private ArrayList<Double> pointsList;
    private String pointsString;
    private List<Node> nodeList;
    private double range;
    private List<Node> emptyFields;
    private int resCategory;

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

        // TODO: redundant - too few nodes to check; duplicate code from other dialog controller
        // TODO: fix it so the outline disappears when writing anything new in the field
        nodeList = List.of(rangeTF, pointsTA);
        // removes red outline from invalid fields upon typing:
        for (Node node : nodeList) {
            if (node instanceof TextField || node instanceof TextArea) {
                ((TextInputControl) node).textProperty().addListener((observable, oldValue, newValue) -> {
                    if (((!newValue.isEmpty() && oldValue.isEmpty()) || (!newValue.equalsIgnoreCase(oldValue)))
                            && node.getStyle().equals("-fx-border-color: red;")) {
                        node.setStyle("");
                    }
                });
            }
        }

        eurametButton.setOnAction(event -> setEurametPoints());
    }

    @FXML
    private void setEurametPoints() {
        System.out.println(resCategory);
        if (validateRange()) {
            List<Double> pointsList = new ArrayList<>();
            switch (functionType) {
                case VDC:
                    if (rangeTypeCB.getValue().equals("First")) {
                        for (int i = 0; i < 8; i++) {

                        }
                    } else {

                    }
            }
        } else {
            // TODO: popup warning
            System.out.println("Invalid range value.");
        }
    }

    private boolean validatePoints() {
        boolean result = false;
        String string = pointsTA.getText().trim();
        String[] stringArray = string.split(",");
        for (String point : stringArray) {
            try {
                if (point != null) {
                    double pointValue = Double.parseDouble(point);
                    pointsList.add(pointValue);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                System.out.println(e.getMessage());
                e.printStackTrace();
                result = false;
                break;
            }
            result = true;
        }
        return result;
    }

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
            e.printStackTrace();
        }
        return result;
    }

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

    // order in the returned list is: empty fields, range TF, points TA
    // false means there's a problem
    public List<Boolean> validateForms() {
        List<Boolean> results = new ArrayList<>();
        results.add(checkForEmptyFields());
        results.add(validateRange());
        results.add(validatePoints());
        return results;
    }

    public void addRedOutline(Node node) {
        node.setStyle("-fx-border-color: red;");
    }

//    private MeasRangeData exportData() {
//
//    }

    private void setResCategory(String resolution) {
        System.out.println(resolution);
        switch (resolution) {
            case "⋜ 4 8/9 digit" -> resCategory = 4;
            case "5 1/2 ÷ 6 1/2 digit" -> resCategory = 5;
            case "≥ 7 1/2 digit" -> resCategory = 7;
            default -> resCategory = -1;
        }
    }

    public TextField getRangeTF() {
        return rangeTF;
    }

    public TextArea getPointsTA() {
        return pointsTA;
    }

    public List<Node> getEmptyFields() {
        return emptyFields;
    }

    public enum Function {
        VDC,
        VAC,
        IDC,
        IAC,
        RDC
    }
}
