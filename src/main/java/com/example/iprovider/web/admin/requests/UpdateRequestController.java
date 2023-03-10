package com.example.iprovider.web.admin.requests;

import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.data.UserRepository;
import com.example.iprovider.entities.ConnectionRequest;
import com.example.iprovider.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/requests/update")
public class UpdateRequestController {
    private final ConnectionRequestRepository connectionRequestRepository;
    private final UserRepository userRepository;

    public UpdateRequestController(ConnectionRequestRepository connectionRequestRepository, UserRepository userRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getRequest(Model model, @RequestParam long requestId) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        if (connectionRequest.isEmpty()) {
            return "redirect:/admin/requests";
        }
        Optional<User> subscriber = userRepository.read(connectionRequest.get().getSubscriber());
        if (subscriber.isEmpty()) {
            return "redirect:/admin/requests";
        }
        model.addAttribute("connectionRequest", connectionRequest.get());
        model.addAttribute("subscriber", subscriber.get());
        model.addAttribute("statuses", ConnectionRequest.RequestStatusType.values());
        return "admin/requests/update";
    }

    @PostMapping
    public String processRequest(@RequestParam long requestId,
                                 @RequestParam("status") String status) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        if (connectionRequest.isEmpty()) {
            return "redirect:/admin/requests";
        }
        System.out.println("Status ->" + status);
        ConnectionRequest request = connectionRequest.get();
        request.setStatus(ConnectionRequest.RequestStatusType.valueOf(status));
        connectionRequestRepository.update(request);
        return "redirect:/admin/requests";
    }
}
