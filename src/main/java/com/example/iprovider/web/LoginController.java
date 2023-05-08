package com.example.iprovider.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class that handles the user login page and any errors that may occur during the login process.
 */
@Controller
public class LoginController {

    /**
     * Renders the login page.
     *
     * @return The name of the view that renders the login page.
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Renders the login page with an error message when there is an error during the login process.
     *
     * @param model The {@link Model} object used to pass data to the view.
     * @return The name of the view that renders the login page with the error message.
     */
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
