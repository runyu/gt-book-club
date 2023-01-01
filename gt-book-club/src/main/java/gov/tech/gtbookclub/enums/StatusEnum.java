package gov.tech.gtbookclub.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    SUCCESS(0, "S", "SUCCESS"),
    FAILED(1, "F", "FAILED"),

    UNAVAILABLE(2, "U", "UNAVAILABLE"),
    AVAILABLE(3, "A", "AVAILABLE");

    private int id;
    private String code;
    private String value;
}
