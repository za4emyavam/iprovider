package com.example.iprovider.web.admin.tariffs;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Tariff;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin/tariffs/update")
public class UpdateTariffController {
    private final TariffRepository tariffRepository;
    private final ServiceRepository serviceRepository;

    public UpdateTariffController(TariffRepository tariffRepository, ServiceRepository serviceRepository) {
        this.tariffRepository = tariffRepository;
        this.serviceRepository = serviceRepository;
    }

    @ModelAttribute
    public void addServicesToModel(Model model) {
        model.addAttribute("services", serviceRepository.readAll());
    }

    @GetMapping
    public String getPage(Model model, @RequestParam long tariffId) {
        Optional<Tariff> currentTariff = tariffRepository.readByID(tariffId);
        if (currentTariff.isEmpty())
            return "redirect:/admin/tariffs";
        model.addAttribute("currentTariff", currentTariff.get());
        model.addAttribute("tariff", new Tariff());
        return "admin/tariffs/update";
    }

    @PostMapping
    public String updateTariff(Model model, @ModelAttribute("tariff") @Valid Tariff tariff, BindingResult bindingResult) {
        Optional<Tariff> currentTariff = tariffRepository.readByID(tariff.getTariffId());
        if (currentTariff.isEmpty())
            return "redirect:/admin/tariffs";

        for (Tariff t : tariffRepository.readAll()) {
            if (t.getName().equals(tariff.getName()) && t.getTariffId() != tariff.getTariffId()) {
                model.addAttribute("currentTariff", currentTariff.get());
                model.addAttribute("updatedTariff", new Tariff());
                model.addAttribute("nameError", "Tariffs with this name already exists");
                return "admin/tariffs/update";
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentTariff", currentTariff.get());
            model.addAttribute("updatedTariff", new Tariff());
            log.error("Validation error: {}", bindingResult);
            return "admin/tariffs/update";
        }



        tariffRepository.update(tariff);
        return "redirect:/admin/tariffs";
    }
}
