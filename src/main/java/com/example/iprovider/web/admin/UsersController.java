package com.example.iprovider.web.admin;

import com.example.iprovider.data.UserRepository;
import com.example.iprovider.data.UserTariffsRepository;
import com.example.iprovider.entities.User;
import com.example.iprovider.entities.UserInfoForm;
import com.example.iprovider.entities.UserTariffs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final UserRepository userRepository;
    private final UserTariffsRepository userTariffsRepository;

    public UsersController(UserRepository userRepository, UserTariffsRepository userTariffsRepository) {
        this.userRepository = userRepository;
        this.userTariffsRepository = userTariffsRepository;
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getUsersPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size) {
        int amount = userRepository.getAmount();
        int maxPage = (int)Math.ceil(amount * 1.0 / size);
        if (page <= 0 || page > maxPage) {
            page = 1;
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("users", userRepository.readAll(page, size));
        return "admin/users";
    }

    @RequestMapping(value = "/admin/users/user_info", method = RequestMethod.GET)
    public String getUserInfoPage(Model model, @RequestParam long userId) {
        Optional<User> user = userRepository.read(userId);
        if (user.isEmpty()) {
            return "redirect:/admin/users";
        }
        Iterable<UserTariffs> userTariffs = userTariffsRepository.readById(userId);
        List<UserTariffs> userTariffsList = new ArrayList<>();
        userTariffs.forEach(userTariffsList::add);
        model.addAttribute("user", user.get());
        model.addAttribute("userTariffs", userTariffsList);
        model.addAttribute("userInfoForm", new UserInfoForm());
        return "admin/user_info";
    }

    @RequestMapping(value = "/admin/users/user_info", method = RequestMethod.POST)
    public String processUserInfoPage(@ModelAttribute UserInfoForm userInfoForm, @RequestParam long userId) {
        System.out.println(userInfoForm);
        Optional<User> user = userRepository.read(userId);
        if (user.isEmpty()) {
            return "redirect:/admin/users";
        }
        switch (userInfoForm.getCommand()) {
            case "status":
                System.out.println("STATUS + " + userInfoForm.getValue());
                break;
            case "role":
                System.out.println("ROLE + " + userInfoForm.getValue());
                break;
            default:
                return "redirect:/admin/users";
        }

        return "admin/user_info";
    }
}
