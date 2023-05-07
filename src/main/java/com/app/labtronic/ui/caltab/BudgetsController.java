package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BudgetsController {
    @FXML
    private BorderPane root;

    private CalData calData;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());
    }
}
