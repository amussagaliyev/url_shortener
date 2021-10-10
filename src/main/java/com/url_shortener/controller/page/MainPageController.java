package com.url_shortener.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageController {

    @GetMapping({"/","index.html"})
    public String index() {
        return "index";
    }

}
