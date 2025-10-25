package ru.lebedeva.librarylabgui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SearchController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;

    private HelloController mainController;

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    protected void onSearchClick() {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        String year = yearField.getText();

        Book searchCriteria = Book.builder()
                .title(title.isEmpty() ? null : title)
                .author(author.isEmpty() ? null : author)
                .genre(genre.isEmpty() ? null : genre)
                .year(year.isEmpty() ? null : year)
                .build();

        ArrayList<Book> foundBooks = Utils.findBooksByArguments(searchCriteria);

        mainController.displaySearchResults(foundBooks, searchCriteria);

        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
