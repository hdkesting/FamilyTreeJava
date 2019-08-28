package nl.hdkesting.familyTree.ui.controllers.api;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import nl.hdkesting.familyTree.ui.viewModels.PersonDetailsVm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/")
public class PersonApiController {
    private final TreeService treeService;
    private static final String MIMETYPE_JSON = "application/json";

    public PersonApiController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "person/{id}", produces = MIMETYPE_JSON)
    public ResponseEntity<?> getPerson(@PathVariable long id) {
        PersonDetailsVm person = new PersonDetailsVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        IndividualDto primary = opt.get();
        return new ResponseEntity<>(new IndividualVm(primary), HttpStatus.OK);
    }

    @PutMapping(path = "person/{id}", consumes = MIMETYPE_JSON)
    public ResponseEntity<?> putPerson(@RequestBody IndividualVm individual, @PathVariable long id) {
        if (individual == null || individual.getId() <= 0L || (individual.getFirstNames() == null && individual.getLastName() == null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (individual.getId() != id) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Optional<IndividualDto> personOpt = this.treeService.getIndividualById(id);
        if (personOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var person = personOpt.get();

        person.setFirstNames(individual.getFirstNames());
        person.setLastName(individual.getLastName());
        person.setBirthDate(individual.getBirthDate());
        person.setBirthPlace(individual.getBirthPlace());
        person.setDeathDate(individual.getDeathDate());
        person.setDeathPlace(individual.getDeathPlace());
        switch (individual.getSex()) {
            case 'M' :
                person.setSex(Sex.Male);
                break;
            case 'F':
                person.setSex(Sex.Female);
                break;
            default:
                person.setSex(Sex.Unknown);
                break;
        }

        this.treeService.update(person);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
