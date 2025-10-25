package ru.lebedeva.librarylabgui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;

    private Book bookToEdit;
    private int bookIndex;

    public void setBookToEdit(Book book, int index) {
        this.bookToEdit = book;
        this.bookIndex = index;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreField.setText(book.getGenre());
        yearField.setText(book.getYear());
    }

    @FXML
    protected void onSaveBookClick() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            String year = yearField.getText();

            if (!title.isEmpty()) {
                Book updatedBook = Book.builder()
                        .title(title)
                        .author(author)
                        .genre(genre)
                        .year(year)
                        .build();

                Utils.getListOfBooks().set(bookIndex, updatedBook);
                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.close();
            } else {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Validation Error");
                validationAlert.setHeaderText("Validation Error");
                validationAlert.setContentText("Book title is required!");

                Utils.styleDialog(validationAlert);
                validationAlert.showAndWait();
            }
        } catch (Exception e) {
            ErrorNotificUtil.showErrorAlert("Error", "Could not save book: " + e.getMessage());
        }
    }
}
