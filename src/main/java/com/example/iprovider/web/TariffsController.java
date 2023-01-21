package com.example.iprovider.web;

import com.example.iprovider.data.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tariffs")
public class TariffsController {
    private final TariffRepository tariffRepository;

    @Autowired
    public TariffsController(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @ModelAttribute
    public void addTariffsToModel(Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        int number = tariffRepository.getAmount();
        int maxPage = (int)Math.ceil(number * 1.0 / size);
        if (page <= 0 || page > maxPage) {
            page = 1;
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("tariffs", tariffRepository.readAll(page, size));
    }

    @GetMapping
    public String tariffs() {
        return "tariffs";
    }
}
