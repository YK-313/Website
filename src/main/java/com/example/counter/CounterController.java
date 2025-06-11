package com.example.counter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CounterController {

    private int count = 0;

    @GetMapping("/count")
    public String index(Model model) {
        model.addAttribute("count", count);
        return "index";
    }

    @PostMapping("/increment")
    public String increment() {
        count++;
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String reset() {
        count = 0;
        return "redirect:/";
    }
}
