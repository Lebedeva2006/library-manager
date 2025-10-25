package ru.lebedeva.librarylabgui;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    private String title;
    @Builder.Default
    private String author = "Unknown author";
    @Builder.Default
    private String genre = "Unknown genre";
    @Builder.Default
    private String year = "Unknown year";

    @Override
    public String toString() {
        return title + " by " + author + " (" + year + ")";
    }

}
