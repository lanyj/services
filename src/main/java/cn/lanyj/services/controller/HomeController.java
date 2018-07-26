package cn.lanyj.services.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        return "hello";
    }
}
