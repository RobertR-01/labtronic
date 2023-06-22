package com.app.labtronic.ui.caltab.valuation;

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

public class RangePreviewController {
    @FXML
    private DialogPane root;
    @FXML
    private TableView<MeasPointData> tableView;

    private ObservableList<MeasPointData> pointsObservableList;

    @FXML
    private void initialize() {
        pointsObservableList = FXCollections.observableArrayList();

        String[] measRangeDataFields = {"pointValueProperty", "unitProperty"};
        tableView.setItems(pointsObservableList);

        int i = 0;
        for (TableColumn<MeasPointData, ?> column : tableView.getColumns()) {
            column.prefWidthProperty().bind(tableView.widthProperty().divide(2));
            column.setCellValueFactory(new PropertyValueFactory<>(measRangeDataFields[i]));
            i++;
        }
    }

    // TODO: validation?
    public void loadTableViewData(MeasRangeData rangeData) {
        if (rangeData != null) {
            List<MeasPointData> points = rangeData.getPoints();
            for (MeasPointData point : points) {
                pointsObservableList.add(new MeasPointData(point.getPointValueProperty(), rangeData.getUnit()));
            }
        }
    }
}
