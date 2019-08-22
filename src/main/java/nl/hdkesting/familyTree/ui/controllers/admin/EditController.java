package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/edit")
public class EditController {
    private final TreeService treeService;

    public EditController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "/person/{id}")
    public String getEditPerson(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin";
        }

        IndividualDto primary = opt.get();
        model.addAttribute("person", new IndividualVm(primary));

        return "admin/editPerson";
    }

    @PostMapping(path = "/person/{id}")
    public String postEditPerson(@PathVariable long id, IndividualVm personVm, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        Optional<IndividualDto> personOpt = this.treeService.getIndividualById(id);
        if (personOpt.isEmpty()) {
            // TODO some message "unknown person"
            return "redirect:/admin";
        }

        var person = personOpt.get();

        person.setFirstNames(personVm.getFirstNames());
        person.setLastName(personVm.getLastName());
        person.setBirthDate(personVm.getBirthDate());
        person.setBirthPlace(personVm.getBirthPlace());
        person.setDeathDate(personVm.getDeathDate());
        person.setDeathPlace(personVm.getDeathPlace());
        switch (personVm.getSex()) {
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

        // TODO have the next page show some "person successfully updated" message
        return "redirect:/admin";
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }

        // error handling ...
        return LocalDate.parse(value);
    }
}
