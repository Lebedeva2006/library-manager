package ru.lebedeva.librarylabgui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Utils {
    private static ArrayList<Book> listOfBooks = startupSetListBooks();

    public static ArrayList<Book> getListOfBooks() {
        return listOfBooks;
    }

    public static void addBook(Book book) {
        listOfBooks.add(book);
    }

    public static ArrayList<Book> startupSetListBooks(){
        ArrayList<Book> listOfBooks = new ArrayList<>();
        listOfBooks.add(Book.builder()
                .title("The Hunger Games")
                .author("Suzanne Collins")
                .genre("Dystopia")
                .year("2008")
                .build());
        listOfBooks.add(Book.builder()
                .title("Catching Fire")
                .author("Suzanne Collins")
                .genre("Dystopia")
                .year("2009")
                .build());
        listOfBooks.add(Book.builder()
                .title("Mockingjay")
                .author("Suzanne Collins")
                .genre("Dystopia")
                .year("2010")
                .build());
        listOfBooks.add(Book.builder()
                .title("The Ballad of Songbirds and Snakes")
                .author("Suzanne Collins")
                .genre("Dystopia")
                .year("2020")
                .build());
        listOfBooks.add(Book.builder()
                .title("Sunrise on the Reaping")
                .author("Suzanne Collins")
                .genre("Dystopia")
                .year("2025")
                .build());

        return listOfBooks;
    }

    public static ArrayList<Book> findBooksByKeyword(String keyword) {
        ArrayList<Book> result = new ArrayList<>();
        for(Book book : listOfBooks){
            if((book.getTitle() != null && book.getTitle().toLowerCase().contains(keyword.toLowerCase())) ||
                    (book.getYear() != null && book.getYear().toLowerCase().contains(keyword.toLowerCase())) ||
                    (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) ||
                    (book.getGenre() != null && book.getGenre().toLowerCase().contains(keyword.toLowerCase()))){
                result.add(book);
            }
        }
        return result;
    }

    public static ArrayList<Book> findBooksByArguments(Book criteria) {
        ArrayList<Book> result = new ArrayList<>();
        for(Book book : listOfBooks){
            Boolean isTitle = false;
            if(criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
                if(book.getTitle() != null && book.getTitle().toLowerCase().contains(criteria.getTitle().toLowerCase())){
                    isTitle = true;
                } else {
                    isTitle = false;
                }
            } else  {
                isTitle = true;
            }

            Boolean isGenre = false;
            if(criteria.getGenre() != null && !criteria.getGenre().isEmpty()) {
                if(book.getGenre() != null && book.getGenre().toLowerCase().contains(criteria.getGenre().toLowerCase())){
                    isGenre = true;
                } else {
                    isGenre = false;
                }
            } else  {
                isGenre = true;
            }
            Boolean isAuthor = false;
            if(criteria.getAuthor() != null && !criteria.getAuthor().isEmpty()) {
                if(book.getAuthor() != null && book.getAuthor().toLowerCase().contains(criteria.getAuthor().toLowerCase())){
                    isAuthor = true;
                } else {
                    isAuthor = false;
                }
            } else  {
                isAuthor = true;
            }

            Boolean isYear = false;
            if(criteria.getYear() != null && !criteria.getYear().isEmpty()) {
                if(book.getYear() != null && book.getYear().toLowerCase().contains(criteria.getYear().toLowerCase())){
                    isYear = true;
                } else {
                    isYear = false;
                }
            } else  {
                isYear = true;
            }

            if(isYear && isTitle && isGenre && isAuthor){
                result.add(book);
            }
        }
        return result;
    }

    public static boolean saveToJsonFile(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            File file = new File(dataDir, filename);

            if (!dataDir.canWrite()) {
                ErrorNotificUtil.showErrorAlert("Write Error", "No permission to write in directory: " + dataDir.getAbsolutePath());
                return false;
            }

            if (file.exists() && !file.canWrite()) {
                ErrorNotificUtil.showErrorAlert("Write Error", "No permission to overwrite file: " + file.getAbsolutePath());
                return false;
            }

            objectMapper.writeValue(file, listOfBooks);
            return true;

        } catch (IOException e) {
            ErrorNotificUtil.showErrorAlert("Save Error", "Could not save books to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean loadFromJsonFile(String filePath) {
        try {
            if (!filePath.toLowerCase().endsWith(".json")) {
                ErrorNotificUtil.showErrorAlert("Format Error", "File must be in JSON format (.json)");
                return false;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(filePath);

            if (!file.exists() && !filePath.contains(File.separator)) {
                file = new File("data", filePath);
            }

            if (!file.exists()) {
                ErrorNotificUtil.showErrorAlert("File Not Found", "File not found: " + file.getAbsolutePath());
                return false;
            }

            if (file.length() == 0) {
                ErrorNotificUtil.showErrorAlert("Empty File", "The file is empty: " + filePath);
                return false;
            }

            ArrayList<Book> loadedBooks = objectMapper.readValue(
                    file,
                    new TypeReference<ArrayList<Book>>() {
                    }
            );
            for (Book newBook : loadedBooks) {
                if (!containsBook(listOfBooks, newBook)) {
                    listOfBooks.add(newBook);
                }
            }

            return true;

        } catch (IOException e) {
            ErrorNotificUtil.showErrorAlert("Load Error", "Could not load books from file: " + e.getMessage());
            return false;
        }
    }

    private static boolean containsBook(ArrayList<Book> books, Book targetBook) {
        for (Book book : books) {
            if (areBooksEqual(book, targetBook)) {
                return true;
            }
        }
        return false;
    }

    private static boolean areBooksEqual(Book book1, Book book2) {
        return Objects.equals(book1.getTitle(), book2.getTitle()) &&
                Objects.equals(book1.getAuthor(), book2.getAuthor()) &&
                Objects.equals(book1.getGenre(), book2.getGenre()) &&
                Objects.equals(book1.getYear(), book2.getYear());
    }

    public static void styleDialog(Dialog<?> dialog) {
        DialogPane dialogPane = dialog.getDialogPane();

        dialogPane.setStyle(
                "-fx-background-color: #000000; " +
                        "-fx-text-fill: #ffff00; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #ffff00; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;"
        );

        for (ButtonType buttonType : dialogPane.getButtonTypes()) {
            dialogPane.lookupButton(buttonType).setStyle(
                    "-fx-background-color: #8b008b; " +
                            "-fx-text-fill: #ffff00; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-color: #ffff00; " +
                            "-fx-border-width: 1; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-radius: 10; " +
                            "-fx-padding: 5 15 5 15;"
            );
        }

        TextField textField = findTextField(dialogPane);
        if (textField != null) {
            textField.setStyle(
                    "-fx-background-color: #000000; " +
                            "-fx-text-fill: #ffff00; " +
                            "-fx-border-color: #ffff00; " +
                            "-fx-font-weight: bold; " +
                            "-fx-prompt-text-fill: #888888;"
            );
        }

        ComboBox<?> comboBox = findComboBox(dialogPane);
        if (comboBox != null) {
            comboBox.setStyle(
                    "-fx-background-color: #000000; " +
                            "-fx-text-fill: #ffff00; " +
                            "-fx-border-color: #ffff00; " +
                            "-fx-font-weight: bold;"
            );
        }

        Node header = dialogPane.lookup(".header-panel");
        if (header != null) {
            header.setStyle("-fx-background-color: #000000;");
        }

        Node headerText = dialogPane.lookup(".header-panel .label");
        if (headerText != null) {
            headerText.setStyle("-fx-text-fill: #ffff00; -fx-font-weight: bold;");
        }

        Node content = dialogPane.lookup(".content");
        if (content != null) {
            content.setStyle("-fx-text-fill: #ffff00; -fx-font-weight: bold; -fx-background-color: #000000;");
        }

        Node contentLabel = dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle("-fx-text-fill: #ffff00; -fx-font-weight: bold;");
        }

        Node graphicContainer = dialogPane.lookup(".graphic-container");
        if (graphicContainer != null) {
            graphicContainer.setVisible(false);
        }

        dialog.setGraphic(null);

        Node alertGraphic = dialogPane.lookup(".alert .graphic");
        if (alertGraphic != null) {
            alertGraphic.setVisible(false);
        }
    }

    private static TextField findTextField(DialogPane dialogPane) {
        if (dialogPane.getContent() instanceof TextField) {
            return (TextField) dialogPane.getContent();
        }
        return (TextField) dialogPane.lookup(".text-field");
    }

    private static ComboBox<?> findComboBox(DialogPane dialogPane) {
        return (ComboBox<?>) dialogPane.lookup(".combo-box");
    }
}

