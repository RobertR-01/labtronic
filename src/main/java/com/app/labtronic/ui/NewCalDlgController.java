package com.app.labtronic.ui;

import com.app.labtronic.data.CalData;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

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
    private CheckBox accreditationCB;
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

    private List<Node> nodeList;

    @FXML
    private void initialize() {
        // disables resolution choice combo box and label if dmm radio is not selected:
        resolutionCB.disableProperty().bind(calRadio.selectedProperty());
        resolutionLabel.disableProperty().bind(calRadio.selectedProperty());

        // disables end-user section if the checkbox is not ticked:
        endUserNameTF.disableProperty().bind(endUserCB.selectedProperty().not());
        endUserNameL.disableProperty().bind(endUserCB.selectedProperty().not());
        endUserAddressTF.disableProperty().bind(endUserCB.selectedProperty().not());
        endUserAddressL.disableProperty().bind(endUserCB.selectedProperty().not());

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

        // TODO: try using pane children list instead:
        nodeList = List.of(kubackiLabNoTF, kubackiRegNoTF, kubackiOrdinalNoTF, kubackiYearTF, switezRegNoTF,
                customerNameTF, customerAddressTF, endUserNameTF, endUserAddressTF, manufacturerTF, typeTF, serialNoTF);

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

        // initialize some input for testing:
        kubackiOrdinalNoTF.setText("test");
        switezRegNoTF.setText("test");
        customerNameTF.setText("test");
        customerAddressTF.setText("test");
        manufacturerTF.setText("test");
        typeTF.setText("test");
        serialNoTF.setText("test");
    }

    public boolean validateForm() {
        boolean result = true;
        for (Node node : nodeList) {
            if ((!node.isDisabled()) && (node instanceof TextField || node instanceof TextArea)
                    && (((TextInputControl) node).getText().trim().isEmpty())) {
                node.setStyle("-fx-border-color: red;");
                result = false;
            } else {
                node.setStyle("");
            }
        }
        return result;
    }

    public CalData exportInitialFormData() {
        CalData.Category category = (dmmRadio.isSelected()) ? CalData.Category.DMM : CalData.Category.CALIBRATOR;
        return new CalData(getFullKubackiRegNo(), switezRegNoTF.getText().trim(), datePicker.getValue(),
                accreditationCB.isSelected(), customerNameTF.getText().trim(), customerAddressTF.getText().trim(),
                endUserCB.isSelected(), endUserNameTF.getText().trim(), endUserAddressTF.getText().trim(), category,
                manufacturerTF.getText().trim(), typeTF.getText().trim(), serialNoTF.getText().trim(),
                resolutionCB.getValue());
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
