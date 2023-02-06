package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import javafx.fxml.FXML;
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
    private Label test;

    private CalData calData;

    @FXML
    private void initialize() {
//        System.out.println(root.getParent());
//        root.setPrefSize(root.getParent().);
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        kubackiRegNoTF.setText(calData.getKubackiRegNo());
        switezRegNoTF.setText(calData.getSwitezRegNo());
        datePicker.setValue(calData.getOrderDate());
        accreditationCB.setSelected(calData.isAccreditation());

        customerNameTA.setText(calData.getCustomerName());
        customerAddressTA.setText(calData.getCustomerAddress());
        endUserCB.setSelected(calData.isEndUserPresent());
        endUserNameTA.setText(calData.getEndUserName());
        endUserAddressTA.setText(calData.getEndUserAddress());

        if (calData.getCategory().equals(CalData.Category.DMM)) {
            dmmRadio.selectedProperty().set(true);
        } else if (calData.getCategory().equals(CalData.Category.CALIBRATOR)) {
            calRadio.selectedProperty().set(true);
        }
        manufacturerTF.setText(calData.getManufacturer());
        typeTF.setText(calData.getType());
        serialNoTF.setText(calData.getSerialNo());
        resolutionCB.setValue(calData.getResolution());

        // TODO: add lsiteners to end-user stuff etc.
        // TODO: possibly a duplicate code from the NewCalDlgController (the listeners, bindings etc.)

    }

    @FXML
    private void test() {
//        root.styleProperty().set("-fx-border-style: solid; -fx-border-width: 5");

//        System.out.println(root.widthProperty().get());
//        System.out.println(root.heightProperty().get());

    }
}
