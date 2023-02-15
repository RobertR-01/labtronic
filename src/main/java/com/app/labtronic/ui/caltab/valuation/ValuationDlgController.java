package com.app.labtronic.ui.caltab.valuation;

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
    private CheckBox eurametCB;
    @FXML
    private Label pointsL;
    @FXML
    private TextArea pointsTA;

    private Function functionType;
    private ArrayList<Double> pointsList;
    private String pointsString;
    private List<Node> nodeList;
    private double range;

    public ValuationDlgController(String function) {
        // TODO: validation?
        switch (function.trim().toUpperCase()) {
            case "VDC" -> functionType = Function.VDC;
            case "VAC" -> functionType = Function.VAC;
            case "IDC" -> functionType = Function.IDC;
            case "IAC" -> functionType = Function.IAC;
            case "RDC" -> functionType = Function.RDC;
            default -> functionType = null;
        }
    }

    @FXML
    private void initialize() {
        pointsList = new ArrayList<>();
        nodeList = new ArrayList<>();

        functionL.setText(functionType.toString() + " range:");

        pointsL.setText("Points:\n(separate values with a comma)");
        pointsTA.disableProperty().bind(eurametCB.selectedProperty());
        eurametCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pointsL.setText(newValue ? "Points:" : "Points:\n(separate values with a comma)");
        });

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
                    if (!newValue.isEmpty() && oldValue.isEmpty() && node.getStyle().equals("-fx-border-color: red;")) {
                        node.setStyle("");
                    }
                });
            }
        }
    }

    private void setEurametPoints() {
        pointsString = pointsTA.getText().trim();

        switch (functionType) {
            case VDC -> functionType = Function.VDC;
            case VAC -> functionType = Function.VAC;
            case IDC -> functionType = Function.IDC;
            case IAC -> functionType = Function.IAC;
            case RDC -> functionType = Function.RDC;
            default -> functionType = null;
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
                e.printStackTrace();
                result = false;
                break;
            }
            result = true;
        }
        return result;
    }

    private void parsePoints()

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
            e.printStackTrace();
        }
        return result;
    }

    public boolean validateForms() {
        boolean result = false;

        for (Node node : nodeList) {
            if (((!node.isDisabled()) && (node instanceof TextField || node instanceof TextArea)
                    && (((TextInputControl) node).getText().trim().isEmpty()))
                    || !validateRange()
                    || !validatePoints()) {
                node.setStyle("-fx-border-color: red;");
            } else {
                result = true;
            }
        }

        return result;
    }

//    private MeasRangeData exportData() {
//
//    }

    public enum Function {
        VDC,
        VAC,
        IDC,
        IAC,
        RDC
    }
}
