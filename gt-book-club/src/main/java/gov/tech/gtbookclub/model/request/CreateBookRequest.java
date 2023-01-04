package gov.tech.gtbookclub.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

    @NotBlank(message = "availability/number of copies must not be blank")
    @Pattern(regexp = "[0-9]{1,4}", message = "availability/number of copies must be a numerical value and smaller than 10000")
    private String availability;

    @NotBlank(message = "year published must not be blank")
    private String yearPublished;
}
