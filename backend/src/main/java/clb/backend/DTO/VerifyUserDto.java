package clb.backend.DTO;

import lombok.Data;

@Data
public class VerifyUserDto {
    private String email;
    private String verificationCode;
}