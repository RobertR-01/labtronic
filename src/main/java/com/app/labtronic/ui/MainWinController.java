package com.app.labtronic.ui;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainWinController {
    @FXML
    private BorderPane root;

    private List<Tab> calTabs;
    private TabPane tabPane;

    @FXML
    private void initialize() {
        removeFocus();

        calTabs = new ArrayList<>();
    }

    @FXML
    private void handleBtnMouseEnter(Event e) {
        ((Button) e.getSource()).getStyleClass().clear();
        ((Button) e.getSource()).getStyleClass().add("button");
        ((Button) e.getSource()).setStyle(null);
    }

    @FXML
    private void handleBtnMouseExit(Event e) {
        ((Button) e.getSource()).getStyleClass().add("toolbarBtn");
    }

    @FXML
    private void handleNewBtn() {
        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.setTitle("New calibration");
        dialog.setHeaderText("Enter basic information:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("new-cal-dlg.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewCalDlgController controller = fxmlLoader.getController();
        // TODO: make DatePicker editable and write some date format validation to prevent exceptions
        // event filter for input validation - consumes the OK event to prevent the dialog from closing in case of
        // an invalid input (forces the user to retry entering missing data):
        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        buttonOK.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            if (!controller.validateForm()) {
                actionEvent.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("New calibration initialization error");
                alert.setHeaderText("Missing input data!");
                alert.setContentText("No fields in the form may remain empty.");
                alert.showAndWait();
            }
        });

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // creating a new calibration tab:
            Tab newCalTab = new Tab();
            if (root.getCenter() == null) {
                tabPane = new TabPane();
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
                root.setCenter(tabPane);
            }

            newCalTab.setText(controller.getFullKubackiRegNo());
            ((TabPane) root.centerProperty().get()).getTabs().add(newCalTab);
            ((TabPane) root.centerProperty().get()).getSelectionModel().select(newCalTab);
            calTabs.add(newCalTab);
            // TODO: test if the focus removal works
            removeFocus();
            CalData calData = controller.exportFormData();
            ActiveSession.getActiveSessionInstance().addCalData(calData); // TODO: WIP

            // TODO: keep different instances of CalData, tabs data etc. in a singleton or some other separate class?
            // initializing tab contents:
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("cal-tab.fxml"));
//            fxmlLoader.setLocation(MainApp.class.getResource("cal-tab.fxml"));
            try {
                calTabs.get(calTabs.size() - 1).setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Couldn't load the FXML for the Tab.");
                e.printStackTrace();
            }
        }
    }

    private void removeFocus() {
        Platform.runLater(() -> root.requestFocus());
    }
}
