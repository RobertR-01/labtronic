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

    @FXML
    private void initialize() {
        pointsList = new ArrayList<>();
        nodeList = new ArrayList<>();

        pointsL.setText("Points:\n(separate values with a comma)");
        pointsTA.disableProperty().bind(eurametCB.selectedProperty());
        eurametCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pointsL.setText(newValue ? "Points:" : "Points:\n(separate values with a comma)");
        });

        // TODO: redundant - too few nodes to check; duplicate code from other dialog controller
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

    public void setFunctionType(String string) {
        functionL.setText(string + " range:");
        switch (string.trim().toUpperCase()) {
            case "VDC" -> functionType = Function.VDC;
            case "VAC" -> functionType = Function.VAC;
            case "IDC" -> functionType = Function.IDC;
            case "IAC" -> functionType = Function.IAC;
            case "RDC" -> functionType = Function.RDC;
            default -> functionType = null;
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
                System.out.println(e.getMessage());
//                e.printStackTrace();
                result = false;
                break;
            }
            result = true;
        }

        System.out.println(result);
        System.out.println(pointsList);

        return result;
    }

    private boolean validateRange() {
        boolean result = true;
        String rangeString = rangeTF.getText().trim();
        try {
            if (!rangeString.isEmpty()) {
                range = Double.parseDouble(rangeString);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            System.out.println(e.getMessage());
//                e.printStackTrace();
            result = false;
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
