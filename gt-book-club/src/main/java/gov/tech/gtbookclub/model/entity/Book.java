package gov.tech.gtbookclub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "book")
public class Book {

    @Id
    private String id;

    private String title;

    private String description;

    private String genre;

    private String author;

    private String yearPublished;

    private String availability;

    private String lastBorrower;

    private Date createdAt;

    private Date updatedAt;

}
