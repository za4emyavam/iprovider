package com.example.iprovider.web;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.model.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tariffs")
public class TariffsController {
    private final TariffRepository tariffRepository;

    @Autowired
    public TariffsController(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @ModelAttribute
    public void addTariffsToModel(Model model) {
        Iterable<Tariff> tariffs = tariffRepository.findAll();
        model.addAttribute("tariffs", tariffs);
    }

    @GetMapping
    public String tariffs() {
        return "tariffs";
    }
}
