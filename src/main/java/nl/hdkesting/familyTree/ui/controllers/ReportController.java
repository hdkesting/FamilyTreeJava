package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.services.TreeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReportController {
    private final TreeService treeService;

    public ReportController(TreeService treeService) {
        this.treeService = treeService;
    }

    @RequestMapping("/summary")
    public String getSummary(Model model) {
        model.addAttribute("summary", this.treeService.getSummary());

        return "summary";
    }
}
