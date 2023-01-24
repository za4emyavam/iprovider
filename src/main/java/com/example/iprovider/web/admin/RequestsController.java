package com.example.iprovider.web.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/requests")
public class RequestsController {

    @GetMapping
    public String getPage() {
        return "admin/requests";
    }

}
