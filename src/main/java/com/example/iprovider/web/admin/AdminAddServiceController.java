package com.example.iprovider.web.admin;

import com.example.iprovider.data.AdditionalServiceRepository;
import com.example.iprovider.entities.AdditionalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
public class AdminAddServiceController {
    private final AdditionalServiceRepository additionalServiceRepository;

    public AdminAddServiceController(AdditionalServiceRepository additionalServiceRepository) {
        this.additionalServiceRepository = additionalServiceRepository;
    }

    @RequestMapping(value = "/admin/add_services", method = RequestMethod.GET)
    public String getPage(Model model) {
        model.addAttribute("addServices", additionalServiceRepository.readAll());
        return "admin/add_services";
    }

    @RequestMapping(value = "/admin/add_services/create", method = RequestMethod.GET)
    public String getCreateServicePage(Model model) {
        model.addAttribute("addService", new AdditionalService());
        return "admin/add_services/create";
    }

    @RequestMapping(value = "/admin/add_services/update", method = RequestMethod.GET)
    public String getUpdateServicePage(@RequestParam long addServiceId, Model model) {
        Optional<AdditionalService> currentAddService = additionalServiceRepository.read(addServiceId);
        if (currentAddService.isEmpty())
            return "redirect:/admin/add_services";
        model.addAttribute("currentAddService", currentAddService.get());
        model.addAttribute("additionalService", new AdditionalService());
        return "admin/add_services/update";
    }

    @RequestMapping(value = "/admin/add_services/delete", method = RequestMethod.POST)
    public String deleteAdditionalService(@RequestParam long addServiceId) {
        additionalServiceRepository.delete(addServiceId);
        return "redirect:/admin/add_services";
    }

    @RequestMapping(value = "/admin/add_services/create", method = RequestMethod.POST)
    public String processCreateAdditionalService(@ModelAttribute @Valid AdditionalService additionalService, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Validation error: {}", errors);
            model.addAttribute("addService", new AdditionalService());
            return "admin/add_services/create";
        }
        additionalService.setStatus(AdditionalService.Status.ACTIVE);
        additionalServiceRepository.create(additionalService);
        return "redirect:/admin/add_services";
    }

    @RequestMapping(value = "/admin/add_services/update", method = RequestMethod.POST)
    public String processUpdateAdditionalService(@ModelAttribute("additionalService")
                                                 @Valid AdditionalService additionalService,
                                                 @RequestParam long additionalServiceId, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Optional<AdditionalService> currentAddService = additionalServiceRepository
                    .read(additionalService.getAdditionalServiceId());
            if (currentAddService.isEmpty())
                return "redirect:/admin/add_services";
            model.addAttribute("currentAddService", currentAddService.get());
            model.addAttribute("additionalService", new AdditionalService());
            log.error("Validation error: {}", errors);
            return "admin/add_services/update";
        }
        additionalService.setAdditionalServiceId(additionalServiceId);
        additionalServiceRepository.update(additionalService);
        return "redirect:/admin/add_services";
    }
}
