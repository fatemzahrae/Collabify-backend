package clb.backend.DTO;

import clb.backend.entities.User;
import lombok.Data;

@Data
public class UserDataDTO  {
    private Long id;
    private String username;
    private String email;

    public UserDataDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserDataDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

}
