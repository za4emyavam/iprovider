package com.example.iprovider.web.admin;

import com.example.iprovider.data.ConnectionRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/requests")
public class RequestsController {

    private final ConnectionRequestRepository connectionRequestRepository;

    public RequestsController(ConnectionRequestRepository connectionRequestRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
    }

    @ModelAttribute
    public void addRequestToModel(Model model) {
        model.addAttribute("requests", connectionRequestRepository.findAll());
    }

    @GetMapping
    public String getPage() {
        return "admin/requests";
    }

}
