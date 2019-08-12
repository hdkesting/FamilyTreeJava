package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.services.TreeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            props.load(resourceStream);
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Cannot read props";
        }

        String path = "static/source/" + props.getProperty("gedcom.source");
        if (this.treeService.load(path)) {
            return "File " + path + " is loaded.";
        }

        return "Some error loading " + path;
    }
}
