package com.example.iprovider.web;

import com.example.iprovider.data.AdditionalServiceRepository;
import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.data.RequestAdditionalServicesRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.*;
import com.example.iprovider.entities.forms.ConnectionRequestForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
public class TariffsController {
    private final TariffRepository tariffRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final RequestAdditionalServicesRepository requestAdditionalServicesRepository;

    @Autowired
    public TariffsController(TariffRepository tariffRepository,
                             ConnectionRequestRepository connectionRequestRepository,
                             AdditionalServiceRepository additionalServiceRepository,
                             RequestAdditionalServicesRepository requestAdditionalServicesRepository) {
        this.tariffRepository = tariffRepository;
        this.connectionRequestRepository = connectionRequestRepository;
        this.additionalServiceRepository = additionalServiceRepository;
        this.requestAdditionalServicesRepository = requestAdditionalServicesRepository;
    }

    @RequestMapping(value = "/tariffs", method = RequestMethod.GET)
    public String tariffsTable(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "6") int size,
                               @RequestParam(defaultValue = "") String sort) {
        String[] sortParams = sort.split(": ");
        Sort s = sortParams.length == 0 || sortParams[0].isEmpty() ?
                Sort.unsorted()
                : sortParams.length == 1 ?
                Sort.by(sortParams[0]) : Sort.by(Direction.valueOf(sortParams[1]), sortParams[0]);

        PageRequest pageRequest = PageRequest.of(page, size, s);

        Page<Tariff> pagedTariffs = tariffRepository.readAllWithActiveStatus(pageRequest);

        model.addAttribute("pageable", pagedTariffs);
        model.addAttribute("tariffs", pagedTariffs.getContent());
        model.addAttribute("max", pagedTariffs.getTotalPages() - 1);
        return "tariffs";
    }

    @RequestMapping(value = "/tariffs/request", method = RequestMethod.GET)
    public String requestToTariff(Model model, @RequestParam long tariffId) {
        Optional<Tariff> tariff = tariffRepository.readByID(tariffId);
        if (tariff.isEmpty()) {
            return "redirect:/tariffs";
        }

        model.addAttribute("connectionRequest", new ConnectionRequestForm());
        model.addAttribute("tariffId", tariffId);
        model.addAttribute("additionalServices", additionalServiceRepository.readAll());
        return "request";
    }

    @RequestMapping(value = "/tariffs/request", method = RequestMethod.POST)
    public String processRequest(@ModelAttribute("connectionRequest") @Valid ConnectionRequestForm connectionRequest,
                                 BindingResult bindingResult, @RequestParam long tariffId, Model model,
                                 Authentication authentication) {
        Optional<Tariff> tariff = tariffRepository.readByID(tariffId);
        if (tariff.isEmpty()) {
            return "redirect:/tariffs";
        }

        if (bindingResult.hasErrors()) {
            log.error("Validation error: {}", bindingResult);
            model.addAttribute("connectionRequest", connectionRequest);
            model.addAttribute("tariffId", tariffId);
            model.addAttribute("additionalServices", additionalServiceRepository.readAll());
            return "request";
        }

        ConnectionRequest conReq = fillConRequest(connectionRequest);

        User userDetails = (User) authentication.getPrincipal();
        conReq.setSubscriber(userDetails.getUserId());
        conReq.setTariff(tariff.get());

        connectionRequestRepository.create(conReq);

        connectionRequest.getAdditionalServiceList()
                .iterator()
                .forEachRemaining(service -> requestAdditionalServicesRepository.create(
                        new RequestAdditionalServices(conReq, service, RequestAdditionalServices.Status.expected)
                ));

        return "redirect:/tariffs";
    }

    private ConnectionRequest fillConRequest(ConnectionRequestForm connectionRequestForm) {
        ConnectionRequest conReq = new ConnectionRequest();

        conReq.setDateOfChange(new Date());
        conReq.setStatus(ConnectionRequest.RequestStatusType.IN_PROCESSING);
        conReq.setAddress(connectionRequestForm.getAddress());
        conReq.setCity(connectionRequestForm.getCity());
        return conReq;
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String additionalServicesPage(Model model) {
        model.addAttribute("additionalServices", additionalServiceRepository.readAll());
        return "services";
    }
}
