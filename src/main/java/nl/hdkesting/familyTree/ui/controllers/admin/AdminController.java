package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.ApplicationProperties;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    private final TreeService treeService;
    private final ApplicationProperties appProperties;

    public AdminController(
            TreeService treeService,
            ApplicationProperties appProperties) {
        this.treeService = treeService;
        this.appProperties = appProperties;
    }

    @GetMapping(path = "")
    public String getIndex() {
        return "admin/index";
    }

    @GetMapping(path = "/search")
    public String getSearch(HttpServletRequest request, Model model) {
        String first = (String)request.getSession().getAttribute("firstname");
        String last = (String)request.getSession().getAttribute("lastname");
        if (first == null) {
            first = "";
        } else {
            first = first.trim();
        }

        if (last == null) {
            last = "";
        } else {
            last = last.trim();
        }
        model.addAttribute("firstname", first);
        model.addAttribute("lastname", last);

        // at least *something* to search on
        if (first.length() > 0 || last.length() > 0) {
            List<IndividualDto> dtoResult = this.treeService.searchByName(first, last);

            List<IndividualVm> result = new ArrayList<>(dtoResult.size());
            for(IndividualDto dto : dtoResult) {
                result.add(new IndividualVm(dto));
            }

            model.addAttribute("list", result);
        }

        return "admin/search";
    }

    @PostMapping(path = "/search")
    public String postSearch(HttpServletRequest request, Model model) {
        String[] afirst = request.getParameterValues("firstname");
        String[] alast = request.getParameterValues("lastname");

        if (afirst != null && alast != null) {
            String first = afirst[0];
            String last = alast[0];

            request.getSession().setAttribute("firstname", first);
            request.getSession().setAttribute("lastname", last);
        }

        // PRG, sort of
        return "redirect:/admin/search";
    }
}
