package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasPointData;
import com.app.labtronic.data.valuation.MeasRangeData;
import com.app.labtronic.utility.RefStdRangePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class BudgetsController {
    @FXML
    private BorderPane root;
    @FXML
    private ListView<String> functionListView;
    @FXML
    private ListView<MeasRangeData> rangeListView;
    @FXML
    private ListView<MeasPointData> pointListView;
    @FXML
    private Button calculateButton;
    @FXML
    private Spinner<Integer> dutResSpinner;
    @FXML
    private ComboBox<String> refStdRangeSelector;

    @FXML
    private TextField dutReading0;
    @FXML
    private TextField dutReading1;
    @FXML
    private TextField dutReading2;
    @FXML
    private TextField dutReading3;
    @FXML
    private TextField dutReading4;
    @FXML
    private TextField dutReading5;
    @FXML
    private TextField dutReading6;
    @FXML
    private TextField dutReading7;
    @FXML
    private TextField dutReading8;
    @FXML
    private TextField dutReading9;
    private List<TextField> readingsTFList;
    @FXML
    private VBox dutReadingsVBox;
    @FXML
    private ComboBox<String> refStdSelector;
    @FXML
    private Label refStdRangeUnit;

    @FXML
    private Label instrumentType;

    private CalData calData;
    private ObservableList<String> functionList;
    private ObservableList<MeasRangeData> rangeList;
    private ObservableList<MeasPointData> pointList;
    private MeasRangeData activeRange;
    private MeasPointData activePoint;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());
        // test button
//        testButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println(rangeList.hashCode());
//                System.out.println();
//                for (MeasRangeData e : rangeList) {
//                    System.out.print(" ; ");
//                    System.out.print(e.getRange());
//                }
//                System.out.println();
//            }
//        });

        String category;
        switch (calData.getCategory()) {
            case CALIBRATOR:
                category = "Calibrator";
                break;
            case DMM:
                category = "Multimeter";
                break;
            default:
                category = "missing";
                break;
        }
        instrumentType.setText(category);

        readingsTFList = List.of(dutReading0, dutReading1, dutReading2, dutReading3, dutReading4, dutReading5,
                dutReading6, dutReading7, dutReading8, dutReading9);

        for (TextField textField : readingsTFList) {
            if (textField != null) {
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (((!newValue.isEmpty() && oldValue.isEmpty()) || (!newValue.equalsIgnoreCase(oldValue)))
                            && textField.getStyle().equals("-fx-border-color: red;")) {
                        textField.setStyle("");
                    }
                });
            }
        }

        functionList = calData.getValuationData().getActiveMeasFunctions();
        rangeList = FXCollections.observableArrayList(); // TODO: check if needed

        functionListView.setItems(functionList);

        // to make the list view always have something selected unless empty
        functionList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                while (c.next()) {
                    if (c.wasAdded() && c.getList().size() == 1) {
                        functionListView.getSelectionModel().selectFirst();
                    }
                }
            }
        });

        functionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> {
            if (observable != null && observable.getValue() != null) {
                String[] functionArray = observable.getValue().split(" ");
                String measFunction = functionArray[0];
                String frequency;
                String completeFunctionName = measFunction;
                rangeList = calData.getValuationData().getRangeList(measFunction);

                if (functionArray.length > 1) {
                    frequency = functionArray[1] + " " + functionArray[2];
                    completeFunctionName += " " + frequency;
                    switch (measFunction) {
                        case "VAC":
                            if (calData.getValuationData().getVacExtraFrequencies().contains(frequency)) {
                                rangeList = calData.getValuationData().getExtraAcRangeLists("VAC").get(frequency);
                            }
                            break;
                        case "IAC":
                            if (calData.getValuationData().getIacExtraFrequencies().contains(frequency)) {
                                rangeList = calData.getValuationData().getExtraAcRangeLists("IAC").get(frequency);
                            }
                            break;
                    }
                }

                rangeListView.setItems(rangeList);

                // select first item in the rangeListView after adding the first range for this function:
                String finalCompleteFunctionName = completeFunctionName;
                rangeList.addListener(new ListChangeListener<MeasRangeData>() {
                    @Override
                    public void onChanged(Change<? extends MeasRangeData> c) {
                        if (!rangeList.isEmpty()) {
                            String measFunctionTest = rangeList.get(0).getFunctionType().toString();
                            String frequencyTest = rangeList.get(0).getFrequency();
                            String completeFunctionNameTest = measFunctionTest;
                            if (frequencyTest != null && !frequencyTest.isBlank()) {
                                completeFunctionNameTest += " " + frequencyTest;
                            }
                            while (c.next()) {
                                if (c.getList().size() == 1 && c.wasAdded() && completeFunctionNameTest.
                                        equals(finalCompleteFunctionName)) {
                                    rangeListView.getSelectionModel().selectFirst();
                                }
                            }
                        }
                    }
                });
            }
        });

        rangeListView.setCellFactory(new Callback<ListView<MeasRangeData>, ListCell<MeasRangeData>>() {
            @Override
            public ListCell<MeasRangeData> call(ListView<MeasRangeData> param) {
                ListCell<MeasRangeData> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(MeasRangeData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getRange() + " " + item.getUnit());
                        }
                    }
                };
                return cell;
            }
        });

        // resets range selection in rangeListView after swapping the selected measurement function:
        functionListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rangeListView.getSelectionModel().selectFirst();
                // TODO: duplicate code (see the listener below)
                if (rangeListView.getSelectionModel().getSelectedItem() != null) {
                    pointList = FXCollections.observableArrayList(rangeListView.getSelectionModel().getSelectedItem().
                            getPoints());
                    pointListView.setItems(pointList);
                    pointListView.getSelectionModel().selectFirst();
                } else {
                    pointListView.setItems(null);
                }
            }
        });

        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> {
            if (rangeListView.getSelectionModel().getSelectedItem() != null) {
                pointList = FXCollections.observableArrayList(rangeListView.getSelectionModel().getSelectedItem().
                        getPoints());
                pointListView.setItems(pointList);
                pointListView.getSelectionModel().selectFirst();
            } else {
                pointListView.setItems(null);
                activeRange = null;
                activePoint = null;
            }
        });

        pointListView.setCellFactory(new Callback<ListView<MeasPointData>, ListCell<MeasPointData>>() {
            @Override
            public ListCell<MeasPointData> call(ListView<MeasPointData> param) {
                ListCell<MeasPointData> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(MeasPointData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getPointValueProperty() + " " + item.getUnitProperty());
                        }
                    }
                };
                return cell;
            }
        });

        pointListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MeasPointData>() {
            @Override
            public void changed(ObservableValue<? extends MeasPointData> observable, MeasPointData oldValue,
                                MeasPointData newValue) {
                if (pointListView.getSelectionModel().getSelectedItem() != null
                        && rangeListView.getSelectionModel().getSelectedItem() != null) {
                    activeRange = rangeListView.getSelectionModel().getSelectedItem();
                    activePoint = pointListView.getSelectionModel().getSelectedItem();

                    dutResSpinner.getValueFactory().setValue(activePoint.getUncertaintyData().getDutResolution());
                    loadReadings(activePoint);

                    dutReadingsVBox.setDisable(false);

                    dutResSpinner.setDisable(false);

                    refStdSelector.setDisable(false);
                    refStdSelector.setValue(activePoint.getUncertaintyData().getRefStandard());
//                    refStdRangeCB.setValue("");

                    refStdRangeSelector.setValue(activePoint.getUncertaintyData().getRefStdRange());
                    refStdRangeSelector.setDisable(false);
                } else {
                    activePoint = null;
                    activeRange = null;

                    clearReadingsTF();
                    dutReadingsVBox.setDisable(true);

                    dutResSpinner.getValueFactory().setValue(0);
                    dutResSpinner.setDisable(true);

                    refStdSelector.setDisable(true);

                    refStdRangeSelector.setValue("");
                    refStdRangeSelector.setDisable(true);
                }
            }
        });

        // range selector change listener
//        refStdRangeSelector.setValue(selectRefStdRange(activePoint, refStdSelector.getValue()));
    }

    public void addRedOutline(Node node) {
        if (node != null) {
            node.setStyle("-fx-border-color: red;");
        }
    }

    private void loadReadings(MeasPointData point) {
        if (point != null) {
            List<String> readings = point.getUncertaintyData().getDutReadings();
            if (readings != null && readings.size() == 10) {
                int i = 0;
                for (String reading : readings) {
                    readingsTFList.get(i).setText(reading);
                    System.out.println("setting text to: " + reading);
                    i++;
                }
            } else {
                System.out.println("BudgetsController -> loadReadings() -> " +
                        "couldn't load readings for this point (" + point.getPointValueProperty() + " "
                        + point.getUnitProperty() + ").");
                clearReadingsTF();
            }
        }
    }

    private void clearReadingsTF() {
        for (TextField textField : readingsTFList) {
            textField.setText("");
        }
    }

    private boolean validateReadings() {
        boolean result = true;
        String reading;
        for (TextField textField : readingsTFList) {
            reading = textField.getText().trim();
            try {
                double readingValue = Double.parseDouble(reading);
            } catch (NumberFormatException e) {
                addRedOutline(textField);
                int index = readingsTFList.indexOf(textField);
                //e.printStackTrace();
                result = false;
            }
        }

        if (!result) {
            System.out.println("BudgetsController -> validateReadings() -> something's wrong.");
        }
        return result;
    }

    private String selectRefStdRange(MeasPointData point, String referenceStandard) {
        String range = null;

        if (point != null) {
            range = RefStdRangePicker.pickRefRange(referenceStandard, point);
        }

        return range;
    }

    private void saveDUTResolution(int resolution, MeasPointData point) {
        if (resolution >= 0 && resolution <= 8 && point != null) {
            point.getUncertaintyData().setDutResolution(resolution);
        }
    }

    private void saveRefStdSelection(String refStandard, MeasPointData point) {
        if (refStandard != null && !refStandard.trim().isBlank() && point != null) {
            point.getUncertaintyData().setRefStandard(refStandard);
        }
    }

    private void saveRefStdRange(String refStdRange, MeasPointData point) {
        if (refStdRange != null && !refStdRange.trim().isBlank() && point != null) {
            point.getUncertaintyData().setRefStdRange(refStdRange);
        }
    }

    private void saveReadings(MeasPointData point) {
        if (point != null) {
            List<String> readings = new ArrayList<>();
            for (TextField textField : readingsTFList) {
                readings.add(textField.getText());
            }
            point.getUncertaintyData().setDutReadings(readings);
        }
    }

    @FXML
    private void calculateButtonHandler() {
        if (activePoint != null) {
            if (validateReadings()) {
                saveReadings(activePoint);
                saveDUTResolution(dutResSpinner.getValueFactory().getValue(), activePoint);
                saveRefStdSelection(refStdSelector.getValue(), activePoint);
                saveRefStdRange(refStdRangeSelector.getValue(), activePoint);
            }
        } else {
            System.out.println("There is currently no active measurement point.");
        }
    }

    // TODO: prep some budget preview
//    private void previewBudget(TableView<MeasRangeData> tableView) {
//        if (tableView != null && !tableView.getSelectionModel().isEmpty()) {
//            MeasRangeData rangeData = tableView.getSelectionModel().getSelectedItem();
//
//            String range = String.valueOf(rangeData.getRange());
//            String unit = rangeData.getUnit();
//            String function = rangeData.getFunctionType().toString();
//
//            ValuationController.SectionContainer container = (ValuationController.SectionContainer) tableView.getUserData();
//            String frequency = null;
//            String frequencyUnit = null;
//
//            StringBuilder headerBuilder = new StringBuilder();
//            headerBuilder.append("Range: ").append(range).append(" ").append(unit);
//            if (function.equalsIgnoreCase("VAC") || function.equalsIgnoreCase("IAC")) {
//                if (container != null) {
//                    frequency = container.getFreqTF().getText();
//                    frequencyUnit = container.getFreqCB().getValue();
//                }
//
//                headerBuilder.append(" (").append(function).append(")\nf = ").append(frequency).append(" ").
//                        append(frequencyUnit);
//            }
//
//            // TODO: duplicate code - dialog initialization
//            // set up the new dialog:
//            Dialog<ButtonType> dialog = new Dialog<>();
//            dialog.initOwner(root.getScene().getWindow());
//            dialog.setTitle("Previewing measurement range");
//            dialog.setHeaderText(headerBuilder.toString());
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("valuation/range-preview.fxml"));
//
//            try {
//                dialog.getDialogPane().setContent(fxmlLoader.load());
//            } catch (IOException e) {
//                System.out.println("Couldn't load the dialog.");
//                e.printStackTrace();
//                return;
//            }
//
//            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//
//            // set up the preview's TableView:
//            RangePreviewController controller = fxmlLoader.getController();
//            // TODO: validation for rangeData
//            controller.loadTableViewData(rangeData);
//            Optional<ButtonType> result = dialog.showAndWait();
//        }
//    }
}
