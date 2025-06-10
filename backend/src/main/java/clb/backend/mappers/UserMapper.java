package clb.backend.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import clb.backend.DTO.UserDataDTO;
import clb.backend.entities.User;

@Component
public class UserMapper {

    public UserDataDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDataDTO(
            user.getId(),
            user.getEmail(),
            user.getUsername() 
        );
    }

    public List<UserDataDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}