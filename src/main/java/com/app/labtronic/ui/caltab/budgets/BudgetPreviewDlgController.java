package com.app.labtronic.ui.caltab.budgets;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.budgets.UncertaintyComponent;
import com.app.labtronic.data.budgets.UncertaintyData;
import com.app.labtronic.data.valuation.MeasPointData;
import com.app.labtronic.data.valuation.MeasRangeData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class BudgetPreviewDlgController {
    @FXML
    private DialogPane root;
    @FXML
    private TableView<UncertaintyComponent> tableView;

    private CalData calData;
    private MeasRangeData measRange;
    private MeasPointData measPoint;
    private UncertaintyData pointUncertaintyData;
    private ObservableList<UncertaintyComponent> uncertaintyComponents;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        uncertaintyComponents = FXCollections.observableArrayList();

        String[] measRangeDataFields = {"symbol", "estimatedValue", "unit", "stdUncertainty", "probabilityDistribution",
                "sensitivityCoefficient", "convertedUncertainty", "degreesOfFreedom", "significance"};
        tableView.setItems(uncertaintyComponents);

        int i = 0;
        for (TableColumn<UncertaintyComponent, ?> column : tableView.getColumns()) {
//            column.prefWidthProperty().bind(tableView.widthProperty().divide(2));
            column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
            i++;
        }
    }

    public void loadPointData(MeasRangeData rangeData, MeasPointData pointData) {
        if (rangeData != null && pointData != null) {
            measRange = rangeData;
            measPoint = pointData;

            List<UncertaintyComponent> components = pointData.getUncertaintyData().getComponents();
            uncertaintyComponents.addAll(components);
        }
    }
}
