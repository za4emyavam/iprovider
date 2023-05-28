package com.example.iprovider.web.admin;

import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.entities.ConnectionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/admin/requests")
public class RequestsController {

    private final ConnectionRequestRepository connectionRequestRepository;

    public RequestsController(ConnectionRequestRepository connectionRequestRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
    }

    @ModelAttribute
    public void addRequestToModel(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ConnectionRequest> pagedConnectionRequest = connectionRequestRepository.readAll(pageRequest);

        model.addAttribute("pageable", pagedConnectionRequest);
        model.addAttribute("requests", pagedConnectionRequest.getContent());
        model.addAttribute("max", pagedConnectionRequest.getTotalPages() - 1);
    }

    @GetMapping
    public String getPage() {
        return "admin/requests";
    }

}
