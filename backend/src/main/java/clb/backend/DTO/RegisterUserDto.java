package clb.backend.DTO;

import lombok.Data;

@Data

public class RegisterUserDto {
    private String firstname;
    private String lastname;
    private String role;
    private String email;
    private String password;
}