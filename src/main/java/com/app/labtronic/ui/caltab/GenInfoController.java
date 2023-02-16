package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class GenInfoController {
    @FXML
    private BorderPane root;
    @FXML
    private TextField kubackiRegNoTF;
    @FXML
    private TextField switezRegNoTF;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox accreditationCB;
    @FXML
    private TextArea customerNameTA;
    @FXML
    private TextArea customerAddressTA;
    @FXML
    private CheckBox endUserCB;
    @FXML
    private Label endUserNameL;
    @FXML
    private TextArea endUserNameTA;
    @FXML
    private Label endUserAddressL;
    @FXML
    private TextArea endUserAddressTA;
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
    private TextArea labName;
    @FXML
    private TextArea labAddress;

    private CalData calData;

    @FXML
    private void initialize() {
        BorderPane.setMargin(root.getCenter(), new Insets(0, 10, 0, 10));

        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        // left
        kubackiRegNoTF.setText(calData.getKubackiRegNo());
        switezRegNoTF.setText(calData.getSwitezRegNo());
        datePicker.setValue(calData.getOrderDate());
        accreditationCB.setSelected(calData.isAccreditation());
        // center
        customerNameTA.setText(calData.getCustomerName());
        customerAddressTA.setText(calData.getCustomerAddress());
        endUserCB.setSelected(calData.isEndUserPresent());
        endUserNameTA.setText(calData.getEndUserName());
        endUserAddressTA.setText(calData.getEndUserAddress());
        // right
        if (calData.getCategory().equals(CalData.Category.DMM)) {
            dmmRadio.selectedProperty().set(true);
        } else if (calData.getCategory().equals(CalData.Category.CALIBRATOR)) {
            calRadio.selectedProperty().set(true);
        }
        manufacturerTF.setText(calData.getManufacturer());
        typeTF.setText(calData.getType());
        serialNoTF.setText(calData.getSerialNo());
        resolutionCB.setValue(calData.getResolution());

        // TODO: duplicate code from the NewCalDlgController (the listeners, bindings etc.);
        //  also TODOs from there apply here
        resolutionCB.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calRadio.isSelected(), calRadio.selectedProperty()));
        resolutionLabel.disableProperty().bind(Bindings.createBooleanBinding(() ->
                calRadio.isSelected(), calRadio.selectedProperty()));

        endUserNameTA.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserNameL.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserAddressTA.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));
        endUserAddressL.disableProperty().bind(Bindings.createBooleanBinding(() ->
                !endUserCB.isSelected(), endUserCB.selectedProperty()));

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

        final String[] lastTFValue = new String[2];
        lastTFValue[0] = "";
        lastTFValue[1] = "";
        endUserNameTA.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lastTFValue[0] = endUserNameTA.getText();
                lastTFValue[1] = endUserAddressTA.getText();
                endUserNameTA.setText("n/a");
                endUserAddressTA.setText("n/a");
            } else {
                endUserNameTA.setText(lastTFValue[0]);
                endUserAddressTA.setText(lastTFValue[1]);
            }
        });
    }

    // TODO: incomplete
    // TODO: validation?
    public void saveData() {
        calData.setResolution(resolutionCB.getValue());
    }
}
