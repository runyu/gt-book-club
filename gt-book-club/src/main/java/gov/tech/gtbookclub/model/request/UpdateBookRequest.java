package gov.tech.gtbookclub.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {

    private String title;

    private String description;

    private String genre;

    private String author;

    private String availability;

    private String yearPublished;

}
