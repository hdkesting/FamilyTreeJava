package nl.hdkesting.familyTree.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("tabtitle", "Welcome");
        return "index";
    }
}
