package com.example.iprovider.security;

import com.example.iprovider.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("user", new RegistrationForm());
        return "register";
    }

    @PostMapping
    public String processRegistration(Model model, RegistrationForm registrationForm) {
        if (userRepository.findByUsername(registrationForm.getUsername()) != null) {
            model.addAttribute("user", new RegistrationForm());
            model.addAttribute("emailError", "This email is already registered");
            return "register";
        }
        userRepository.create(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
