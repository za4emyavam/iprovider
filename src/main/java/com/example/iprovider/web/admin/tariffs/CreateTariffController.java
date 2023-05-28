package com.example.iprovider.web.admin.tariffs;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Tariff;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("services", serviceRepository.readAll());
    }

    @GetMapping
    public String getCreateTariff(Model model) {
        model.addAttribute("tariff", new Tariff());
        return "admin/tariffs/create";
    }

    @PostMapping
    public String addTariff(@ModelAttribute("tariff") @Valid Tariff tariff, BindingResult bindingResult,
                            Model model) {
        for (Tariff t : tariffRepository.readAll()) {
            if (t.getName().equals(tariff.getName()) && t.getTariffId() != tariff.getTariffId()) {
                model.addAttribute("tariff", tariff);
                model.addAttribute("nameError", "Tariffs with this name already exists");
                return "admin/tariffs/create";
            }
        }
        if (bindingResult.hasErrors()) {
            log.error("Validation error: {}", bindingResult);
            model.addAttribute("tariff", tariff);
            return "admin/tariffs/create";
        }
        tariffRepository.create(tariff);
        return "redirect:/admin/tariffs";
    }
}
