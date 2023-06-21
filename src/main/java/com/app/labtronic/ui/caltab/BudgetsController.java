package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasRangeData;
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
    private ListView<String> activeFunctionsListView;
    @FXML
    private ListView<MeasRangeData> activeFunctionRangesLV;
    @FXML
    private Button testButton;


    private CalData calData;
    private ObservableList<String> activeFunctionsList;
    private ObservableList<MeasRangeData> activeRange;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());
        activeFunctionsList = calData.getValuationData().getActiveMeasFunctions();

        activeFunctionsListView.setItems(activeFunctionsList);

        activeFunctionsList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                while (c.next()) {
                    if (c.wasAdded() && c.getList().size() == 1) {
                        activeFunctionsListView.getSelectionModel().selectFirst();
                    }
                }
            }
        });

        activeFunctionsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> {
//            String function = newValue.getSelectedItem();
            if (observable.getValue().equals("VDC")) {
                System.out.println("test");
                activeFunctionRangesLV.setItems(calData.getValuationData().getRangeList("VDC"));
            } else if (observable.getValue().equals("IDC")) {
                System.out.println("test2");
                activeFunctionRangesLV.setItems(calData.getValuationData().getRangeList("IDC"));
            }
        });

        activeFunctionRangesLV.setCellFactory(new Callback<ListView<MeasRangeData>, ListCell<MeasRangeData>>() {
            @Override
            public ListCell<MeasRangeData> call(ListView<MeasRangeData> param) {
                ListCell<MeasRangeData> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(MeasRangeData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(String.valueOf(item.getRange()));
                        }
                    }
                };
                return cell;
            }
        });

        // template
//        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
//            @Override
//            public ListCell<TodoItem> call(ListView<TodoItem> param) {
//                ListCell<TodoItem> cell = new ListCell<>() {
//                    @Override
//                    protected void updateItem(TodoItem item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setText(null);
//                        } else {
//                            setText(item.getShortDescription());
//                            if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
//                                setTextFill(Color.RED);
//                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
//                                setTextFill(Color.ORANGE);
//                            }
//                        }
//                    }
//                };
//
//                // associating the context menu with the non-empty cells
//                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
//                    if (isNowEmpty) {
//                        cell.setContextMenu(null);
//                    } else {
//                        cell.setContextMenu(listContextMenu);
//                    }
//                });
//
//                return cell;
//            }
//        });
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
