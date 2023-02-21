package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasRangeData;
import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ValuationController {
    @FXML
    private BorderPane root;

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

    private List<AcFreqContainer> vacExtraFreqContainers;
    private List<AcFreqContainer> iacExtraFreqContainers;

    @FXML
    private TableView<MeasRangeData> vdcTableView;
    @FXML
    private TableView<MeasRangeData> vacTableView;
    @FXML
    private TableView<MeasRangeData> idcTableView;
    @FXML
    private TableView<MeasRangeData> iacTableView;
    @FXML
    private TableView<MeasRangeData> rdcTableView;

    private List<TableView<MeasRangeData>> vacExtraTableViews;
    private List<TableView<MeasRangeData>> iacExtraTableViews;

    private CalData calData;

    @FXML
    private void initialize() {
        // TODO: duplicate code from other controller?
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        // sets equal column width:
        List<TableView<MeasRangeData>> tableViewList = List.of(vdcTableView, vacTableView, idcTableView, iacTableView,
                rdcTableView);
        for (TableView<MeasRangeData> tableView : tableViewList) {
            for (TableColumn<MeasRangeData, ?> column : tableView.getColumns()) {
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
        vacExtraTableViews = new LinkedList<>();
        iacExtraTableViews = new LinkedList<>();

        vacExtraFreqContainers = new ArrayList<>();
        iacExtraFreqContainers = new ArrayList<>();
        vacSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > oldValue) {
                calData.getValuationData().addExtraAcFreq("VAC");
                addAcFreqContainer(true);
            } else if (newValue < oldValue) {
                removeAcFreq(true);
                calData.getValuationData().removeExtraAcFreq("VAC");
            }
        });
        iacSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > oldValue) {
                calData.getValuationData().addExtraAcFreq("IAC");
                addAcFreqContainer(false);
            } else if (newValue < oldValue) {
                removeAcFreq(false);
                calData.getValuationData().removeExtraAcFreq("IAC");
            }
        });

        // basic 5 TableViews setup:
        List<TableView<MeasRangeData>> listOfTables = List.of(vdcTableView, vacTableView, idcTableView, iacTableView,
                rdcTableView);
        String[] functions = {"VDC", "VAC", "IDC", "IAC", "RDC"};
        String[] measRangeDataFields = {"rangeProperty", "unitProperty", "numberOfPointsProperty", "rangeTypeProperty",
                "costProperty"};
        int index;
        for (TableView<MeasRangeData> table : listOfTables) {
            index = listOfTables.indexOf(table);
            table.setItems(calData.getValuationData().getObservableArray(functions[index]));

            int i = 0;
            for (TableColumn<MeasRangeData, ?> column : table.getColumns()) {
                column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
                i++;
            }
        }

        // TableView context menu:
        // context menus on TableView rows (non-empty and empty):
        ContextMenu nonEmptyRowContextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Edit");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editMeanRange(event);
            }
        });
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeMeasRange(event);
            }
        });
        nonEmptyRowContextMenu.getItems().addAll(editMenuItem, deleteMenuItem);

        ContextMenu emptyRowContextMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add");
//        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                addNewContactHandler();
//            }
//        });
        emptyRowContextMenu.getItems().add(addMenuItem);

        // TODO: move it to the block above (duplicate for loop)
        for (TableView<MeasRangeData> table : listOfTables) {
            table.setContextMenu(emptyRowContextMenu);

            table.setRowFactory(new Callback<TableView<MeasRangeData>, TableRow<MeasRangeData>>() {
                @Override
                public TableRow<MeasRangeData> call(TableView<MeasRangeData> param) {
                    TableRow<MeasRangeData> row = new TableRow<>();
                    // associating the context menu with the non-empty rows:
                    row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            row.setContextMenu(null);
                        } else {
                            // for retrieving menu's TableView owner later in the addNewMeasRange():
                            nonEmptyRowContextMenu.setUserData(row.getTableView());

                            row.setContextMenu(nonEmptyRowContextMenu);
                        }
                    });

                    return row;
                }
            });

            table.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.DELETE && table.getSelectionModel().getSelectedIndex() >= 0) {
                        removeMeasRange(event);
                    }
                }
            });

            table.getSelectionModel().selectFirst();
//            menuBarDeleteContactItem.disableProperty().bind(Bindings.isEmpty(contactList));
//            menuBarEditContactItem.disableProperty().bind(Bindings.isEmpty(contactList));
//            editMenuItem.disableProperty().bind(Bindings.isEmpty(contactList));
//            deleteMenuItem.disableProperty().bind(Bindings.isEmpty(contactList));
        }
    }

    @FXML
    private void editMeanRange(ActionEvent event) {
        // getting the proper TableView:
        TableView<MeasRangeData> tableView;
        if (event instanceof ActionEvent) {
            ContextMenu menu = ((MenuItem) event.getSource()).getParentPopup();
            tableView = (TableView<MeasRangeData>) menu.getUserData();
        } else {
            System.out.println("ValuationDlgController.editMeasRange() -> TableView not found.");
            return;
        }

        MeasRangeData oldRange = tableView.getSelectionModel().getSelectedItem();
        ObservableList<MeasRangeData> rangeList = tableView.getItems();

        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.setTitle("Editing measurement range");
        dialog.setHeaderText("Enter measurement range / points details:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("valuation/valuation-dlg.fxml"));

        String resolution = calData.getResolution();
        String function = oldRange.getFunctionType().toString();
        ValuationDlgController controller = new ValuationDlgController(function, resolution);

        fxmlLoader.setController(controller);

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        // loading data to the dialog's forms:
        String rangeString = String.valueOf(oldRange.getRange());
        String rangeTypeString = oldRange.getRangeType();
        String unitString = oldRange.getUnit();
        StringBuilder pointsBuilder = new StringBuilder();
        for (double point : oldRange.getPoints()) {
            pointsBuilder.append(point);
            if (oldRange.getPoints().indexOf(point) != oldRange.getPoints().size() - 1) {
                pointsBuilder.append(", ");
            }
        }
        controller.loadData(rangeString, rangeTypeString, unitString, pointsBuilder.toString());

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
                StringBuilder content = new StringBuilder();

                if (!validationResults.get(0)) {
                    for (Node node : controller.getEmptyFields()) {
                        controller.addRedOutline(node);
                    }
                    content.append("Missing input data!\nNo fields in the form may remain empty.\n\n");
                }

                if (!validationResults.get(1)) {
                    controller.addRedOutline(controller.getRangeTF());
                    content.append("Invalid range value!\nEnter a numeric value for the range field.\n\n");
                }

                if (!validationResults.get(2)) {
                    controller.addRedOutline(controller.getPointsTA());
                    content.append("Invalid measurement points!\nEnter numeric values separated with a comma.\n\n");
                }

                alert.setTitle(title);
                alert.setContentText(content.toString());
                alert.showAndWait();
            }
        });

        // dialog results processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MeasRangeData newRange = controller.exportData();
            // TODO: mess
            // TODO: try putting it in the constructor
            newRange.calculateCost();
            newRange.initializeProperties();
            if (tableView != null) {
                ObservableList<MeasRangeData> rangeObservableArray = tableView.getItems();
                calData.getValuationData().editRange(rangeObservableArray, oldRange, newRange);
            } else {
                System.out.println("ValuationController.addNewMeasRange() -> TableView is null");
            }
        }
    }

    // TODO: lots of duplicate code from NewCalDlgController
    @FXML
    private void addNewMeasRange(ActionEvent event) {
        // getting the proper tableView:
        // (eventSourceButton -> sourceParentPane -> sectionPane)
        // TODO: this is a mess, find a better way of retrieving the corresponding TableView
        Button eventSourceButton = (Button) event.getSource();
        Node sourceParentPane = eventSourceButton.getParent();
        Node sectionPane = sourceParentPane.getParent();

        List<Node> sectionControls = ((Pane) sectionPane).getChildren();
        int parentPaneIndex = sectionControls.indexOf(sourceParentPane);

        TableView<MeasRangeData> tableView = null;
        for (Node node : sectionControls) {
            if (node instanceof TableView<?> && (sectionControls.indexOf(node) == parentPaneIndex + 1)) {
                try {
                    tableView = (TableView<MeasRangeData>) node;
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
                StringBuilder content = new StringBuilder();

                if (!validationResults.get(0)) {
                    for (Node node : controller.getEmptyFields()) {
                        controller.addRedOutline(node);
                    }
                    content.append("Missing input data!\nNo fields in the form may remain empty.\n\n");
                }

                if (!validationResults.get(1)) {
                    controller.addRedOutline(controller.getRangeTF());
                    content.append("Invalid range value!\nEnter a numeric value for the range field.\n\n");
                }

                if (!validationResults.get(2)) {
                    controller.addRedOutline(controller.getPointsTA());
                    content.append("Invalid measurement points!\nEnter numeric values separated with a comma.\n\n");
                }

                alert.setTitle(title);
                alert.setContentText(content.toString());
                alert.showAndWait();
            }
        });

        // dialog results processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MeasRangeData newRange = controller.exportData();
            // TODO: mess
            // TODO: try putting it in the constructor
            newRange.calculateCost();
            newRange.initializeProperties();
            if (tableView != null) {
                ObservableList<MeasRangeData> rangeObservableArray = tableView.getItems();
                calData.getValuationData().addRange(rangeObservableArray, newRange);
            } else {
                System.out.println("ValuationController.addNewMeasRange() -> TableView is null");
            }
        }
    }

    private void removeMeasRange(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // TODO: init owners for other alerts used in this project
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle("Deleting measurement range");
        alert.setContentText("Are you sure you want to delete this range?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TableView<MeasRangeData> tableView;
            if (event instanceof KeyEvent) {
                tableView = (TableView<MeasRangeData>) event.getSource();
            } else if (event instanceof ActionEvent) {
                ContextMenu menu = ((MenuItem) event.getSource()).getParentPopup();
                tableView = (TableView<MeasRangeData>) menu.getUserData();
            } else {
                System.out.println("ValuationDlgController.removeMeasRange() -> TableView not found.");
                return;
            }

            MeasRangeData range = tableView.getSelectionModel().getSelectedItem();
            ObservableList<MeasRangeData> rangeList = tableView.getItems();
            calData.getValuationData().removeRange(rangeList, range);
        }
    }

    // TODO: duplicate code from other controller
    @FXML
    private void removeFocus() {
        root.requestFocus();
    }

    private void addAcFreqContainer(boolean isVoltage) {
        AcFreqContainer container = new AcFreqContainer(isVoltage);
        String function = null;
        if (isVoltage) {
            vacExtraFreqContainers.add(container);
            vacSection.getChildren().add(container.getHBox());
            vacSection.getChildren().add(container.getTableView());
            function = "VAC";
        } else {
            iacExtraFreqContainers.add(container);
            iacSection.getChildren().add(container.getHBox());
            iacSection.getChildren().add(container.getTableView());
            function = "IAC";
        }
        // TODO: check for null
        container.initializeTableView();
    }

    private void removeAcFreq(boolean isVoltage) {
        List<AcFreqContainer> acFreqs = (isVoltage) ? vacExtraFreqContainers : iacExtraFreqContainers;
        VBox acSection = (isVoltage) ? vacSection : iacSection;
        int lastIndex = acFreqs.size() - 1;
        AcFreqContainer container = acFreqs.get(lastIndex);
        acSection.getChildren().remove(container.getHBox());
        acSection.getChildren().remove(container.getTableView());
        acFreqs.remove(container);
    }

    private class AcFreqContainer {
        private final HBox hBox;
        private final TextField textField;
        private final ComboBox<String> comboBox;
        private final Button button;
        private final TableView<MeasRangeData> tableView;
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
            TableColumn<MeasRangeData, String> column;
            String[] strings = {"Range", "Unit", "Number of points", "Type", "Cost"};
            for (int i = 0; i < 5; i++) {
                column = new TableColumn<>(strings[i]);
                column.setResizable(false);
                column.prefWidthProperty().bind(tableView.widthProperty().divide(5));
                tableView.getColumns().add(column);
            }
        }

        public void initializeTableView() {
            String[] measRangeDataFields = {"rangeProperty", "unitProperty", "numberOfPointsProperty",
                    "rangeTypeProperty", "costProperty"};
            String functionType = (isVoltage) ? "VAC" : "IAC";
            System.out.println(functionType);
            // TODO: validation
            int lastIndex = calData.getValuationData().getExtraAcFreqs(functionType).size() - 1;
            System.out.println(lastIndex);
            ObservableList<MeasRangeData> rangeArray = calData.getValuationData().getExtraAcFreqs(functionType).
                    get(lastIndex);

            if (rangeArray != null) {
                tableView.setItems(rangeArray);
                int i = 0;
                for (TableColumn<MeasRangeData, ?> column : tableView.getColumns()) {
                    column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
                    i++;
                }
            } else {
                System.out.println("ValuationController.initializeTableView() error - underlying array for the" +
                        " TableView is null");
            }

            // TODO: ContextMenu setup for the TableView; loads of duplicate code -> move to a separate method
            // TableView context menu:
            // context menus on TableView rows (non-empty and empty):
            ContextMenu nonEmptyRowContextMenu = new ContextMenu();
            MenuItem editMenuItem = new MenuItem("Edit");
            editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editMeanRange(event);
                }
            });
            MenuItem deleteMenuItem = new MenuItem("Delete");
            deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    removeMeasRange(event);
                }
            });
            nonEmptyRowContextMenu.getItems().addAll(editMenuItem, deleteMenuItem);

            ContextMenu emptyRowContextMenu = new ContextMenu();
            MenuItem addMenuItem = new MenuItem("Add");
//        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                addNewContactHandler();
//            }
//        });
            emptyRowContextMenu.getItems().add(addMenuItem);

            tableView.setContextMenu(emptyRowContextMenu);

            tableView.setRowFactory(new Callback<TableView<MeasRangeData>, TableRow<MeasRangeData>>() {
                @Override
                public TableRow<MeasRangeData> call(TableView<MeasRangeData> param) {
                    TableRow<MeasRangeData> row = new TableRow<>();
                    // associating the context menu with the non-empty rows:
                    row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            row.setContextMenu(null);
                        } else {
                            // for retrieving menu's parent TableView later:
                            nonEmptyRowContextMenu.setUserData(row.getTableView());

                            row.setContextMenu(nonEmptyRowContextMenu);
                        }
                    });

                    return row;
                }
            });

            tableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.DELETE && tableView.getSelectionModel().getSelectedIndex() >= 0) {
                        removeMeasRange(event);
                    }
                }
            });

            tableView.getSelectionModel().selectFirst();
//            menuBarDeleteContactItem.disableProperty().bind(Bindings.isEmpty(contactList));
//            menuBarEditContactItem.disableProperty().bind(Bindings.isEmpty(contactList));
//            editMenuItem.disableProperty().bind(Bindings.isEmpty(contactList));
//            deleteMenuItem.disableProperty().bind(Bindings.isEmpty(contactList));

        }

        public HBox getHBox() {
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

        public TableView<MeasRangeData> getTableView() {
            return tableView;
        }
    }
}
