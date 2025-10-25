package ru.lebedeva.librarylabgui;

import javafx.scene.control.Alert;

public class ErrorNotificUtil {

    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        Utils.styleDialog(alert);
        alert.showAndWait();
    }
}