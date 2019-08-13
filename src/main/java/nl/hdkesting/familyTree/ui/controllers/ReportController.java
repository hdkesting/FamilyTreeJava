package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.SummaryVm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {
    private final TreeService treeService;

    public ReportController(TreeService treeService) {
        this.treeService = treeService;
    }

    @RequestMapping("/summary")
    public String getSummary(Model model) {
        SummaryVm vm = new SummaryVm(this.treeService.getSummary());
        model.addAttribute("summary", vm);

        return "summary";
    }
}
