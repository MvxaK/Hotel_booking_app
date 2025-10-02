package org.cook.booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
        return "Index";
    }

    @GetMapping("/new")
    public String New(){
        return "new";
    }
}
