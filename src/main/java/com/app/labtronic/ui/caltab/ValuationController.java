package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.ValuationData;
import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ValuationController {
    @FXML
    private BorderPane root;
    @FXML
    private Button addBtn;

    @FXML
    private CheckBox vdcCB;
    @FXML
    private CheckBox vacCB;
    @FXML
    private CheckBox idcCB;
    @FXML
    private CheckBox iacCB;
    @FXML
    private CheckBox rdcCB;
    private List<CheckBox> cbList;

    @FXML
    private VBox vdcSection;
    @FXML
    private VBox vacSection;
    @FXML
    private VBox idcSection;
    @FXML
    private VBox iacSection;
    @FXML
    private VBox rdcSection;
    private List<VBox> vBoxList;

    @FXML
    private Label vacFreqL;
    @FXML
    private Spinner<Integer> vacSpinner;
    @FXML
    private Label iacFreqL;
    @FXML
    private Spinner<Integer> iacSpinner;

    private List<AcFreqContainer> vacFreqs;
    private List<AcFreqContainer> iacFreqs;

    @FXML
    private TableView<ValuationData> vdcTableView;
    @FXML
    private TableView<ValuationData> vacTableView;
    @FXML
    private TableView<ValuationData> idcTableView;
    @FXML
    private TableView<ValuationData> iacTableView;
    @FXML
    private TableView<ValuationData> rdcTableView;

    private CalData calData;

    @FXML
    private void initialize() {
        // TODO: duplicate code from other controller?
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        // sets equal column width:
        List<TableView<ValuationData>> tableViewList = List.of(vdcTableView, vacTableView, idcTableView, iacTableView,
                rdcTableView);
        for (TableView<ValuationData> tableView : tableViewList) {
            for (TableColumn<ValuationData, ?> column : tableView.getColumns()) {
                column.prefWidthProperty().bind(tableView.widthProperty().divide(5));
            }
        }

        // TODO: classic - check those code duplicates for bindings etc. from other controllers

        // top panel combo box bindings:
        vacFreqL.disableProperty().bind(vacCB.selectedProperty().not());
        vacSpinner.disableProperty().bind(vacCB.selectedProperty().not());
        iacFreqL.disableProperty().bind(iacCB.selectedProperty().not());
        iacSpinner.disableProperty().bind(iacCB.selectedProperty().not());

        cbList = List.of(vdcCB, vacCB, idcCB, iacCB, rdcCB);
        vBoxList = List.of(vdcSection, vacSection, idcSection, iacSection, rdcSection);

        for (int i = 0; i < cbList.size(); i++) {
            vBoxList.get(i).visibleProperty().bind(cbList.get(i).selectedProperty());
            vBoxList.get(i).managedProperty().bind(vBoxList.get(i).visibleProperty());
        }

        // vac/iac spinners:
        vacFreqs = new ArrayList<>();
        iacFreqs = new ArrayList<>();
        vacSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > oldValue) {
                addAcFreq(true);
            } else if (newValue < oldValue) {
                removeAcFreq(true);
            }
        });
        iacSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > oldValue) {
                addAcFreq(false);
            } else if (newValue < oldValue) {
                removeAcFreq(false);
            }
        });
    }

    // TODO: lots of duplicate code from NewCalDlgController
    @FXML
    private void addNewMeasRange(ActionEvent event) {
        // getting the proper tableView:
        // (eventSourceButton -> sourceParentPane -> sectionPane)
        // TODO: this is a mess, fine a better way of retrieving the corresponding TableView
        Button eventSourceButton = (Button) event.getSource();
        Node sourceParentPane = eventSourceButton.getParent();
        Node sectionPane = sourceParentPane.getParent();

        List<Node> sectionControls = ((Pane) sectionPane).getChildren();
        int parentPaneIndex = sectionControls.indexOf(sourceParentPane);

        TableView<ValuationData> tableView;
        for (Node node : sectionControls) {
            if (node instanceof TableView<?> && (sectionControls.indexOf(node) == parentPaneIndex + 1)) {
                try {
                    tableView = (TableView<ValuationData>) node;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.setTitle("Adding new measurement range");
        dialog.setHeaderText("Enter measurement range / points details:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("valuation/valuation-dlg.fxml"));

        // text for the topmost label in the dialog:
        // TODO: validation? else statement?
        String text = "not initialized";
        for (Node node : ((Pane) sourceParentPane).getChildren()) {
            if (node instanceof Label) {
                text = ((Label) node).getText();
            }
        }

        // TODO: save data from every tab to the active session CalData instance on tab switch and use that CalData
        //  instead
        String resolution = calData.getResolution();
        fxmlLoader.setController(new ValuationDlgController(text, resolution));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        // TODO: obsolete?
        ValuationDlgController controller = fxmlLoader.getController();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        buttonOK.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            // any false value = form issue
            List<Boolean> validationResults = controller.validateForms();
            boolean formProblem = false;
            for (Boolean check : validationResults) {
                if (!check) {
                    formProblem = true;
                    break;
                }
            }
            if (!validationResults.isEmpty() && formProblem) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                actionEvent.consume();
                String title = "Measurement range addition related error";
//                StringBuilder header = new StringBuilder();
                StringBuilder content = new StringBuilder();

                if (!validationResults.get(0)) {
                    for (Node node : controller.getEmptyFields()) {
                        controller.addRedOutline(node);
                    }
//                    header.append("Missing input data!");
                    content.append("Missing input data!\nNo fields in the form may remain empty.\n\n");
                }

                if (!validationResults.get(1)) {
                    controller.addRedOutline(controller.getRangeTF());
//                    header.append("\nInvalid range value!");
                    content.append("Invalid range value!\nEnter a numeric value for the range field.\n\n");
                }

                if (!validationResults.get(2)) {
                    controller.addRedOutline(controller.getPointsTA());
//                    header.append("\nInvalid measurement points!");
                    content.append("Invalid measurement points!\nEnter numeric values separated with a comma.\n\n");
                }

                alert.setTitle(title);
//                alert.setHeaderText(header.toString());
                alert.setContentText(content.toString());
                alert.showAndWait();
            }
        });

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

//            // creating a new calibration tab:
//            Tab newCalTab = new Tab();
//            if (root.getCenter() == null) {
//                tabPane = new TabPane();
//                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
//                root.setCenter(tabPane);
//            }
//
//            newCalTab.setText(controller.getFullKubackiRegNo());
//            ((TabPane) root.centerProperty().get()).getTabs().add(newCalTab);
//            ((TabPane) root.centerProperty().get()).getSelectionModel().select(newCalTab);
//            calTabs.add(newCalTab);
//            removeFocus();
//            CalData calData = controller.exportFormData();
//            ActiveSession.getActiveSessionInstance().addCalData(calData);
//
//            // initializing tab contents:
//            fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("cal-tab.fxml"));
////            fxmlLoader.setLocation(MainApp.class.getResource("cal-tab.fxml"));
//            try {
//                calTabs.get(calTabs.size() - 1).setContent(fxmlLoader.load());
//            } catch (IOException e) {
//                System.out.println("Couldn't load the FXML for the Tab.");
//                e.printStackTrace();
//            }
        }
    }

    // TODO: duplicate code from other controller
    @FXML
    private void removeFocus() {
        root.requestFocus();
    }

    private void addAcFreq(boolean isVoltage) {
        AcFreqContainer container = new AcFreqContainer(isVoltage);
        if (isVoltage) {
            vacFreqs.add(container);
            vacSection.getChildren().add(container.gethBox());
            vacSection.getChildren().add(container.getTableView());
        } else {
            iacFreqs.add(container);
            iacSection.getChildren().add(container.gethBox());
            iacSection.getChildren().add(container.getTableView());
        }
    }

    private void removeAcFreq(boolean isVoltage) {
        List<AcFreqContainer> acFreqs = (isVoltage) ? vacFreqs : iacFreqs;
        VBox acSection = (isVoltage) ? vacSection : iacSection;
        int lastIndex = acFreqs.size() - 1;
        AcFreqContainer container = acFreqs.get(lastIndex);
        acSection.getChildren().remove(container.gethBox());
        acSection.getChildren().remove(container.getTableView());
        acFreqs.remove(container);
    }

    private class AcFreqContainer {
        private final HBox hBox;
        private final TextField textField;
        private final ComboBox<String> comboBox;
        private final Button button;
        private final TableView<ValuationData> tableView;
        private final boolean isVoltage;

        private AcFreqContainer(boolean isVoltage) {
            this.isVoltage = isVoltage;

            hBox = new HBox();
            hBox.spacingProperty().set(20);
            hBox.setAlignment(Pos.CENTER);

            String labelTxt = (isVoltage) ? "VAC" : "IAC";
            Label vacLabel = new Label(labelTxt);
            vacLabel.setUnderline(true);
            hBox.getChildren().add(vacLabel);

            textField = new TextField("50");
            textField.setPrefWidth(50);
            hBox.getChildren().add(textField);

            comboBox = new ComboBox<>(FXCollections.observableArrayList("Hz", "kHz"));
            comboBox.setPrefWidth(70);
            comboBox.setValue("Hz");
            hBox.getChildren().add(comboBox);

            button = new Button();
            Tooltip tooltip = new Tooltip("Add new measurement range / points");
            tooltip.setShowDelay(new Duration(500));
            button.setTooltip(tooltip);
            FontIcon icon = new FontIcon("bx-plus");
            icon.setIconSize(16);
            button.setGraphic(icon);
            button.setOnAction(event -> ValuationController.this.addNewMeasRange(event));
            hBox.getChildren().add(button);

            tableView = new TableView<>();
            VBox.setVgrow(tableView, Priority.ALWAYS);
            TableColumn<ValuationData, String> column;
            String[] strings = {"Range", "Unit", "Meas. Point", "Type", "Cost"};
            for (int i = 0; i < 5; i++) {
                column = new TableColumn<>(strings[i]);
                column.setResizable(false);
                column.prefWidthProperty().bind(tableView.widthProperty().divide(5));
                tableView.getColumns().add(column);
            }
        }

        public HBox gethBox() {
            return hBox;
        }

        public TextField getTextField() {
            return textField;
        }

        public ComboBox<String> getComboBox() {
            return comboBox;
        }

        public Button getButton() {
            return button;
        }

        public TableView<ValuationData> getTableView() {
            return tableView;
        }
    }
}
