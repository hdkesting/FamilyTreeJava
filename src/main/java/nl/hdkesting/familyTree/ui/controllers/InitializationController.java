package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.services.TreeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/init")
public class InitializationController {
    private final TreeService treeService;

    public InitializationController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path="/clear")
    public @ResponseBody String clearAll() {
        if (this.treeService.clearAll()) {
            return "All cleared";
        } else {
            return "Something went wrong";
        }
    }

    @GetMapping(path = "/load")
    public @ResponseBody String loadAll() {
        String path = "static/family.ged";
        if (this.treeService.load(path)) {
            return "File " + path + " is loaded.";
        }

        return "Some error loading " + path;
    }
}
