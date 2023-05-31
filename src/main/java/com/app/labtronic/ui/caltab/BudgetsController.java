package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import com.app.labtronic.data.valuation.MeasRangeData;
import com.app.labtronic.ui.caltab.valuation.RangePreviewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class BudgetsController {
    @FXML
    private BorderPane root;

    private CalData calData;

    @FXML
    private void initialize() {
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());
    }

    private void previewBudget(TableView<MeasRangeData> tableView) {
        if (tableView != null && !tableView.getSelectionModel().isEmpty()) {
            MeasRangeData rangeData = tableView.getSelectionModel().getSelectedItem();

            String range = String.valueOf(rangeData.getRange());
            String unit = rangeData.getUnit();
            String function = rangeData.getFunctionType().toString();

            ValuationController.SectionContainer container = (ValuationController.SectionContainer) tableView.getUserData();
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
}
