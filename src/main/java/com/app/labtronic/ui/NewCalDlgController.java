package com.app.labtronic.ui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class NewCalDlgController {
    @FXML
    private RadioButton dmmRadio;
    @FXML
    private RadioButton calRadio;
    @FXML
    private ComboBox<String> resolutionCB;
    @FXML
    private Label resolutionLabel;
    @FXML
    private TextField kubackiLabNo;
    @FXML
    private TextField kubackiRegNo;
    @FXML
    private TextField kubackiOrdinalNo;
    @FXML
    private TextField kubackiYear;
    @FXML
    private Label endUserNameL;
    @FXML
    private Label endUserAddressL;
    @FXML
    private TextField endUserNameTF;
    @FXML
    private TextField endUserAddressTF;
    @FXML
    private CheckBox endUserCB;

    @FXML
    private void initialize() {
        // disables resolution choice combo box and label if dmm radio is not selected:
        resolutionCB.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calRadio.isSelected(), calRadio.selectedProperty()));
        resolutionLabel.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calRadio.isSelected(), calRadio.selectedProperty()));

        // disables end-user section if the checkbox is not ticked:
        endUserNameTF.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserNameL.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserAddressTF.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserAddressL.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));

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

        kubackiYear.setText(String.valueOf(LocalDate.now().getYear()));
    }

//    public String validateNameArgument() {
//        String name = kubackiRegistryNumberTextField.getText().trim();
//        // TODO: change the conditions
//        if (name.length() != 0) {
//            return name;
//        }
//        return null;
//    }

    public String getKubackiRegNo() {
        String number = kubackiLabNo.getText() + "." + kubackiRegNo.getText() + "."
                + kubackiOrdinalNo.getText() + "." + kubackiYear.getText();
        return number.trim();
    }

    private class CalData {
        private String kubackiRegNo;
        private String switezRegNo;
        private String customerName;
        private String customerAddress;
        private LocalDate orderDate;
        private Category category;
        private String manufacturer;
        private String type;
        private String serialNo;
        private String resolution;

        public CalData(String kubackiRegNo, String switezRegNo, String customerName, String customerAddress,
                       LocalDate orderDate, Category category, String manufacturer, String type,
                       String serialNo, String resolution) {
            this.kubackiRegNo = kubackiRegNo;
            this.switezRegNo = switezRegNo;
            this.customerName = customerName;
            this.customerAddress = customerAddress;
            this.orderDate = orderDate;
            this.category = category;
            this.manufacturer = manufacturer;
            this.type = type;
            this.serialNo = serialNo;
            this.resolution = resolution;
        }

        public String getKubackiRegNo() {
            return kubackiRegNo;
        }

        public String getSwitezRegNo() {
            return switezRegNo;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getCustomerAddress() {
            return customerAddress;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public Category getCategory() {
            return category;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getType() {
            return type;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public String getResolution() {
            return resolution;
        }

        private enum Category {
            DMM,
            CALIBRATOR
        }
    }
}
