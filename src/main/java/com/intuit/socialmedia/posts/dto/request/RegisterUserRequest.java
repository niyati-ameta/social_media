package com.intuit.socialmedia.posts.dto.request;

import com.intuit.socialmedia.posts.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @Email
    private String email;

    @NotEmpty
    @ValidPassword
    private String password;

    @Size(max = 50, min = 3, message = "Name must be min 3 and max 50 character long")
    @NotBlank(message = "Name should not be blank")
    private String name;

    private String profilePic;
}
