package com.fitness.gateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Format")
    private  String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, message = "Password must have at least 6 character")
    private  String password;
    private String keyCloakId;
    private  String firstName;
    private  String lastName;


}
