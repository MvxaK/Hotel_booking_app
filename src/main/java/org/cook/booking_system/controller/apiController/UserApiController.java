package org.cook.booking_system.controller.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.UserCreateRequest;
import org.cook.booking_system.service.service_interface.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> createUser(@Valid  @RequestBody UserCreateRequest userCreateRequest){
        User user = userService.createUser(userCreateRequest);

        URI location = URI.create("/api/users/" + user.getId());

        return ResponseEntity.created(location)
                .body(user);
    } 

    @PutMapping("/{id}/update-info")
    public ResponseEntity<User> updateUserInfo(@PathVariable Long id, @RequestParam String newEmail, @RequestParam String currentPassword){
        User user = userService.updateUserInfo(id, newEmail, currentPassword);

        return ResponseEntity.ok()
                .body(user);
    }

    @PatchMapping("/{id}/update-password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable Long id, @RequestParam String newPassword, @RequestParam String currentPassword){
        userService.updateUserPassword(id, newPassword, currentPassword);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);

        return ResponseEntity.noContent()
                .build();
    }
}
