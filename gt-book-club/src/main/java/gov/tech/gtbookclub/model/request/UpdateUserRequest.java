package gov.tech.gtbookclub.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateUserRequest {
    private String name;

    @Size(min = 5, message = "Password should be at least 5 characters")
    private String password;

    private String role;
}
