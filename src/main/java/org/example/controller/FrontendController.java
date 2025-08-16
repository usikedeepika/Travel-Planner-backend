package org.example.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/html/index.html";
    }

    @RequestMapping("/")
    public String root() {
        return "forward:/html/index.html";
    }
}
