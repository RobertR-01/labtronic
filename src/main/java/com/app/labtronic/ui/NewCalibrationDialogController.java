package com.app.labtronic.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class NewCalibrationDialogController {
    @FXML
    private TextField kubackiRegistryNumberTextField;
    @FXML
    private RadioButton multimeterRadioButton;
    @FXML
    private RadioButton calibratorRadioButton;
    @FXML
    private ComboBox<String> pricingTierComboBox;


    @FXML
    private void initialize() {
        // disables combo box if dmm is not selected:
        pricingTierComboBox.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calibratorRadioButton.isSelected(), calibratorRadioButton.selectedProperty()));

        // combo box value depends on whether it's disabled or not:
        final String[] lastComboBoxValue = new String[1];
        pricingTierComboBox.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lastComboBoxValue[0] = pricingTierComboBox.getValue();
                pricingTierComboBox.getItems().add("n/a");
                pricingTierComboBox.setValue("n/a");
            } else {
                pricingTierComboBox.setValue(lastComboBoxValue[0]);
                pricingTierComboBox.getItems().remove(pricingTierComboBox.getItems().size() - 1);
            }
        });
    }

    public String validateNameArgument() {
        String name = kubackiRegistryNumberTextField.getText().trim();
        // TODO: change the conditions
        if (name.length() != 0) {
            return name;
        }
        return null;
    }

    @FXML
    private void disableResolutionComboBox() {

    }

    public String getKubackiRegistryNumber() {
        return kubackiRegistryNumberTextField.getText().trim();
    }
}
