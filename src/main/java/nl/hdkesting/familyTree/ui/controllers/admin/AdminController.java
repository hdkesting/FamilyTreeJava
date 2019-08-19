package nl.hdkesting.familyTree.ui.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    public static final String LOGIN_SESSION = "LoginSession";

    @GetMapping(path = "")
    public String getIndex(HttpServletRequest request) {
        if (isLoggedIn(request)) {
            return "redirect:/admin/search";
        } else {
            return "redirect:/admin/login";
        }
    }

    @GetMapping(path = "/search")
    public String getSearch(HttpServletRequest request) {
        // show search page when logged in, else redirect to login
        return isLoggedIn(request)? "admin/search" : "redirect:/admin/login";
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

    private boolean isLoggedIn(HttpServletRequest request) {
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
