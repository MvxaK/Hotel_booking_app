package org.cook.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.service.implementation.UserServiceImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserServiceImpl userService;

    @ModelAttribute
    public void addUserAttributes(Principal principal, Model model){
        if(principal != null){
            User user = userService.getUserName(principal.getName());
            model.addAttribute("currentUserId", user.getId());
            model.addAttribute("currentUserName", user.getUserName());
        }
    }
}
