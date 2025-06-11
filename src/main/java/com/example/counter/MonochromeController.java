package com.example.counter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MonochromeController {
    @GetMapping("/Monochrome")
    public String showMonochromePage() {
        return "Monochrome";
    }

    // この後、"/upload" の処理を書くメソッドも追加していきます
}