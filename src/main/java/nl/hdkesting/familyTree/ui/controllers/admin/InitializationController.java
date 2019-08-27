package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.services.ApplicationProperties;
import nl.hdkesting.familyTree.core.services.TreeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/admin/init")
public class InitializationController {
    private final TreeService treeService;
    private final ApplicationProperties appProperties;
    private static final String MESSAGE_KEY = "message";

    // NB no "login check" added

    public InitializationController(
            TreeService treeService,
            ApplicationProperties appProperties) {
        this.treeService = treeService;
        this.appProperties = appProperties;
    }

    @GetMapping(path = "")
    public String getIndex(Model model, HttpServletRequest request) {
        String message = (String)request.getSession().getAttribute(MESSAGE_KEY);
        request.getSession().removeAttribute(MESSAGE_KEY);
        model.addAttribute("message", message);
        return "admin/init";
    }

    @GetMapping(path="/clear")
    public String clearAll(HttpServletRequest request) {
        String message;
        if (this.treeService.clearAll()) {
            message = "All cleared";
        } else {
            message = "Something went wrong";
        }

        request.getSession().setAttribute(MESSAGE_KEY, message);
        return "redirect:/admin/init";
    }

    @GetMapping(path = "/load")
    public String loadAll(HttpServletRequest request) {
        String path = "source/" + this.appProperties.getGedcomSource();
        String message;
        if (this.treeService.load(path)) {
            message = "File " + path + " is loaded.";
        } else {
            message = "Some error loading " + path;
        }

        request.getSession().setAttribute(MESSAGE_KEY, message);
        return "redirect:/admin/init";
    }
}
