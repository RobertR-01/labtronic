package com.app.labtronic.ui;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.ui.caltab.GenInfoController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class CalTabController {
    @FXML
    private TabPane rootTabPane;

    @FXML
    private GenInfoController genInfoController;

    private CalData calData;

    @FXML
    private void initialize() {
        // TODO: check if it's really needed
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        // TODO: obsolete (probably)
        calData.setSubTabsList(rootTabPane.getTabs());

        // saves the current data from the general info sub-tab (WIP)
        rootTabPane.getTabs().get(0).selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (oldValue && !newValue) {
                genInfoController.saveData();
            }
        });
    }
}
