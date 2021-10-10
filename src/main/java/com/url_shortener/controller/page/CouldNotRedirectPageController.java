package com.url_shortener.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CouldNotRedirectPageController {

    @GetMapping({"/s/could_not_redirect.html"})
    public String index() {
        return "could_not_redirect";
    }

}
