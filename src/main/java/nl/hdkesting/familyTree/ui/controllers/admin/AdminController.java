package nl.hdkesting.familyTree.ui.controllers.admin;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.ApplicationProperties;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.ui.viewModels.IndividualVm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    private static final String LOGIN_SESSION = "LoginSession";
    public static final String LOGIN_REDIRECT = "redirect:/admin/login";
    private final TreeService treeService;
    private final ApplicationProperties appProperties;

    public AdminController(
            TreeService treeService,
            ApplicationProperties appProperties) {
        this.treeService = treeService;
        this.appProperties = appProperties;
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
    public String postLogin(Model model, HttpServletRequest request) {
        String[] vals = request.getParameterValues("answer");
        if (vals != null && vals.length == 1) {
            if (vals[0].equalsIgnoreCase("yes")) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password)) {
                    byte[] unmbytes = username.toLowerCase().getBytes(StandardCharsets.UTF_8); // make username case-insensitive
                    byte[] pwdbytes = password.getBytes(StandardCharsets.UTF_8); // keep pwd case sensitive
                    // TODO salt + hash the password - https://www.baeldung.com/java-password-hashing

                    String u64 = Base64.encodeBase64String(unmbytes);
                    String p64 = Base64.encodeBase64String(pwdbytes);

                    String uprop = this.appProperties.getEncodedAdminUsername();
                    String pprop = this.appProperties.getEncodedAdminPassword();

                    if (u64.equals(uprop) && p64.equals(pprop)) {
                        setLoggedIn(request, true);
                        // back to admin start page, let that redirect further
                        return "redirect:/admin";
                    }
                }
            }
        }

        setLoggedIn(request, false);
        // re-ask username/password
        model.addAttribute("message", "Invalid username and/or password.");

        return "admin/login";
    }

    @GetMapping(path = "/logout")
    public String getLogout(HttpServletRequest request) {
        setLoggedIn(request, false);
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
