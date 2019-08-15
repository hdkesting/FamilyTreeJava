package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.FamilyVm;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import nl.hdkesting.familyTree.ui.viewModels.PersonDetailsVm;
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
        PersonDetailsVm person = new PersonDetailsVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return "redirect:/familynames";
        }

        IndividualDto primary = opt.get();
        person.primary = new IndividualVm(primary);

        if (primary.getChildFamilies().size() > 0) {
            // use only first one for siblings and (grand)parents
            FamilyDto fam = primary.getChildFamilies().iterator().next();
            person.family = new FamilyVm(fam);
            person.setSiblings();

            for (IndividualDto prnt : fam.getSpouses()) {
                if (prnt.isMale()) {
                    // paternal grandparents
                    if (prnt.getChildFamilies().size() > 0) {
                        FamilyDto f2 = prnt.getChildFamilies().iterator().next();
                        person.paternalGrandparents = new FamilyVm(f2);
                    }
                } else {
                    // assume isFemale
                    // maternal grandparents
                    if (prnt.getChildFamilies().size() > 0) {
                        FamilyDto f2 = prnt.getChildFamilies().iterator().next();
                        person.maternalGrandparents = new FamilyVm(f2);
                    }
                }
            }
        }

        model.addAttribute("person", person);
        return "person";
    }
}
