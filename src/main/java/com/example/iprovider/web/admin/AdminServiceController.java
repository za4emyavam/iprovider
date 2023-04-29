package com.example.iprovider.web.admin;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Service;
import com.example.iprovider.entities.Tariff;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class AdminServiceController {
    private final ServiceRepository serviceRepository;
    private final TariffRepository tariffRepository;

    public AdminServiceController(ServiceRepository serviceRepository, TariffRepository tariffRepository) {
        this.serviceRepository = serviceRepository;
        this.tariffRepository = tariffRepository;
    }

    @RequestMapping(value = "/admin/services", method = RequestMethod.GET)
    public String getServicesPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if(error != null) {
            model.addAttribute("error",
                    "Tariffs are connected to the service. First remove the connected tariffs, then the service.");
        }
        model.addAttribute("services", serviceRepository.readAll());
        return "admin/services";
    }

    @RequestMapping(value = "/admin/services/create", method = RequestMethod.GET)
    public String getCreateServicePage(Model model) {
        model.addAttribute("service", new Service());
        return "admin/services/create";
    }

    @RequestMapping(value = "/admin/services/create", method = RequestMethod.POST)
    public String processCreateService(@ModelAttribute @Valid Service service, Errors errors) {
        if (errors.hasErrors()) {
            log.error("Validation error: {}", errors);
            return "admin/services/create";
        }
        serviceRepository.create(service);
        return "redirect:/admin/services";
    }

    @RequestMapping(value = "/admin/services/delete", method = RequestMethod.POST)
    public String deleteService(@RequestParam long serviceId, RedirectAttributes redirectAttributes) {
        Iterable<Tariff> tariffsByService = tariffRepository.readAllByService(serviceId);
        if(tariffsByService.iterator().hasNext())
        {
            redirectAttributes.addFlashAttribute("error",
                    "Tariffs are connected to the service");
            return "redirect:/admin/services?error";
        }
        else
            serviceRepository.delete(serviceId);
        return "redirect:/admin/services";
    }
}
