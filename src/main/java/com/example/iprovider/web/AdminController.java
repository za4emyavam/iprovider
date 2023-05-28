package com.example.iprovider.web;

import com.example.iprovider.data.ChecksRepository;
import com.example.iprovider.entities.Checks;
import com.example.iprovider.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final ChecksRepository checksRepository;

    public AdminController(ChecksRepository checksRepository) {
        this.checksRepository = checksRepository;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPage() {
        return "redirect:/admin/requests";
    }

    @RequestMapping(value = "/admin/check_payments", method = RequestMethod.GET)
    public String getChecksPage(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Checks> pagedChecks = checksRepository.readAll(pageRequest);

        model.addAttribute("pageable", pagedChecks);
        model.addAttribute("checks", pagedChecks.getContent());
        return "admin/check_payments";
    }

    @RequestMapping(value = "/admin/check_payments", method = RequestMethod.POST)
    public String processCheckPayments(Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();
        checksRepository.create(userDetails.getUserId());
        return "redirect:/admin/check_payments";
    }
}
