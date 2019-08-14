package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GeneaController {
    private final TreeService treeService;

    public GeneaController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "/familynames")
    public String getAllFamilyNames(Model model) {
        model.addAttribute("names", this.treeService.getLastNames());

        return "familynames";
    }

    @GetMapping(path = "/names/{name}")
    public String getFamily(@PathVariable String name, Model model) {
        List<IndividualDto> persons = this.treeService.getAllByLastname(name);
        model.addAttribute("persons", persons);
        model.addAttribute("name", name);
        return "names";
    }
}
