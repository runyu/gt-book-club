package gov.tech.gtbookclub.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank(message = "please enter name")
    private String name;

    @NotBlank(message = "please enter email")
    @Email(message ="please enter valid email")
    private String email;

    @NotNull(message = "please enter password")
    @Size(min = 5, message = "Password should be at least 5 characters")
    private String password;

    private String role;
}
