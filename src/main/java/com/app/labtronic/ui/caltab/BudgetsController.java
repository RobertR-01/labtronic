package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasPointData;
import com.app.labtronic.data.valuation.MeasRangeData;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

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
    private Button testButton;


    private CalData calData;
    private ObservableList<String> functionList;
    private ObservableList<MeasRangeData> rangeList;
    private ObservableList<MeasPointData> pointList;
    private MeasRangeData activeRange;
    private MeasPointData activePoint;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());
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
            if (observable != null) {
                String[] functionArray = observable.getValue().split(" ");
                String measFunction = functionArray[0];
                rangeList = calData.getValuationData().getRangeList(measFunction);

                if (functionArray.length > 1) {
                    String frequency = functionArray[1] + " " + functionArray[2];
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

        /*
        // to make the list view always have something selected unless empty
        // TODO: duplicate code from the above
        rangeList.addListener(new ListChangeListener<MeasRangeData>() {
            @Override
            public void onChanged(Change<? extends MeasRangeData> c) {
                while (c.next()) {
                    if (c.wasAdded() && c.getList().size() == 1) {
                        rangeListView.getSelectionModel().selectFirst();
                    }
                }
            }
        });

        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> {
            pointList = FXCollections.observableArrayList(rangeListView.getSelectionModel().getSelectedItem().
                    getPoints());
            pointListView.setItems(pointList);
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
        */
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
