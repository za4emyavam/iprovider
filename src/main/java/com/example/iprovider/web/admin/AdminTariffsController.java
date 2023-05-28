package com.example.iprovider.web.admin;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Tariff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Tariff> pagedTariffs = tariffRepository.readAll(pageRequest);

        model.addAttribute("pageable", pagedTariffs);
        model.addAttribute("tariffs", pagedTariffs.getContent());
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
