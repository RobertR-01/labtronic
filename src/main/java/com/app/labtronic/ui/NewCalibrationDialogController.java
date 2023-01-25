package com.app.labtronic.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewCalibrationDialogController {
    @FXML
    private TextField registryNumberTextField;
    @FXML
    private Label customerLabel;
    @FXML
    private Label dateLabel;


    public String validateNameArgument() {
        String name = registryNumberTextField.getText().trim();
        // TODO: change the conditions
        if (name.length() != 0) {
            return name;
        }
        return null;
    }
}
