package com.example.iprovider.web;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.data.UserTariffsRepository;
import com.example.iprovider.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {
    private final UserTariffsRepository userTariffsRepository;

    public CabinetController(UserTariffsRepository userTariffsRepository) {
        this.userTariffsRepository = userTariffsRepository;
    }

    @ModelAttribute
    public void addTariffsToModel(Model model, Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();
        model.addAttribute("tariffs", userTariffsRepository.readById(userDetails.getUserId()));
    }

    @GetMapping
    public String cabinet() {
        return "cabinet";
    }
}
