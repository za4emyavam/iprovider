package com.example.iprovider.web;

import com.example.iprovider.data.ChecksRepository;
import com.example.iprovider.entities.User;
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
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size) {
        int amount = checksRepository.getAmount();
        int maxPage = (int) Math.ceil(amount * 1.0 / size);
        if (page < 1 || page > maxPage)
            page = 1;
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("checks", checksRepository.readAll(page, size));
        return "admin/check_payments";
    }

    @RequestMapping(value = "/admin/check_payments", method = RequestMethod.POST)
    public String processCheckPayments(Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();
        checksRepository.create(userDetails.getUserId());
        return "redirect:/admin/check_payments";
    }
}
