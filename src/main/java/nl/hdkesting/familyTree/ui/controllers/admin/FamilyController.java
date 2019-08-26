package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.FamilyVm;
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
@RequestMapping(path = "/admin/family")
public class FamilyController {
    private final TreeService treeService;

    public FamilyController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "/edit/{id}")
    public String getEditFamily(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        Optional<FamilyDto> opt = this.treeService.getFamilyById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin/search";
        }

        FamilyDto fam = opt.get();
        model.addAttribute("family", new FamilyVm(fam));

        return "admin/editFamily";
    }

    @PostMapping(path = "/edit/{id}")
    public String postEditFamily(@PathVariable long id, FamilyVm familyVm, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        Optional<FamilyDto> opt = this.treeService.getFamilyById(id);
        if (opt.isEmpty()) {
            // TODO some message "unknown person"
            return "redirect:/admin/search";
        }

        var fam = opt.get();

        fam.setMarriageDate(familyVm.getMarriageDate());
        fam.setMarriagePlace(familyVm.getMarriagePlace());
        fam.setDivorceDate(familyVm.getDivorceDate());
        fam.setDivorcePlace(familyVm.getDivorcePlace());

        this.treeService.update(fam);

        // TODO have the next page show some "person successfully updated" message
        long primary = Long.parseLong("0" + request.getParameter("primary"));
        if (primary > 0) {
            return "redirect:/admin/person/show/" + primary;
        }
        return "redirect:/admin";
    }

    @GetMapping(path = "/add")
    public String getAddFamily(Model model, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        long primary = Long.parseLong("0" + request.getParameter("primary"));
        String famtype = request.getParameter("famtype"); // C=primary is child, S=primary is spouse

        if (primary == 0 || (!famtype.equalsIgnoreCase("C") && !famtype.equalsIgnoreCase("P"))) {
            return "redirect:/admin/search";
        }

        model.addAttribute("family", new FamilyVm()); // to prevent NPE

        return "admin/editFamily"; // re-use that template
    }

    @PostMapping(path = "/add")
    public String postAddFamily(FamilyVm familyVm, HttpServletRequest request) {
        if (!AdminController.isLoggedIn(request)) {
            return AdminController.LOGIN_REDIRECT;
        }

        long primary = Long.parseLong("0" + request.getParameter("primary"));
        String famtype = request.getParameter("famtype"); // C=primary is child, S=primary is spouse

        if (primary == 0 || (!famtype.equalsIgnoreCase("C") && !famtype.equalsIgnoreCase("P"))) {
            return "redirect:/admin/search";
        }

        var fam = new FamilyDto();
        fam.setMarriageDate(familyVm.getMarriageDate());
        fam.setMarriagePlace(familyVm.getMarriagePlace());
        fam.setDivorceDate(familyVm.getDivorceDate());
        fam.setDivorcePlace(familyVm.getDivorcePlace());

        var famid = this.treeService.add(fam);

        if (famtype.equalsIgnoreCase("C")) {
            this.treeService.addChild(famid, primary);
        }
        else if (famtype.equalsIgnoreCase("S")) {
            this.treeService.addSpouse(famid, primary);
        }

        return "redirect:/admin/person/show/" + primary;
    }
}
