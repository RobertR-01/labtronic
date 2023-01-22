package com.app.labtronic.ui;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.util.List;
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
        StringBuilder tabTitle = new StringBuilder("new1");
        Tab newTab = new Tab();

        if (rootPane.getCenter() == null) {
            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
            rootPane.setCenter(tabPane);
        }

        // new tab title setup:
        List<Tab> tabs = ((TabPane) rootPane.centerProperty().get()).getTabs();
        int tabTitleIndex = 1;
        boolean wasTitleChanged = true;
        while (wasTitleChanged) {
            wasTitleChanged = false;
            for (Tab t : tabs) {
                if (t.getText().equals(tabTitle.toString())) {
                    tabTitle = new StringBuilder("new").append(tabTitleIndex++);
                    wasTitleChanged = true;
                }
            }
        }

        newTab.setText(tabTitle.toString());
        ((TabPane) rootPane.centerProperty().get()).getTabs().add(newTab);
        ((TabPane) rootPane.centerProperty().get()).getSelectionModel().select(newTab);
        removeLastFocus();
    }

    private void removeLastFocus() {
        Platform.runLater(() -> rootPane.requestFocus());
    }
}
