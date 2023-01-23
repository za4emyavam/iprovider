package com.example.iprovider.web.admin;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Tariff;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminTariffsController {
    private final TariffRepository tariffRepository;

    public AdminTariffsController(TariffRepository tariffRepository) {
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

    @ModelAttribute(name = "tariff")
    public Tariff tariff() {
        return new Tariff();
    }

    @RequestMapping(value = "/admin/tariffs", method = RequestMethod.GET)
    public String getAdminTariffsPage() {
        return "admin/tariffs";
    }

    @RequestMapping(value = "/admin/tariffs/delete", method = RequestMethod.POST)
    public String deleteTariff(@RequestParam long tariffId) {
        tariffRepository.delete(tariffId);
        return "redirect:/admin/tariffs";
    }


}
