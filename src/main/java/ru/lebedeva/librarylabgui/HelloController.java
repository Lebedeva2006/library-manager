package ru.lebedeva.librarylabgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static ru.lebedeva.librarylabgui.Utils.styleDialog;


public class HelloController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String oneTimeString = "WELCOME TO MY LIBRARY";
        welcomeText.setEditable(false);
        welcomeText.setText(oneTimeString);

    }

    @FXML
    private TextArea welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onNewClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("newBook-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Add a book");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorNotificUtil.showErrorAlert("Error", "Could not open book creation window: " + e.getMessage());
        }
    }
    @FXML
    protected void ShowListOfBooks(){
        try {
            ArrayList<Book> currentList = Utils.getListOfBooks();
            String result = "";
            for(Book book : currentList){
                result += "Title: " + book.getTitle();
                result += "  Author: " + book.getAuthor();
                result += "  Genre: " + book.getGenre();
                result += "  Year: " + book.getYear() + "\n";
            }
            welcomeText.setText(result);
        } catch (Exception e) {
            ErrorNotificUtil.showErrorAlert("Error", "Could not load book list: " + e.getMessage());
        }

    }
    @FXML
    protected void onEditClick() {
        try {
            ArrayList<Book> books = Utils.getListOfBooks();

            if (books.isEmpty()) {
                ErrorNotificUtil.showErrorAlert("No Books", "There are no books to edit!");
                return;
            }
            ChoiceDialog<Book> dialog = new ChoiceDialog<>(books.get(0), books);
            dialog.setTitle("Select Book to Edit");
            dialog.setHeaderText("Choose a book to edit:");
            dialog.setContentText("Book:");
            styleDialog(dialog);

            Optional<Book> result = dialog.showAndWait();
            result.ifPresent(book -> {
                int index = books.indexOf(book);
                openEditWindow(book, index);
            });
        } catch (Exception e) {
            ErrorNotificUtil.showErrorAlert("Error", "Could not open book selection: " + e.getMessage());
        }
    }

    private void openEditWindow(Book book, int index) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("editBook-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EditController editController = fxmlLoader.getController();
        editController.setBookToEdit(book, index);

        Stage stage = new Stage();
        stage.setTitle("Edit Book");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onSearchByKeywordClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search by Keyword");
        dialog.setHeaderText("Enter search keyword:");
        dialog.setContentText("Keyword:");
        styleDialog(dialog);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(keyword -> {
            if (!keyword.isEmpty()) {
                ArrayList<Book> foundBooks = Utils.findBooksByKeyword(keyword);
                if (!foundBooks.isEmpty()) {
                    displayBooks(foundBooks, "Search results for: '" + keyword + "'");
                } else {
                    ErrorNotificUtil.showErrorAlert("No Results", "No books found for '" + keyword + "'");
                }
            } else {
                ErrorNotificUtil.showErrorAlert("Empty Keyword", "Please enter a keyword to search");
            }
        });
    }


    @FXML
    protected void onSearchByArgumentsClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("searchBook-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchController searchController = fxmlLoader.getController();
        searchController.setMainController(this);
        Stage stage = new Stage();
        stage.setTitle("Search by Arguments");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onSaveToJsonClick() {
        TextInputDialog dialog = new TextInputDialog("books.json");
        dialog.setTitle("Save to JSON");
        dialog.setHeaderText("Enter filename:");
        dialog.setContentText("Filename:");

        styleDialog(dialog);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            if (!filename.isEmpty()) {
                if (!filename.endsWith(".json")) {
                    filename += ".json";
                }
                boolean success = Utils.saveToJsonFile(filename);
                if (success) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Success");
                    successAlert.setContentText("Books saved to " + filename);
                    Utils.styleDialog(successAlert);
                    successAlert.showAndWait();
                }
            } else {
                ErrorNotificUtil.showErrorAlert("Empty Filename", "Please enter a filename");
            }
        });
    }

    @FXML
    protected void onLoadFromJsonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load from JSON");
        dialog.setHeaderText("Enter file path:");
        dialog.setContentText("File path:");

        styleDialog(dialog);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filePath -> {
            if (!filePath.isEmpty()) {
                boolean success = Utils.loadFromJsonFile(filePath);
                if (success) {
                    ShowListOfBooks();
                    ErrorNotificUtil.showErrorAlert("Success", "Books loaded from " + filePath);
                }
            } else {
                ErrorNotificUtil.showErrorAlert("Empty Path", "Please enter a file path");
            }
        });
    }

    private void displayBooks(ArrayList<Book> books, String header) {
        String result = header + "\n\n";
        for (Book book : books) {
            result += "Title: " + book.getTitle();
            result += "  Author: " + book.getAuthor();
            result += "  Genre: " + book.getGenre();
            result += "  Year: " + book.getYear() + "\n";
        }
        welcomeText.setText(result);
    }

    public void displaySearchResults(ArrayList<Book> foundBooks, Book criteria) {
        if (!foundBooks.isEmpty()) {
            String criteriaText = buildCriteriaText(criteria);
            displayBooks(foundBooks, "Found " + foundBooks.size() + " book(s) for " + criteriaText + ":");
        } else {
            ErrorNotificUtil.showErrorAlert("No Results", "No books found for the specified criteria");
        }
    }

    private String buildCriteriaText(Book criteria) {
        List<String> activeCriteria = new ArrayList<>();
        if (criteria.getTitle() != null && !criteria.getTitle().isEmpty())
            activeCriteria.add("title: '" + criteria.getTitle() + "'");
        if (criteria.getAuthor() != null && !criteria.getAuthor().isEmpty())
            activeCriteria.add("author: '" + criteria.getAuthor() + "'");
        if (criteria.getGenre() != null && !criteria.getGenre().isEmpty())
            activeCriteria.add("genre: '" + criteria.getGenre() + "'");
        if (criteria.getYear() != null && !criteria.getYear().isEmpty())
            activeCriteria.add("year: '" + criteria.getYear() + "'");

        return String.join(", ", activeCriteria);
    }

    @FXML
    protected void onExitClick() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Exit Application");
        confirmation.setHeaderText("Are you sure you want to exit?");
        confirmation.setContentText("Any unsaved changes will be lost.");
        styleDialog(confirmation);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.close();
        }
    }



}
