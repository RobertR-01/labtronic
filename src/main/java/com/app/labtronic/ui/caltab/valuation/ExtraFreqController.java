package com.app.labtronic.ui.caltab.valuation;

import com.app.labtronic.data.valuation.MeasRangeData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ExtraFreqController {
    @FXML
    private VBox root;
    @FXML
    private HBox sectionHBox;
    @FXML
    private TextField freqTF;
    @FXML
    private ComboBox<String> freqCB;
    @FXML
    private Button addRangeBtn;
    @FXML
    private Label acCostL;
    @FXML
    private TableView<MeasRangeData> tableView;


}
