package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import nl.hdkesting.familyTree.ui.viewModels.PersonVm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(path = "/person/{id}")
    public String getPerson(@PathVariable long id, Model model) {
        PersonVm person = new PersonVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return "redirect:/familynames";
        }

        person.primary = new IndividualVm(opt.get());

        model.addAttribute("person", person);
        return "person";
    }
}
