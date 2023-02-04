module com.app.labtronic {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.boxicons;

    opens com.app.labtronic to javafx.fxml;
    exports com.app.labtronic;
    exports com.app.labtronic.ui;
    opens com.app.labtronic.ui to javafx.fxml;

    exports com.app.labtronic.data;
    exports com.app.labtronic.ui.caltab;
    opens com.app.labtronic.ui.caltab to javafx.fxml;
}