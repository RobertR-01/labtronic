package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.valuation.ValuationData;
import com.app.labtronic.ui.caltab.valuation.ValuationDlgController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class ValuationController {
    @FXML
    private BorderPane root;
    @FXML
    private Button addBtn;

    @FXML
    private TableView<ValuationData> vdcTableView;

    @FXML
    private void initialize() {
        // sets equal column width:
        System.out.println(vdcTableView.widthProperty().get());
        for (TableColumn<ValuationData, ?> column : vdcTableView.getColumns()) {
            column.prefWidthProperty().bind(vdcTableView.widthProperty().divide(5));
        }
    }

    // TODO: lots of duplicate code from NewCalDlgController
    @FXML
    private void handleAddBtn() {
        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.setTitle("Adding new valuation section");
        dialog.setHeaderText("Enter pricing related information:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("valuation/valuation-dlg.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ValuationDlgController controller = fxmlLoader.getController();
        // event filter for input validation - consumes the OK event to prevent the dialog from closing in case of
        // an invalid input (forces the user to retry entering missing data):
//        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
//        buttonOK.addEventFilter(ActionEvent.ACTION, actionEvent -> {
//            if (!controller.validateForm()) {
//                actionEvent.consume();
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Valuation related error");
//                alert.setHeaderText("Missing input data!");
//                alert.setContentText("No fields in the form may remain empty.");
//                alert.showAndWait();
//            }
//        });

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
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
//        }
    }
}
