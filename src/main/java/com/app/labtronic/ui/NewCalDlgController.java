package com.app.labtronic.ui;

import com.app.labtronic.data.CalData;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class NewCalDlgController {
    @FXML
    private TextField kubackiLabNoTF;
    @FXML
    private TextField kubackiRegNoTF;
    @FXML
    private TextField kubackiOrdinalNoTF;
    @FXML
    private TextField kubackiYearTF;
    @FXML
    private TextField switezRegNoTF;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField customerAddressTF;
    @FXML
    private CheckBox endUserCB;
    @FXML
    private Label endUserNameL;
    @FXML
    private TextField endUserNameTF;
    @FXML
    private Label endUserAddressL;
    @FXML
    private TextField endUserAddressTF;
    @FXML
    private RadioButton dmmRadio;
    @FXML
    private RadioButton calRadio;
    @FXML
    private TextField manufacturerTF;
    @FXML
    private TextField typeTF;
    @FXML
    private TextField serialNoTF;
    @FXML
    private Label resolutionLabel;
    @FXML
    private ComboBox<String> resolutionCB;

    @FXML
    private void initialize() {
        // disables resolution choice combo box and label if dmm radio is not selected:
        resolutionCB.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calRadio.isSelected(), calRadio.selectedProperty()));
        resolutionLabel.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calRadio.isSelected(), calRadio.selectedProperty()));

        // TODO: consolidate that section somehow to remove duplicates (possibly with the above); consider a method?
        // disables end-user section if the checkbox is not ticked:
        endUserNameTF.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserNameL.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserAddressTF.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserAddressL.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));

        // TODO: the following two blocks are practically the same - needs refactoring?
        // combo box value when enabled/disabled:
        final String[] lastCBValue = new String[1];
        resolutionCB.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lastCBValue[0] = resolutionCB.getValue();
                resolutionCB.getItems().add("n/a");
                resolutionCB.setValue("n/a");
            } else {
                resolutionCB.setValue(lastCBValue[0]);
                resolutionCB.getItems().remove(resolutionCB.getItems().size() - 1);
            }
        });

        // end-user text fields value when enabled/disabled:
        final String[] lastTFValue = new String[2];
        lastTFValue[0] = "";
        lastTFValue[1] = "";
        endUserNameTF.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lastTFValue[0] = endUserNameTF.getText();
                lastTFValue[1] = endUserAddressTF.getText();
                endUserNameTF.setText("n/a");
                endUserAddressTF.setText("n/a");
            } else {
                endUserNameTF.setText(lastTFValue[0]);
                endUserAddressTF.setText(lastTFValue[1]);
            }
        });

        kubackiYearTF.setText(String.valueOf(LocalDate.now().getYear()));
    }

    // TODO: write separate methods for actual validation and export
    public boolean validateForm() {
        boolean result = true;
        if ((getFullKubackiRegNo() == null) || (switezRegNoTF.getText().trim().isEmpty())
                || (datePicker.getValue() == null) || (customerNameTF.getText().trim().isEmpty())
                || (customerAddressTF.getText().trim().isEmpty())
                || ((endUserNameTF.getText().trim().isEmpty() || endUserAddressTF.getText().trim().isEmpty())
                && endUserCB.isSelected())
                || (manufacturerTF.getText().trim().isEmpty()) || (typeTF.getText().trim().isEmpty())
                || (serialNoTF.getText().trim().isEmpty())) {
            result = false;
        }

        return result;
    }

    public CalData exportFormData() {
        CalData.Category category = (dmmRadio.isSelected()) ? CalData.Category.DMM : CalData.Category.CALIBRATOR;
        return new CalData(getFullKubackiRegNo(), switezRegNoTF.getText().trim(), datePicker.getValue(),
                customerNameTF.getText().trim(), customerAddressTF.getText().trim(), endUserNameTF.getText().trim(),
                endUserAddressTF.getText().trim(), category, manufacturerTF.getText().trim(),
                typeTF.getText().trim(), serialNoTF.getText().trim(), resolutionCB.getValue());
    }

    public String getFullKubackiRegNo() {
        String fullNumber;
        String kubackiLabNo = kubackiLabNoTF.getText().trim();
        String kubackiRegNo = kubackiRegNoTF.getText().trim();
        String kubackiOrdinalNo = kubackiOrdinalNoTF.getText().trim();
        String kubackiYear = kubackiYearTF.getText().trim();

        if (kubackiLabNo.isEmpty() || kubackiRegNo.isEmpty() || kubackiOrdinalNo.isEmpty() || kubackiYear.isEmpty()) {
            fullNumber = null;
            System.out.println("NewCalDlgController.getFullKubackiRegNo() error: one or more segments empty.");
        } else {
            fullNumber = kubackiLabNo + "." + kubackiRegNo + "." + kubackiOrdinalNo + "." + kubackiYear;
        }

        return fullNumber;
    }
}
