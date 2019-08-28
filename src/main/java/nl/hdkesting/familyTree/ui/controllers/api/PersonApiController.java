package nl.hdkesting.familyTree.ui.controllers.api;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import nl.hdkesting.familyTree.ui.viewModels.PersonDetailsVm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/")
public class PersonApiController {
    private final TreeService treeService;

    public PersonApiController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "person/{id}")
    public @ResponseBody IndividualVm getPerson(@PathVariable long id) {
        PersonDetailsVm person = new PersonDetailsVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return null;
        }

        IndividualDto primary = opt.get();
        return new IndividualVm(primary);
    }
}
