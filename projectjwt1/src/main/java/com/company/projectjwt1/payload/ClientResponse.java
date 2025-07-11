package com.company.projectjwt1.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {

    @JsonIgnore
    private Integer id;
    private String name;
    private Integer age;
    private String emailId;
    private String password;
    private String role;
}
