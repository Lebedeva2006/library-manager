module ru.lebedeva.librarylabgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;


    opens ru.lebedeva.librarylabgui to javafx.fxml;
    exports ru.lebedeva.librarylabgui;
}