package com.company.projectjwt1.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {

    private Integer id;

    @NotBlank(message = "name can not be empty")
    private String name;

    @NotNull(message = "age can not be empty")
    private Integer age;

    @NotBlank(message = "email id can not be empty")
    @Email(message = "email ID is invalid")
    private String emailId;

    @NotBlank(message = "password can not be empty")
    @Size(min = 4, max = 15, message = "password is not in proper length")
    private String password;

    @NotBlank(message = "role can not be empty")
    private String role;
}
