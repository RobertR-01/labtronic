package com.app.labtronic.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainWindowController {
    @FXML
    private BorderPane rootPane;

    private SortedMap<Integer, Tab> tabMap;

    @FXML
    private void initialize() {
        tabMap = new TreeMap<>();
        removeLastFocus();
    }

    @FXML
    private void handleToolbarOnMouseEntered(Event e) {
        ((Button) e.getSource()).setId(null);
        ((Button) e.getSource()).setStyle(null);
    }

    @FXML
    private void handleToolbarOnMouseExited(Event e) {
        ((Button) e.getSource()).setId("toolbarButton");
    }

    @FXML
    private void handleNewButton() {
        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(rootPane.getScene().getWindow());
        dialog.setTitle("New calibration");
        dialog.setHeaderText("Enter basic information:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("new-calibration-dialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        /*
        NewCalibrationDialogController controller = fxmlLoader.getController();
        // event filter for input validation:
        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        buttonOK.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            // Check whether some conditions are fulfilled
            if (controller.validateNameArgument() == null) {
                // the TextField contents are prohibited, so we consume the event
                // to prevent the dialog from closing - forces the user to retry entering data
                actionEvent.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("New calibration initialization error");
                alert.setHeaderText("Invalid calibration registry number!");
                alert.setContentText("The registry number for the calibration cannot be left void.");
                alert.showAndWait();
            }
        });
         */

        NewCalibrationDialogController controller = fxmlLoader.getController();

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // TODO: add some real dialog results processing
            // controller.processTextInput();

            // creating a new calibration tab:
            Tab newCalibrationTab = new Tab();
            if (rootPane.getCenter() == null) {
                TabPane tabPane = new TabPane();
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
                rootPane.setCenter(tabPane);
            }

            StringBuilder calibrationTabTitle;
            if (!controller.getKubackiRegistryNumber().isEmpty()) {
                calibrationTabTitle = new StringBuilder(controller.getKubackiRegistryNumber());
            } else {
                calibrationTabTitle = new StringBuilder("new1");
                // new default tab title setup:
                List<Tab> tabs = ((TabPane) rootPane.centerProperty().get()).getTabs();
                int tabTitleIndex = 1;
                boolean wasTitleChanged = true;
                while (wasTitleChanged) {
                    wasTitleChanged = false;
                    for (Tab t : tabs) {
                        if (t.getText().equals(calibrationTabTitle.toString())) {
                            calibrationTabTitle = new StringBuilder("new").append(tabTitleIndex++);
                            wasTitleChanged = true;
                        }
                    }
                }
            }

            newCalibrationTab.setText(calibrationTabTitle.toString());
            ((TabPane) rootPane.centerProperty().get()).getTabs().add(newCalibrationTab);
            ((TabPane) rootPane.centerProperty().get()).getSelectionModel().select(newCalibrationTab);
            // TODO: test if the focus removal works
            removeLastFocus();
        }
    }

    private void removeLastFocus() {
        Platform.runLater(() -> rootPane.requestFocus());
    }
}
