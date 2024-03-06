package com.app.labtronic.ui.caltab.valuation;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FrequencyDlgController {
    @FXML
    private TextField valueTF;
    @FXML
    private ComboBox<String> freqCB;

    public String[] exportData() {
        // TODO: form validation + error highlights
        String freqValue = valueTF.getText().trim();
        String freqUnit = freqCB.getValue();
        String[] frequency = {freqValue, freqUnit};
        return frequency;
    }
}
