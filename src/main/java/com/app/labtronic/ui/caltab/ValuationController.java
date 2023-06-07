package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasRangeData;
import com.app.labtronic.ui.caltab.valuation.FrequencyDlgController;
import com.app.labtronic.ui.caltab.valuation.RangePreviewController;
import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
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
    private HBox vdcHBox;
    @FXML
    private HBox vacHBox;
    @FXML
    private HBox idcHBox;
    @FXML
    private HBox iacHBox;
    @FXML
    private HBox rdcHBox;

    @FXML
    private Button addVdcBtn;
    @FXML
    private Button addVacBtn;
    @FXML
    private Button addIdcBtn;
    @FXML
    private Button addIacBtn;
    @FXML
    private Button addRdcBtn;

    @FXML
    private VBox vacBaseVBox;
    @FXML
    private VBox iacBaseVBox;

    @FXML
    private TextField vacFreqTF;
    @FXML
    private TextField iacFreqTF;
    @FXML
    private ComboBox<String> vacFreqCB;
    @FXML
    private ComboBox<String> iacFreqCB;

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
    //    private List<TableView<MeasRangeData>> vacExtraTableViews;
//    private List<TableView<MeasRangeData>> iacExtraTableViews;
    private CalData calData;

    @FXML
    private Label vdcCostL;
    @FXML
    private Label vacCostL;
    @FXML
    private Label idcCostL;
    @FXML
    private Label iacCostL;
    @FXML
    private Label rdcCostL;
    @FXML
    private Label totalCostL;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        // vac/iac spinners:
//        vacExtraTableViews = new LinkedList<>();
//        iacExtraTableViews = new LinkedList<>();

        vacExtraFreqContainers = new ArrayList<>();
        iacExtraFreqContainers = new ArrayList<>();

        // validation for removing extra AC sections and going below 0 index in different collections is somewhat done
        // by limiting the minimum spinner value and the lister itself
        vacSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > oldValue) {
                String frequency = vacFreqTF.getText().trim() + vacFreqCB.getValue();
                calData.getValuationData().addExtraAcFreq(frequency, "VAC");
                addAcFreqContainer(true);
                int lastIndex = vacExtraFreqContainers.size() - 1;
                // TODO: no check for index out of bounds
                vacExtraFreqContainers.get(lastIndex).getTableView().setUserData(vacExtraFreqContainers.get(lastIndex));
            } else if (newValue < oldValue) {
                removeAcFreq(true);
                calData.getValuationData().removeExtraAcFreq("VAC");
                calData.getValuationData().resetTotalCostProperty();
                // TODO: test if userData attached to a table view should be cleared
            }
        });
        iacSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > oldValue) {
                String frequency = iacFreqTF.getText().trim() + iacFreqCB.getValue();
                calData.getValuationData().addExtraAcFreq(frequency, "IAC");
                addAcFreqContainer(false);
                int lastIndex = iacExtraFreqContainers.size() - 1;
                // TODO: no check for index out of bounds
                iacExtraFreqContainers.get(lastIndex).getTableView().setUserData(iacExtraFreqContainers.get(lastIndex));
            } else if (newValue < oldValue) {
                removeAcFreq(false);
                calData.getValuationData().removeExtraAcFreq("IAC");
                calData.getValuationData().resetTotalCostProperty();
            }
        });

        // attaching section containers to AC section TableViews for easier retrieval of controls later:
        // (setUserData inside the loop below)
        // TODO: no check for frequency related controls being passed as null
        SectionContainer vdcSectionContainer = new SectionContainer(vdcSection, vdcHBox, null, null, addVdcBtn,
                vdcTableView, vdcCostL, "VDC");
        SectionContainer vacSectionContainer = new SectionContainer(vacSection, vacHBox, vacFreqTF, vacFreqCB,
                addVacBtn, vacTableView, vacCostL, "VAC");
        SectionContainer idcSectionContainer = new SectionContainer(idcSection, idcHBox, null, null, addIdcBtn,
                idcTableView, idcCostL, "IDC");
        SectionContainer iacSectionContainer = new SectionContainer(iacSection, iacHBox, iacFreqTF, iacFreqCB,
                addIacBtn, iacTableView, iacCostL, "IAC");
        SectionContainer rdcSectionContainer = new SectionContainer(rdcSection, rdcHBox, null, null, addRdcBtn,
                rdcTableView, rdcCostL, "RDC");
        List<SectionContainer> baseSectionsContainers = List.of(vdcSectionContainer, vacSectionContainer,
                idcSectionContainer, iacSectionContainer, rdcSectionContainer);

        // basic 5 TableViews setup + top panel combo boxes:
        // top panel combo box bindings:
        vacFreqL.disableProperty().bind(vacCB.selectedProperty().not());
        vacSpinner.disableProperty().bind(vacCB.selectedProperty().not());
        iacFreqL.disableProperty().bind(iacCB.selectedProperty().not());
        iacSpinner.disableProperty().bind(iacCB.selectedProperty().not());

        cbList = List.of(vdcCB, vacCB, idcCB, iacCB, rdcCB);
        vBoxList = List.of(vdcSection, vacSection, idcSection, iacSection, rdcSection);

        List<TableView<MeasRangeData>> listOfBaseTables = List.of(vdcTableView, vacTableView, idcTableView, iacTableView,
                rdcTableView);
        String[] functions = {"VDC", "VAC", "IDC", "IAC", "RDC"};
        String[] measRangeDataFields = {"rangeProperty", "unitProperty", "numberOfPointsProperty", "rangeTypeProperty",
                "costProperty"};

        int index;
        for (TableView<MeasRangeData> tableView : listOfBaseTables) {
            index = listOfBaseTables.indexOf(tableView);
            tableView.setItems(calData.getValuationData().getRangeList(functions[index]));

            int i = 0;
            for (TableColumn<MeasRangeData, ?> column : tableView.getColumns()) {
                column.prefWidthProperty().bind(tableView.widthProperty().divide(5));
                column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
                i++;
            }

            // context menus on TableView rows (non-empty and empty):
            ContextMenu nonEmptyRowContextMenu = new ContextMenu();
            MenuItem editMenuItem = new MenuItem("Edit");
            editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editMeasRange(event);
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

            // TODO: move it to the block above (duplicate for loop)
            ContextMenu emptyRowContextMenu = new ContextMenu();
            MenuItem addMenuItem = new MenuItem("Add");
            addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addNewMeasRange(event, false);
                }
            });
            emptyRowContextMenu.getItems().add(addMenuItem);

            tableView.setContextMenu(emptyRowContextMenu);
            // for retrieving menu's parent TableView later:
            emptyRowContextMenu.setUserData(tableView);

            tableView.setRowFactory(new Callback<TableView<MeasRangeData>, TableRow<MeasRangeData>>() {
                @Override
                public TableRow<MeasRangeData> call(TableView<MeasRangeData> param) {
                    TableRow<MeasRangeData> row = new TableRow<>();
                    // associating the context menu with the non-empty rows:
                    row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            row.setContextMenu(null);
                        } else {
                            // for retrieving menu's TableView-owner later in the addNewMeasRange():
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

            // onDoubleClick (range preview) setup:
            tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        if (event.getClickCount() == 2) {
                            previewRange(tableView);
                        }
                    }
                }
            });

            tableView.setUserData(baseSectionsContainers.get(index));
        }

        for (int i = 0; i < cbList.size(); i++) {
            // hiding/revealing base sections:
            vBoxList.get(i).visibleProperty().bind(cbList.get(i).selectedProperty());
            vBoxList.get(i).managedProperty().bind(vBoxList.get(i).visibleProperty());

            // reset the section cost and the total cost when hiding the entire section via check box (including extra
            // frequencies for AC functions):
            int finalI = i;
            cbList.get(i).selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue && oldValue) {
                    int spinnerValue;
                    switch (finalI) {
                        case 0:
                            calData.getValuationData().getRangeList("VDC").clear();
                            calData.getValuationData().setObservableVdcCost(0);
                            break;
                        case 1:
                            spinnerValue = vacSpinner.getValue();
                            for (int j = spinnerValue; j > 1; j--) {
                                vacSpinner.getValueFactory().setValue(j - 1);
                            }
                            vacExtraFreqContainers.clear();
                            calData.getValuationData().getRangeList("VAC").clear();
                            calData.getValuationData().getExtraAcRangeLists("VAC").clear();
                            calData.getValuationData().setObservableVacCost(0);
                            break;
                        case 2:
                            calData.getValuationData().getRangeList("IDC").clear();
                            calData.getValuationData().setObservableIdcCost(0);
                            break;
                        case 3:
                            spinnerValue = iacSpinner.getValue();
                            for (int j = spinnerValue; j > 1; j--) {
                                iacSpinner.getValueFactory().setValue(j - 1);
                            }
                            iacExtraFreqContainers.clear();
                            calData.getValuationData().getRangeList("IAC").clear();
                            calData.getValuationData().getExtraAcRangeLists("IAC").clear();
                            calData.getValuationData().setObservableIacCost(0);
                            break;
                        case 4:
                            calData.getValuationData().getRangeList("RDC").clear();
                            calData.getValuationData().setObservableRdcCost(0);
                            break;
                    }
                    calData.getValuationData().resetTotalCostProperty();
                }
            });
        }

        // total cost label setup:
        vdcCostL.textProperty().bind(calData.getValuationData().observableVdcCostProperty().asString());
        vacCostL.textProperty().bind(calData.getValuationData().observableVacCostProperty().asString());
        idcCostL.textProperty().bind(calData.getValuationData().observableIdcCostProperty().asString());
        iacCostL.textProperty().bind(calData.getValuationData().observableIacCostProperty().asString());
        rdcCostL.textProperty().bind(calData.getValuationData().observableRdcCostProperty().asString());

        totalCostL.textProperty().bind(calData.getValuationData().observableTotalCostProperty().asString());
    }

    @FXML
    private void clearSection(VBox section) {
        if (section == null) {
            return;
        }

        calData.getValuationData().resetTotalCostProperty();
    }

    private String[] openNewAcSectionDlg() {
        String[] frequency = new String[2];

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.setTitle("Choosing frequency");
        dialog.setHeaderText("Enter a desired frequency for this section:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("valuation/frequency-dlg.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return null;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        FrequencyDlgController controller = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {


            return frequency;
        } else {
            return null;
        }
    }

    private void previewRange(TableView<MeasRangeData> tableView) {
        if (tableView != null && !tableView.getSelectionModel().isEmpty()) {
            MeasRangeData rangeData = tableView.getSelectionModel().getSelectedItem();

            String range = String.valueOf(rangeData.getRange());
            String unit = rangeData.getUnit();
            String function = rangeData.getFunctionType().toString();

            SectionContainer container = (SectionContainer) tableView.getUserData();
            String frequency = null;
            String frequencyUnit = null;

            StringBuilder headerBuilder = new StringBuilder();
            headerBuilder.append("Range: ").append(range).append(" ").append(unit);
            if (function.equalsIgnoreCase("VAC") || function.equalsIgnoreCase("IAC")) {
                if (container != null) {
                    frequency = container.getFreqTF().getText();
                    frequencyUnit = container.getFreqCB().getValue();
                }

                headerBuilder.append(" (").append(function).append(")\nf = ").append(frequency).append(" ").
                        append(frequencyUnit);
            }

            // TODO: duplicate code - dialog initialization
            // set up the new dialog:
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(root.getScene().getWindow());
            dialog.setTitle("Previewing measurement range");
            dialog.setHeaderText(headerBuilder.toString());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("valuation/range-preview.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Couldn't load the dialog.");
                e.printStackTrace();
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            // set up the preview's TableView:
            RangePreviewController controller = fxmlLoader.getController();
            // TODO: validation for rangeData
            controller.loadTableViewData(rangeData);
            Optional<ButtonType> result = dialog.showAndWait();
        }
    }

    @FXML
    private void editMeasRange(ActionEvent event) {
        // getting the proper TableView:
        TableView<MeasRangeData> tableView;
        ContextMenu menu = ((MenuItem) event.getSource()).getParentPopup();
        tableView = (TableView<MeasRangeData>) menu.getUserData();
        if (tableView == null) {
            System.out.println("ValuationDlgController.editMeasRange() -> TableView not found.");
            return;
        }

        SectionContainer container = (SectionContainer) tableView.getUserData();

        MeasRangeData oldRange = tableView.getSelectionModel().getSelectedItem();
        ObservableList<MeasRangeData> rangeList = tableView.getItems();

        // TODO: duplicate code - dialog initialization
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
            // any false value present in the list == some form issue
            List<Boolean> validationResults = controller.validateForms();
            boolean formProblem = false;
            for (Boolean check : validationResults) {
                if (!check) {
                    formProblem = true;
                    break;
                }
            }
            // check forms:
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

                if (!validationResults.get(3)) {
                    controller.addRedOutline(controller.getRangeTF());
                    content.append("Invalid range value!\nEnter a non-zero value for the range field.\n\n");
                }

                alert.setTitle(title);
                alert.setContentText(content.toString());
                alert.showAndWait();
            } else {
                // check for duplicate range value / duplicate first range
                MeasRangeData newRange = controller.exportData();

                if (newRange != null) {
                    if (newRange.getFunctionType().toString().equals("VAC")
                            || newRange.getFunctionType().toString().equals("IAC")) {
                        String frequency = container.freqTF.getText() + " " + container.freqCB.getValue();
                        newRange.setFrequency(frequency);
                    }

                    newRange.calculateCost();
                    newRange.initializeProperties();
                    ObservableList<MeasRangeData> rangeObservableArray = tableView.getItems();
                    boolean[] results = calData.getValuationData().editRange(rangeObservableArray, oldRange, newRange);
                    if (!results[0]) {
                        System.out.println("Problem with editing selected range - first range already exists.");
                        fireExistingFirstRangeAlert();
                        actionEvent.consume();
                    }
                    if (!results[1]) {
                        System.out.println("Problem with editing selected range - duplicate range.");
                        fireDuplicateRangeValueAlert();
                        actionEvent.consume();
                    }
                } else {
                    System.out.println("ValuationController.editMeasRange() -> newRange is null");
                }
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
    }

    private void fireDuplicateRangeValueAlert() {
        Alert alert = initializeAlert("Range already exists for that function.",
                "Range not added - try different range value.");
        alert.showAndWait();
    }

    private void fireExistingFirstRangeAlert() {
        Alert alert = initializeAlert("The \"First\" range is already present for that function.",
                "Range not added - change the range type to \"Additional\" or delete the existing \"First\" range.");
        alert.showAndWait();
    }

    private Alert initializeAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error when adding measurement range");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    // wrapper for onAction property in FXML:
    @FXML
    private void handleAddNewMeasRange(ActionEvent event) {
        addNewMeasRange(event, true);
    }

    // TODO: lots of duplicate code from NewCalDlgController
    @FXML
    private void addNewMeasRange(ActionEvent event, boolean isEventSourceButton) {
        TableView<MeasRangeData> tableView;

        if (isEventSourceButton) {
            // when called by clicking a button:
            tableView = (TableView<MeasRangeData>) ((Button) event.getSource()).getUserData();
        } else {
            // when called from the ContextMenu on the TableView:
            ContextMenu menu = ((MenuItem) event.getSource()).getParentPopup();
            tableView = (TableView<MeasRangeData>) menu.getUserData();
        }

        // TODO: no check for tableView being null? same for the above (getUserData() could return null?)
        SectionContainer container = (SectionContainer) tableView.getUserData();

        // text for the topmost label in the dialog:
        // TODO: validation? else statement?
        String text = ((SectionContainer) tableView.getUserData()).getFunctionName();

        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.setTitle("Adding new measurement range");
        dialog.setHeaderText("Enter measurement range / points details:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("valuation/valuation-dlg.fxml"));

        String resolution = calData.getResolution();
        fxmlLoader.setController(new ValuationDlgController(text, resolution));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        ValuationDlgController controller = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        TableView<MeasRangeData> finalTableView = tableView;
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

                if (!validationResults.get(3)) {
                    controller.addRedOutline(controller.getRangeTF());
                    content.append("Invalid range value!\nEnter a non-zero value for the range field.\n\n");
                }

                alert.setTitle(title);
                alert.setContentText(content.toString());
                alert.showAndWait();
            } else {
                // check for duplicate range value / duplicate first range and process the results
                MeasRangeData newRange = controller.exportData();

                if (newRange != null) {
                    if (newRange.getFunctionType().toString().equals("VAC")
                            || newRange.getFunctionType().toString().equals("IAC")) {
                        String frequency = container.freqTF.getText() + " " + container.freqCB.getValue();
                        newRange.setFrequency(frequency);
                    }

                    newRange.calculateCost();
                    newRange.initializeProperties();
                    if (finalTableView != null) {
                        ObservableList<MeasRangeData> rangeObservableArray = finalTableView.getItems();
                        boolean[] results = calData.getValuationData().addRange(rangeObservableArray, newRange);
                        if (!results[0]) {
                            System.out.println("Problem with adding selected range - first range already exists.");
                            controller.addRedOutline(controller.getRangeTF());
                            controller.addRedOutline(controller.getRangeTypeCB());
                            fireExistingFirstRangeAlert();
                            actionEvent.consume();
                        }
                        if (!results[1]) {
                            System.out.println("Problem with adding selected range - duplicate range.");
                            controller.addRedOutline(controller.getRangeTF());
                            fireDuplicateRangeValueAlert();
                            actionEvent.consume();
                        }
                    } else {
                        System.out.println("ValuationController.addNewMeasRange() -> TableView is null");
                    }
                } else {
                    System.out.println("ValuationController.editMeasRange() -> newRange is null");
                }
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
    }

    private void removeMeasRange(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // TODO: check if initOwner call is needed for this and other dialogs
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

    // TODO: duplicate code from the main window controller
    @FXML
    private void removeFocus() {
        root.requestFocus();
    }

    private void addAcFreqContainer(boolean isVoltage) {
        AcFreqContainer container = new AcFreqContainer(isVoltage);
        if (isVoltage) {
            vacExtraFreqContainers.add(container);
            vacSection.getChildren().add(container.getHBox());
            vacSection.getChildren().add(container.getTableView());
            container.setFunctionName("VAC");
        } else {
            iacExtraFreqContainers.add(container);
            iacSection.getChildren().add(container.getHBox());
            iacSection.getChildren().add(container.getTableView());
            container.setFunctionName("IAC");
        }
        // TODO: check for null
        container.initializeTableView();
        container.initializeCostDisplay();
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

    private class SectionContainer {
        private VBox topLevelVBox;
        private HBox hBox;
        private TextField freqTF;
        private ComboBox<String> freqCB;
        private Button addRangeBtn;
        private TableView<MeasRangeData> tableView;
        private Label costL;
        private String functionName;

        private SectionContainer() {

        }

        private SectionContainer(VBox topLevelVBox, HBox hBox, TextField freqTF, ComboBox<String> freqCB,
                                 Button addRangeBtn, TableView<MeasRangeData> tableView, Label costL,
                                 String functionName) {
            this.topLevelVBox = topLevelVBox;
            this.hBox = hBox;
            this.freqTF = freqTF;
            this.freqCB = freqCB;
            this.addRangeBtn = addRangeBtn;
            this.tableView = tableView;
            this.costL = costL;
            this.functionName = functionName;

            setUserDataForButton();
        }

        private void setUserDataForButton() {
            if (addRangeBtn != null && tableView != null) {
                addRangeBtn.setUserData(tableView);
            }
        }

        public VBox getTopLevelVBox() {
            return topLevelVBox;
        }

        public HBox getHBox() {
            return hBox;
        }

        public TextField getFreqTF() {
            return freqTF;
        }

        public ComboBox<String> getFreqCB() {
            return freqCB;
        }

        public Button getAddRangeBtn() {
            return addRangeBtn;
        }

        public TableView<MeasRangeData> getTableView() {
            return tableView;
        }

        public Label getCostL() {
            return costL;
        }

        public String getFunctionName() {
            return functionName;
        }

        public void setTopLevelVBox(VBox topLevelVBox) {
            this.topLevelVBox = topLevelVBox;
        }

        public void setHBox(HBox hBox) {
            this.hBox = hBox;
        }

        public void setFreqTF(TextField freqTF) {
            this.freqTF = freqTF;
        }

        public void setFreqCB(ComboBox<String> freqCB) {
            this.freqCB = freqCB;
        }

        public void setAddRangeBtn(Button addRangeBtn) {
            this.addRangeBtn = addRangeBtn;
        }

        public void setTableView(TableView<MeasRangeData> tableView) {
            this.tableView = tableView;
        }

        public void setCostL(Label costL) {
            this.costL = costL;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }
    }

    private class AcFreqContainer extends SectionContainer {
        private final boolean isVoltage;

        private AcFreqContainer(boolean isVoltage) {
            this.isVoltage = isVoltage;

            setTopLevelVBox(new VBox());
            getTopLevelVBox().setSpacing(10);

            setHBox(new HBox());
            getHBox().spacingProperty().set(20);
            getHBox().setAlignment(Pos.CENTER);

            String labelTxt = (isVoltage) ? "VAC" : "IAC";
            Label vacLabel = new Label(labelTxt);
            vacLabel.setUnderline(true);
            getHBox().getChildren().add(vacLabel);

            setFreqTF(new TextField("50"));
            getFreqTF().setPrefWidth(50);
            getHBox().getChildren().add(getFreqTF());

            setFreqCB(new ComboBox<>(FXCollections.observableArrayList("Hz", "kHz")));
            getFreqCB().setPrefWidth(70);
            getFreqCB().setValue("Hz");
            getHBox().getChildren().add(getFreqCB());

            setAddRangeBtn(new Button());
            Tooltip tooltip = new Tooltip("Add new measurement range / points");
            tooltip.setShowDelay(new Duration(500));
            getAddRangeBtn().setTooltip(tooltip);
            FontIcon icon = new FontIcon("bx-plus");
            icon.setIconSize(16);
            getAddRangeBtn().setGraphic(icon);
            getAddRangeBtn().setOnAction(event -> ValuationController.this.addNewMeasRange(event, true));
            getHBox().getChildren().add(getAddRangeBtn());

            HBox costHBox = new HBox();
            costHBox.setSpacing(10);
            costHBox.setAlignment(Pos.CENTER);
            Label sectionCostL = new Label("Section cost: ");
            sectionCostL.setAlignment(Pos.CENTER);
            setCostL(new Label());
            getCostL().setAlignment(Pos.CENTER);
            costHBox.getChildren().add(sectionCostL);
            costHBox.getChildren().add(getCostL());
            getHBox().getChildren().add(costHBox);

            setTableView(new TableView<>());
            VBox.setVgrow(getTableView(), Priority.ALWAYS);
            TableColumn<MeasRangeData, String> column;
            String[] strings = {"Range", "Unit", "Number of points", "Type", "Cost"};
            for (int i = 0; i < 5; i++) {
                column = new TableColumn<>(strings[i]);
                column.setResizable(false);
                column.prefWidthProperty().bind(getTableView().widthProperty().divide(5));
                getTableView().getColumns().add(column);
            }

            getTopLevelVBox().getChildren().add(getHBox());
            getTopLevelVBox().getChildren().add(getTableView());
        }

        public void initializeTableView() {
            String[] measRangeDataFields = {"rangeProperty", "unitProperty", "numberOfPointsProperty",
                    "rangeTypeProperty", "costProperty"};
            String functionType = (isVoltage) ? "VAC" : "IAC";
            // TODO: validation
            int lastIndex = calData.getValuationData().getExtraAcRangeLists(functionType).size() - 1;
            ObservableList<MeasRangeData> rangeList = calData.getValuationData().getExtraAcRangeLists(functionType).
                    get(lastIndex);

            if (rangeList != null) {
                getTableView().setItems(rangeList);
                int i = 0;
                for (TableColumn<MeasRangeData, ?> column : getTableView().getColumns()) {
                    column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
                    i++;
                }
            } else {
                System.out.println("ValuationController.initializeTableView() error - underlying list for the" +
                        " TableView is null");
            }

            // TODO: ContextMenu setup for the TableView - contains loads of duplicate code -> move to a separate method
            // TableView context menu:
            // context menus on TableView rows (non-empty and empty):
            ContextMenu nonEmptyRowContextMenu = new ContextMenu();
            MenuItem editMenuItem = new MenuItem("Edit");
            editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editMeasRange(event);
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
            addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addNewMeasRange(event, false);
                }
            });
            emptyRowContextMenu.getItems().add(addMenuItem);

            getTableView().setContextMenu(emptyRowContextMenu);
            // for retrieving menu's parent TableView later:
            emptyRowContextMenu.setUserData(getTableView());

            getTableView().setRowFactory(new Callback<TableView<MeasRangeData>, TableRow<MeasRangeData>>() {
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

            getTableView().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.DELETE && getTableView().getSelectionModel().getSelectedIndex() >= 0) {
                        removeMeasRange(event);
                    }
                }
            });

            getTableView().getSelectionModel().selectFirst();

            // onDoubleClick (range preview) setup:
            getTableView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        if (event.getClickCount() == 2) {
                            previewRange(getTableView());
                        }
                    }
                }
            });

            getAddRangeBtn().setUserData(getTableView());
        }

        public void initializeCostDisplay() {
            // TODO: similar method to the initializeTableView() - merge those methods or call them from the constructor
            String functionType = (isVoltage) ? "VAC" : "IAC";
            List<String> frequencies = null;
            switch (functionType) {
                case "VAC":
                    frequencies = calData.getValuationData().getVacExtraFrequencies();
                    break;
                case "IAC":
                    frequencies = calData.getValuationData().getIacExtraFrequencies();
                    break;
                    // TODO: default?
            }
            int lastIndex = frequencies.size() - 1;
            String frequency = frequencies.get(lastIndex);
            // TODO: validation?
            ObservableList<MeasRangeData> rangeLists = calData.getValuationData().getExtraAcRangeLists(functionType).
                    get(frequency);
            SimpleDoubleProperty property = calData.getValuationData().getExtraAcProperties(functionType).
                    get(frequency);
            calData.getValuationData().initFunctionCostProperty(rangeLists, property);
            getCostL().textProperty().bind(property.asString());
        }
    }
}
