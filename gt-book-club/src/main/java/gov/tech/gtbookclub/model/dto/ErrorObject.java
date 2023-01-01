package gov.tech.gtbookclub.model.dto;

import lombok.Data;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private long timestamp;
}
