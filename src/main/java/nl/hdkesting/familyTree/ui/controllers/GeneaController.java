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

    /**
     * Level 1: just all known last names (plus a count)
     * @param model
     * @return
     */
    @GetMapping(path = "/familynames")
    public String getAllFamilyNames(Model model) {
        model.addAttribute("names", this.treeService.getLastNames());

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
        System.out.println("=== Start getting person " + id);
        PersonDetailsVm person = new PersonDetailsVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return "redirect:/familynames";
        }

        IndividualDto primary = opt.get();
        person.primary = new IndividualVm(primary);

        // add all his/her marriages (+children)
        for(var spouseFam : this.treeService.getSpouseFamiliesByIndividualId(id)) {
            person.marriages.add(new FamilyVm(spouseFam));
        }

        // add parents
        var childFams = this.treeService.getChildFamiliesByIndividualId(id);
        if (!childFams.isEmpty()) {
            // use the first one, ignore potential others
            person.family = new FamilyVm(childFams.iterator().next());
            person.setSiblings();

            // add grandparents (only needed when any parents are known)
            if (person.family.husband != null) {
                var fam = this.treeService.getChildFamiliesByIndividualId(person.family.husband.id);
                if (!fam.isEmpty()) {
                    person.paternalGrandparents = new FamilyVm(fam.iterator().next());
                }
            }
            if (person.family.wife != null) {
                var fam = this.treeService.getChildFamiliesByIndividualId(person.family.wife.id);
                if (!fam.isEmpty()) {
                    person.maternalGrandparents = new FamilyVm(fam.iterator().next());
                }
            }
        }
        /*
        // Assume each person is child in (at most) one family. Ignore adoptions etc.
        if (!primary.getChildFamilies().isEmpty()) {
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

        // A person may have (had) multiple marriages.
        for (FamilyDto dto : primary.getSpouseFamilies()) {
            FamilyVm fam = new FamilyVm(dto);
            person.marriages.add(fam);
        }


         */
        person.sortData();

        System.out.println("=== Done getting person " + id);
        model.addAttribute("person", person);
        return "person";
    }
}
