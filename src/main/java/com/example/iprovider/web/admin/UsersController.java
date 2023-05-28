package com.example.iprovider.web.admin;

import com.example.iprovider.data.RequestAdditionalServicesRepository;
import com.example.iprovider.data.UserRepository;
import com.example.iprovider.data.UserTariffsRepository;
import com.example.iprovider.entities.RequestAdditionalServices;
import com.example.iprovider.entities.User;
import com.example.iprovider.entities.forms.DeleteTariffFromUserForm;
import com.example.iprovider.entities.forms.UserInfoForm;
import com.example.iprovider.entities.UserTariffs;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final UserRepository userRepository;
    private final UserTariffsRepository userTariffsRepository;
    private final RequestAdditionalServicesRepository requestAdditionalServicesRepository;

    public UsersController(UserRepository userRepository,
                           UserTariffsRepository userTariffsRepository,
                           RequestAdditionalServicesRepository requestAdditionalServicesRepository) {
        this.userRepository = userRepository;
        this.userTariffsRepository = userTariffsRepository;
        this.requestAdditionalServicesRepository = requestAdditionalServicesRepository;
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getUsersPage(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> pagedUsers = userRepository.readAll(pageRequest);

        model.addAttribute("pageable", pagedUsers);
        model.addAttribute("users", pagedUsers.getContent());
        model.addAttribute("maxPage", pagedUsers.getTotalPages() - 1);

        return "admin/users";
    }

    @RequestMapping(value = "/admin/users/user_info", method = RequestMethod.GET)
    public String getUserInfoPage(Model model, @RequestParam long userId) {
        Optional<User> user = userRepository.read(userId);
        if (user.isEmpty()) {
            return "redirect:/admin/users";
        }
        List<UserTariffs> userTariffsList = new ArrayList<>();
        userTariffsRepository.readById(userId).forEach(userTariffsList::add);

        List<RequestAdditionalServices> requestAdditionalServices = new ArrayList<>();
        requestAdditionalServicesRepository.readBySubscriberId(userId).forEach(requestAdditionalServices::add);
        model.addAttribute("user", user.get());
        model.addAttribute("userTariffs", userTariffsList);
        model.addAttribute("additionalServices", requestAdditionalServices);
        model.addAttribute("userInfoForm", new UserInfoForm());
        model.addAttribute("deleteTariffFromUserForm", new DeleteTariffFromUserForm());
        return "admin/user_info";
    }

    @RequestMapping(value = "/admin/users/user_info", method = RequestMethod.POST)
    public String processUserInfoPage(RedirectAttributes redirectAttributes,
                                      @ModelAttribute UserInfoForm userInfoForm,
                                      @RequestParam long userId,
                                      @RequestParam String command) {
        Optional<User> user = userRepository.read(userId);
        if (user.isEmpty()) {
            return "redirect:/admin/users";
        }
        switch (command) {
            case "status":
                user.get().setUserStatus(User.UserStatusType.valueOf(userInfoForm.getValue()));
                userRepository.update(user.get());
                break;
            case "role":
                user.get().setUserRole(User.RoleType.valueOf(userInfoForm.getValue()));
                userRepository.update(user.get());
                break;
            default:
                return "redirect:/admin/users";
        }
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/admin/users/user_info";
    }

    @RequestMapping(value = "/admin/users/user_info/delete")
    public String deleteTariffFromUser(RedirectAttributes redirectAttributes,
                                       @Valid @ModelAttribute("deleteTariffFromUserForm") DeleteTariffFromUserForm deleteTariffFromUserForm) {
        //TODO удалять по id usertariffs, а не по отдельность и изменить connection_request
        if (userTariffsRepository.deleteByUserIdTariffId(deleteTariffFromUserForm.getUserId(), deleteTariffFromUserForm.getTariffId())) {
            redirectAttributes.addAttribute("userId", deleteTariffFromUserForm.getUserId());
            return "redirect:/admin/users/user_info";
        }
        return "redirect:/admin/users";
    }
}
