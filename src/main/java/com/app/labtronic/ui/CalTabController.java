package com.app.labtronic.ui;

import com.app.labtronic.data.CalData;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class CalTabController {
    @FXML
    public BorderPane genInfo;
    @FXML
    private TabPane root;

    private CalData calData;

    @FXML
    private void initialize() {
        // TODO: check if it's really needed
//        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());
    }
}
