package com.example.iprovider.web.admin;

import com.example.iprovider.data.ConnectionRequestRepository;
import lombok.extern.slf4j.Slf4j;
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
    public void addRequestToModel(Model model, @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        int number = connectionRequestRepository.getAmount();
        int maxPage = (int)Math.ceil(number * 1.0 / size);
        if (page <= 0 || page > maxPage) {
            page = 1;
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("requests", connectionRequestRepository.readAll(page, size));
    }

    @GetMapping
    public String getPage() {
        return "admin/requests";
    }

}
