package org.cook.booking_system.controller.authController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.cook.booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest){
        userService.createUser(registerRequest);

        return "redirect:/";
    }

}
