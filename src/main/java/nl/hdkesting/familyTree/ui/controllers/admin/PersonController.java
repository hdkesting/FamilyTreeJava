package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.FamilyVm;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import nl.hdkesting.familyTree.ui.viewModels.PersonDetailsVm;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/person")
public class PersonController {
    private final TreeService treeService;
    private static final String MESSAGE_KEY = "message";

    public PersonController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "/edit/{id}")
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

    @PostMapping(path = "/edit/{id}")
    public String postEditPerson(@PathVariable long id, IndividualVm personVm, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        Optional<IndividualDto> personOpt = this.treeService.getIndividualById(id);
        if (personOpt.isEmpty()) {
            // TODO some message "unknown person"
            return "redirect:/admin/search";
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
        long primary = Long.parseLong("0" + request.getParameter("primary"));
        if (primary > 0) {
            return "redirect:/admin/person/show/" + primary;
        }
        return "redirect:/admin";
    }

    @GetMapping(path = "/delete/{id}")
    public String getPersonDelete(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        Optional<IndividualDto> personOpt = this.treeService.getIndividualById(id);
        if (personOpt.isEmpty()) {
            // TODO some message "unknown person"
            return "redirect:/admin/search";
        }

        var person = personOpt.get();
        model.addAttribute("person", new IndividualVm(person));

        return "admin/deletePerson";
    }

    @PostMapping(path = "/delete/{id}")
    public String postPersonDelete(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        var o = request.getParameter("confirmid");
        var x = request.getParameterNames();
        if (o != null) {
            long confirm = Long.parseLong(o.toString());
            if (confirm == id) {
                this.treeService.deleteIndividualById(id);
            }
        }

        long primary = Long.parseLong("0" + request.getParameter("primary"));
        if (primary > 0) {
            return "redirect:/admin/person/show/" + primary;
        }

        return "redirect:/admin/search";
    }

    @GetMapping(path = "/show/{id}")
    public String showPerson(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        // load "personDetailsVm", same as in EditController, but skip the grandparents
        PersonDetailsVm person = new PersonDetailsVm();

        Optional<IndividualDto> opt = this.treeService.getIndividualById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin/search";
        }

        IndividualDto primary = opt.get();
        person.primary = new IndividualVm(primary);
        model.addAttribute("tabtitle", person.primary.getLastName() + ", " + person.primary.getFirstNames());

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

            // SKIP grandparents
        }

        person.sortData();

        model.addAttribute("person", person);

        return "admin/showPerson";
    }

    /**
     * Add a new person.
     * @param primary the primary person (id) to return back to
     * @param cfam the family (id) the new person will be a child of
     * @param sfam the family (id) the new person will be spouse of
     * @return
     */
    @GetMapping(path = "/add")
    public String getAddPerson(
            @RequestParam(value = "primary", required = true) long primary,
            @RequestParam(value = "cfam", required = false) Long cfam,
            @RequestParam(value = "sfam", required = false) Long sfam,
            Model model,
            HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        // preserve supplied values
        model.addAttribute("primary", primary);
        model.addAttribute("cfam", cfam);
        model.addAttribute("sfam", sfam);
        // add empty person model, so template doesn't have to deal with a null
        model.addAttribute("person", new IndividualVm());

        // re-use edit template
        return "admin/editPerson";
    }

    @PostMapping(path = "/add")
    public String postAddPerson(IndividualVm personVm, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        long primary = Long.parseLong(request.getParameter("primary"));
        long cfam = Long.parseLong("0" + request.getParameter("cfam"));
        long sfam = Long.parseLong("0" + request.getParameter("sfam"));

        var person = new IndividualDto();
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

        long newid = this.treeService.add(person);
        // add this new person as either spouse or child to a family
        if (cfam > 0) {
            this.treeService.addChild(cfam, newid);
        }

        if (sfam > 0) {
            this.treeService.addSpouse(sfam, newid);
        }

        return "redirect:/admin/person/show/" + primary;
    }

    @GetMapping(path = "/restore")
    public String getRestoreDeleted(Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }
        // get all soft-deleted people
        // add list to model

        List<IndividualDto> dtolist = this.treeService.getAllDeletedPersons();
        List<IndividualVm> vmlist = new ArrayList<>(dtolist.size());
        for(var dto : dtolist) {
            vmlist.add(new IndividualVm(dto));
        }

        model.addAttribute("list", vmlist);
        var msg = getMessage(request);
        if (msg.isPresent()) {
            model.addAttribute("message", msg.get());
        }

        return "admin/restorePersons";
    }

    @PostMapping(path = "/restore")
    public String postRestoreDeleted(HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }
        // get button: "restore checked" or "hard-delete checked"

        List<Long> ids = new ArrayList<>();
        String[] idStrings = request.getParameterValues("person");
        if (idStrings != null) {
            // the fantastic Java needs this null-check. Apparently can't be bothered to return an empty list.
            for (String id : idStrings) {
                ids.add(Long.parseLong(id));
            }
        }

        if (!ids.isEmpty()) {
            int cnt = this.treeService.restorePersons(ids);

            String message;
            switch (cnt) {
                case 0:
                    message = "No people are restored.";
                    break;
                case 1:
                    message = "One person is restored.";
                    break;
                default:
                    message = "" + cnt + " persons are restored.";
                    break;
            }
            setMessage(request,  message);
        } else {
            setMessage(request, "No people were selected.");
        }

        return "redirect:/admin/person/restore";
    }

    /**
     * Get the possible message from the session, immediately removing it.
     * @param request
     * @return
     */
    private Optional<String> getMessage(HttpServletRequest request) {
        String message = (String)request.getSession().getAttribute(MESSAGE_KEY);
        request.getSession().removeAttribute(MESSAGE_KEY);
        return Optional.ofNullable(message);
    }

    private void setMessage(HttpServletRequest request, String message) {
        if (Strings.isNullOrEmpty(message)) {
            request.getSession().removeAttribute(MESSAGE_KEY);
        } else {
            request.getSession().setAttribute(MESSAGE_KEY, message);
        }
    }
}
