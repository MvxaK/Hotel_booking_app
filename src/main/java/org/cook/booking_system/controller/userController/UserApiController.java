package org.cook.booking_system.controller.userController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.cook.booking_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody RegisterRequest registerRequest){
        User user = userService.createUser(registerRequest);

        URI location = URI.create("/api/users/" + user.getId());

        return ResponseEntity.created(location)
                .body(user);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUserInfo(@PathVariable Long id, @RequestBody User userToUpdate, @RequestParam String password){
        User user = userService.updateUserInfo(id, userToUpdate, password);

        return ResponseEntity.ok()
                .body(user);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Void> updateUserPassword(@PathVariable Long id, @RequestParam String newPassword, String password){
        userService.updateUserPassword(id, newPassword, password);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, String password){
        userService.deleteUser(id, password);

        return ResponseEntity.noContent()
                .build();
    }
}
