package com.example.iprovider.web;

import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.ConnectionRequest;
import com.example.iprovider.entities.Tariff;
import com.example.iprovider.entities.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
public class TariffsController {
    private final TariffRepository tariffRepository;
    private final ConnectionRequestRepository connectionRequestRepository;

    @Autowired
    public TariffsController(TariffRepository tariffRepository, ConnectionRequestRepository connectionRequestRepository) {
        this.tariffRepository = tariffRepository;
        this.connectionRequestRepository = connectionRequestRepository;
    }

    @RequestMapping(value = "/tariffs", method = RequestMethod.GET)
    public String tariffsTable(Model model,
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
        return "tariffs";
    }

    @RequestMapping(value = "/tariffs/request", method = RequestMethod.GET)
    public String requestToTariff(Model model, @RequestParam long tariffId) {
        Optional<Tariff> tariff = tariffRepository.readByID(tariffId);
        if (tariff.isEmpty()) {
            return "redirect:/tariffs";
        }
        ConnectionRequest connectionRequest = new ConnectionRequest();
        //connectionRequest.setTariff(tariff.get());
        model.addAttribute("connectionRequest", connectionRequest);
        model.addAttribute("tariffId", tariffId);
        return "request";
    }

    @RequestMapping(value = "/tariffs/request", method = RequestMethod.POST)
    public String processRequest(@ModelAttribute @Valid ConnectionRequest connectionRequest,
                                 @RequestParam long tariffId, Errors errors,
                                 Authentication authentication) {
        if(errors.hasErrors()) {
            log.error("Validation error: {}", errors);
            return "request";
        }
        Optional<Tariff> tariff = tariffRepository.readByID(tariffId);
        if (tariff.isEmpty()) {
            return "redirect:/tariffs";
        }
        User userDetails = (User) authentication.getPrincipal();
        connectionRequest.setSubscriber(userDetails.getUserId());
        connectionRequest.setTariff(tariff.get());
        connectionRequest.setDateOfChange(new Date());
        connectionRequest.setStatus(ConnectionRequest.RequestStatusType.IN_PROCESSING);
        connectionRequestRepository.create(connectionRequest);
        return "redirect:/tariffs";
    }
}
