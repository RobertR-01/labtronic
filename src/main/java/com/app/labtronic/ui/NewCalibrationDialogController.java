package com.app.labtronic.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewCalibrationDialogController {
    @FXML
    private TextField profileNameTextField;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label highestWinLabel;


    public String validateNameArgument() {
        String name = profileNameTextField.getText().trim();
        // TODO: change the conditions
        if (name.length() != 0 && !name.equalsIgnoreCase("empty")) {
            return name;
        }
        return null;
    }
}
