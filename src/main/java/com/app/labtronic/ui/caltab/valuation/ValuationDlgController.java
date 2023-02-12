package com.app.labtronic.ui.caltab.valuation;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private String function;

    @FXML
    private void initialize() {
        pointsTA.disableProperty().bind(eurametCB.selectedProperty());
        eurametCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pointsL.setText(newValue ? "Points:" : "Points: (separate values with a comma)");
        });


    }

    public void setFunctionLTxt(String string) {
        functionL.setText(string + " range:");
        function = string;
    }

    private void setEurametPoints() {

    }
}
