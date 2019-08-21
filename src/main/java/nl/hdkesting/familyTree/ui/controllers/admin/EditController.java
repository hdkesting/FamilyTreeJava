package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
    public String postEditPerson(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        // TODO get record from db, update fields, send back to db

        // TODO have the next page show some "person successfully updated" message
        return "redirect:/admin";
    }
}
