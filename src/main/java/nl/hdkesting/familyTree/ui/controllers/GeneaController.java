package nl.hdkesting.familyTree.ui.controllers;

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

    /**
     * Level 1: just all known last names (plus a count)
     * @param model
     * @return
     */
    @GetMapping(path = "/familynames")
    public String getAllFamilyNames(Model model) {
        model.addAttribute("names", this.treeService.getLastNames());
        model.addAttribute("tabtitle", "All known last names");

        return "familynames";
    }

    /**
     * Level 2: all first names plus details for a supplied last name.
     * @param name
     * @param model
     * @return
     */
    @GetMapping(path = "/names/{name}")
    public String getFamily(@PathVariable String name, Model model) {
        model.addAttribute("tabtitle", "Family list for " + name);

        List<IndividualDto> persons = this.treeService.getAllByLastname(name);
        model.addAttribute("persons", persons);
        model.addAttribute("name", name);
        return "names";
    }

    /**
     * Level 3: details about one person: siblings, parents, grandparents, marriage(s), children
     * @param id - the internal ID of the person.
     * @param model
     * @return
     */
    @GetMapping(path = "/person/{id}")
    public String getPerson(@PathVariable long id, Model model) {
        PersonDetailsVm person = new PersonDetailsVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id, false);
        if (opt.isEmpty()) {
            return "redirect:/familynames";
        }

        IndividualDto primary = opt.get();
        person.primary = new IndividualVm(primary);
        model.addAttribute("tabtitle", person.primary.getLastName() + ", " + person.primary.getFirstNames());

        // add all his/her marriages (+children)
        for(var spouseFam : this.treeService.getSpouseFamiliesByIndividualId(id, false)) {
            person.marriages.add(new FamilyVm(spouseFam));
        }

        // add parents
        var childFams = this.treeService.getChildFamiliesByIndividualId(id, false);
        if (!childFams.isEmpty()) {
            // use the first one, ignore potential others
            person.family = new FamilyVm(childFams.iterator().next());
            person.setSiblings();

            // add grandparents (only needed when any parents are known)
            if (person.family.getHusband() != null) {
                var fam = this.treeService.getChildFamiliesByIndividualId(person.family.getHusband().getId(), false);
                if (!fam.isEmpty()) {
                    person.paternalGrandparents = new FamilyVm(fam.iterator().next());
                }
            }
            if (person.family.getWife() != null) {
                var fam = this.treeService.getChildFamiliesByIndividualId(person.family.getWife().getId(), false);
                if (!fam.isEmpty()) {
                    person.maternalGrandparents = new FamilyVm(fam.iterator().next());
                }
            }
        }

        person.sortData();

        model.addAttribute("person", person);
        return "person";
    }
}
