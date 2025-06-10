package clb.backend.services;

import clb.backend.DTO.UserDataDTO;
import clb.backend.entities.User;
import clb.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDataDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return new UserDataDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole());
    }


    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}