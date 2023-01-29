package com.app.labtronic.ui;

import javafx.application.Platform;
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

public class MainWinController {
    @FXML
    private BorderPane root;

    private SortedMap<Integer, Tab> tabs;

    @FXML
    private void initialize() {
        tabs = new TreeMap<>();
        removeFocus();
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

        /*
        NewCalDlgController controller = fxmlLoader.getController();
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

        NewCalDlgController controller = fxmlLoader.getController();

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // TODO: add some real dialog results processing
            // controller.processTextInput();

            // creating a new calibration tab:
            Tab newCalTab = new Tab();
            if (root.getCenter() == null) {
                TabPane tabPane = new TabPane();
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
                root.setCenter(tabPane);
            }

            StringBuilder calTabTitle;
            if (!controller.getKubackiRegNo().isEmpty()) {
                calTabTitle = new StringBuilder(controller.getKubackiRegNo());
            } else {
                calTabTitle = new StringBuilder("new1");
                // new default tab title setup:
                List<Tab> tabs = ((TabPane) root.centerProperty().get()).getTabs();
                int tabTitleIndex = 1;
                boolean cont = true;
                while (cont) {
                    cont = false;
                    for (Tab t : tabs) {
                        if (t.getText().equals(calTabTitle.toString())) {
                            calTabTitle = new StringBuilder("new").append(tabTitleIndex++);
                            cont = true;
                        }
                    }
                }
            }

            newCalTab.setText(calTabTitle.toString());
            ((TabPane) root.centerProperty().get()).getTabs().add(newCalTab);
            ((TabPane) root.centerProperty().get()).getSelectionModel().select(newCalTab);
            // TODO: test if the focus removal works
            removeFocus();
        }
    }

    private void removeFocus() {
        Platform.runLater(() -> root.requestFocus());
    }
}
