package ru.lebedeva.librarylabgui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NewController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField yearField;

    @FXML
    protected void onAddBookClick() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            String year = yearField.getText();

            if (!title.isEmpty()) {
                Book newBook = Book.builder()
                        .title(title)
                        .author(author)
                        .genre(genre)
                        .year(year)
                        .build();

                Utils.addBook(newBook);
                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.close();
            } else {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Validation Error");
                validationAlert.setHeaderText("Validation Error");
                validationAlert.setContentText("Book title is required!");
                Utils.styleDialog(validationAlert);
                validationAlert.showAndWait();           }
        } catch (Exception e) {
            ErrorNotificUtil.showErrorAlert("Error", "Could not add book: " + e.getMessage());
        }
    }
}
