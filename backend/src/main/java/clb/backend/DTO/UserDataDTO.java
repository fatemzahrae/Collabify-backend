package clb.backend.DTO;

import clb.backend.entities.User;
import lombok.Data;

@Data

public class UserDataDTO extends User {
    private Long id;
    private String firstname;
    private String lastname;
    private String role;
    private String email;

    public UserDataDTO(Long id, String firstname, String lastname, String email, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }

}
