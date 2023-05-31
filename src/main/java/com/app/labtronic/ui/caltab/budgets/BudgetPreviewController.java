package com.app.labtronic.ui.caltab.budgets;

import com.app.labtronic.data.budgets.UncertaintyData;
import com.app.labtronic.data.valuation.MeasPointData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BudgetPreviewController {
    @FXML
    private DialogPane root;
    @FXML
    private TableView<UncertaintyData> tableView;

    private ObservableList<UncertaintyData> uncertaintyComponents;

    @FXML
    private void initialize() {
        uncertaintyComponents = FXCollections.observableArrayList();

        String[] measRangeDataFields = {"pointValueProperty", "unitProperty"};
        tableView.setItems(uncertaintyComponents);

        int i = 0;
        for (TableColumn<MeasPointData, ?> column : tableView.getColumns()) {
            column.prefWidthProperty().bind(tableView.widthProperty().divide(2));
            column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
            i++;
        }
    }
}
