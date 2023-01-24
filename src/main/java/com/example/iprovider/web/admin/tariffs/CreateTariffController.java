package com.example.iprovider.web.admin.tariffs;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Tariff;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/tariffs/create")
public class CreateTariffController {
    private final TariffRepository tariffRepository;
    private final ServiceRepository serviceRepository;

    public CreateTariffController(TariffRepository tariffRepository, ServiceRepository serviceRepository) {
        this.tariffRepository = tariffRepository;
        this.serviceRepository = serviceRepository;
    }

    @ModelAttribute
    public void addServicesToModel(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
    }

    @GetMapping
    public String getCreateTariff(Model model) {
        model.addAttribute("tariff", new Tariff());
        return "admin/tariffs/create";
    }

    @PostMapping
    public String addTariff(@ModelAttribute @Valid Tariff tariff, Errors errors) {
        if (errors.hasErrors()) {
            log.error("Validation error: {}", errors);
            return "admin/tariffs/create";
        }
        tariffRepository.create(tariff);
        return "redirect:/admin/tariffs";
    }
}
