package clb.backend.controllers;

import clb.backend.DTO.UserDataDTO;
import clb.backend.services.UserService;
import clb.backend.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDataDTO>> allUsers() {
        List <User> users = userService.allUsers();
        List<UserDataDTO> dtos = users.stream()
                .map(UserDataDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);       }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDataDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }



}