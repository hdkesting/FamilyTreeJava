package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
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
    public static final String LOGIN_SESSION = "LoginSession";
    public static final String LOGIN_REDIRECT = "redirect:/admin/login";
    private final TreeService treeService;

    public AdminController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path = "")
    public String getIndex(HttpServletRequest request) {
        if (isLoggedIn(request)) {
            // logged in: redirect to search
            return "redirect:/admin/search";
        } else {
            // not logged in: ask
            return LOGIN_REDIRECT;
        }
    }

    @GetMapping(path = "/search")
    public String getSearch(HttpServletRequest request, Model model) {
        if (!isLoggedIn(request)) {
            return LOGIN_REDIRECT;
        }

        String first = (String)request.getSession().getAttribute("firstname");
        String last = (String)request.getSession().getAttribute("lastname");
        model.addAttribute("firstname", first);
        model.addAttribute("lastname", last);

        if (first != null && last != null) {
            List<IndividualDto> dtoResult = this.treeService.searchByName(first, last);

            model.addAttribute("firstname", first);
            model.addAttribute("lastname", last);

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
        if (!isLoggedIn(request)) {
            return LOGIN_REDIRECT;
        }

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

    @GetMapping(path = "/login")
    public String getLogin(HttpServletRequest request) {
        return isLoggedIn(request) ? "redirect:/admin" : "admin/login";
    }

    @PostMapping(path = "/login")
    public String postLogin(HttpServletRequest request) {
        String[] vals = request.getParameterValues("answer");
        if (vals != null && vals.length == 1) {
            if (vals[0].equalsIgnoreCase("yes")) {
                setLoggedIn(request, true);
                // back to admin start page, let that redirect further
                return "redirect:/admin";
            }
        }

        setLoggedIn(request, false);
        // get out of admin
        return "redirect:/";
    }

    // simple "is logged in" check, assuming that a *real* security system will handle this at a more fundamental level
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // don't create a new session
        if (session == null) {
            // no session == not logged in
            return false;
        } else {
            return session.getAttribute(LOGIN_SESSION) != null;
        }
    }

    private void setLoggedIn(HttpServletRequest request, boolean isLoggedIn) {
        HttpSession session = request.getSession(true); // do create a new session, when needed

        if (isLoggedIn) {
            session.setAttribute(LOGIN_SESSION, Boolean.valueOf(true));
        } else {
            session.removeAttribute(LOGIN_SESSION);
        }
    }
}
