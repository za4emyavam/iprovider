package com.example.iprovider.security;

import com.example.iprovider.data.UserRepository;
import com.example.iprovider.entities.forms.RegistrationForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * Controller for user registration page and registration form processing.
 */
@Slf4j
@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for RegistrationController class.
     *
     * @param userRepository  an instance of UserRepository
     * @param passwordEncoder an instance of PasswordEncoder
     */

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Returns the registration form view.
     *
     * @param model an instance of Model
     * @return a String representing the view name
     */
    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("user", new RegistrationForm());
        return "register";
    }

    /**
     * Processes the submitted registration form.
     *
     * @param registrationForm an instance of RegistrationForm
     * @param bindingResult    an instance of BindingResult
     * @param model            an instance of Model
     * @return a String representing the view name
     */
    @PostMapping
    public String processRegistration(@ModelAttribute("user") @Valid RegistrationForm registrationForm,
                                      BindingResult bindingResult, Model model) {
        //If have jakarta.validation errors
        if (bindingResult.hasErrors()) {
            log.error("Validation error: {}", bindingResult);
            model.addAttribute("user", registrationForm);
            return "register";
        }

        //If current email is already registered
        if (userRepository.findByUsername(registrationForm.getUsername()) != null) {
            model.addAttribute("user", registrationForm);
            model.addAttribute("emailError", "This email is already registered");
            return "register";
        }

        //If passwords do not match
        if (!registrationForm.getPassword().equals(registrationForm.getConfirmPass())) {
            model.addAttribute("user", registrationForm);
            model.addAttribute("passError", "Passwords do not match");
            return "register";
        }

        userRepository.create(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
