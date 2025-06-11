package com.example.counter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class yokosc {

    @GetMapping("/yokosc")
    public String showGamePage() {
        return "yokosc";  // templates/yokosc.html を返す（拡張子不要）
    }
}
