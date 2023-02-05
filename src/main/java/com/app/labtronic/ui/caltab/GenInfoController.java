package com.app.labtronic.ui.caltab;

import com.app.labtronic.data.ActiveSession;
import com.app.labtronic.data.CalData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class GenInfoController {
    @FXML
    private BorderPane root;
    @FXML
    private TextField kubackiRegNoTF;
    @FXML
    private TextField switezRegNoTF;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox accreditationCB;

    @FXML
    private Label test;

    private CalData calData;

    @FXML
    private void initialize() {
//        System.out.println(root.getParent());
//        root.setPrefSize(root.getParent().);
        calData = ActiveSession.getActiveSessionInstance().getActiveCalTabs().get(ActiveSession.getLastAddedId());

        kubackiRegNoTF.setText(calData.getKubackiRegNo());
        switezRegNoTF.setText(calData.getSwitezRegNo());
        datePicker.setValue(calData.getOrderDate());
        accreditationCB.setSelected(calData.isAccreditation());

        test.setText("""
                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                 sed do eiusmod tempor incididunt ut labore et dolore magna 
                 aliqua. Dignissim cras tincidunt lobortis feugiat vivamus. 
                 Arcu risus quis varius quam quisque id diam. Accumsan sit amet
                  nulla facilisi. Sagittis vitae et leo duis ut. Egestas
                   maecenas pharetra convallis posuere morbi leo urna molestie. 
                   Pellentesque habitant morbi tristique senectus et netus 
                   et malesuada. Eleifend donec pretium vulputate sapien nec
                    sagittis aliquam malesuada. Sed sed risus pretium quam vu
                    lputate dignissim suspendisse in. Elementum curabitur vit
                    ae nunc sed velit dignissim. Sit amet facilisis magna etia
                    m tempor orci eu lobortis. A lacus vestibulum sed arcu non
                     odio euismod lacinia at. Ut diam quam nulla porttitor mas
                     sa. Eleifend donec pretium vulputate sapien nec sagittis 
                     aliquam. Amet venenatis urna cursus eget nunc. Facilisis vo
                     lutpat est velit egestas. Ac placerat vestibulum lectus mau
                     ris ultrices eros in cursus turpis. Nec nam aliquam sem e
                     t tortor consequat id porta nibh. Bibendum est ultricies i
                     nteger quis.
                       Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                 sed do eiusmod tempor incididunt ut labore et dolore magna 
                 aliqua. Dignissim cras tincidunt lobortis feugiat vivamus. 
                 Arcu risus quis varius quam quisque id diam. Accumsan sit amet
                  nulla facilisi. Sagittis vitae et leo duis ut. Egestas
                   maecenas pharetra convallis posuere morbi leo urna molestie. 
                   Pellentesque habitant morbi tristique senectus et netus 
                   et malesuada. Eleifend donec pretium vulputate sapien nec
                    sagittis aliquam malesuada. Sed sed risus pretium quam vu
                    lputate dignissim suspendisse in. Elementum curabitur vit
                    ae nunc sed velit dignissim. Sit amet facilisis magna etia
                    m tempor orci eu lobortis. A lacus vestibulum sed arcu non
                     odio euismod lacinia at. Ut diam quam nulla porttitor mas
                     sa. Eleifend donec pretium vulputate sapien nec sagittis 
                     aliquam. Amet venenatis urna cursus eget nunc. Facilisis vo
                     lutpat est velit egestas. Ac placerat vestibulum lectus mau
                     ris ultrices eros in cursus turpis. Nec nam aliquam sem e
                     t tortor consequat id porta nibh. Bibendum est ultricies i
                     nteger quis.Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                 sed do eiusmod tempor incididunt ut labore et dolore magna 
                 aliqua. Dignissim cras tincidunt lobortis feugiat vivamus. 
                 Arcu risus quis varius quam quisque id diam. Accumsan sit amet
                  nulla facilisi. Sagittis vitae et leo duis ut. Egestas
                   maecenas pharetra convallis posuere morbi leo urna molestie. 
                   Pellentesque habitant morbi tristique senectus et netus 
                   et malesuada. Eleifend donec pretium vulputate sapien nec
                    sagittis aliquam malesuada. Sed sed risus pretium quam vu
                    lputate dignissim suspendisse in. Elementum curabitur vit
                    ae nunc sed velit dignissim. Sit amet facilisis magna etia
                    m tempor orci eu lobortis. A lacus vestibulum sed arcu non
                     odio euismod lacinia at. Ut diam quam nulla porttitor mas
                     sa. Eleifend donec pretium vulputate sapien nec sagittis 
                     aliquam. Amet venenatis urna cursus eget nunc. Facilisis vo
                     lutpat est velit egestas. Ac placerat vestibulum lectus mau
                     ris ultrices eros in cursus turpis. Nec nam aliquam sem e
                     t tortor consequat id porta nibh. Bibendum est ultricies i
                     nteger quis.
                                
                .pen""");
    }

    @FXML
    private void test() {
//        root.styleProperty().set("-fx-border-style: solid; -fx-border-width: 5");

//        System.out.println(root.widthProperty().get());
//        System.out.println(root.heightProperty().get());

    }
}
