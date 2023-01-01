package gov.tech.gtbookclub.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "title must not be blank")
    private String title;

    private String description;

    private String genre;

    @NotBlank(message = "author must not be blank")
    private String author;

    @NotBlank(message = "year published must not be blank")
    private String yearPublished;
}
