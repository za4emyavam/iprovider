package com.example.iprovider.web.admin.requests;

import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.data.RequestAdditionalServicesRepository;
import com.example.iprovider.data.UserRepository;
import com.example.iprovider.entities.ConnectionRequest;
import com.example.iprovider.entities.RequestAdditionalServices;
import com.example.iprovider.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/requests/update")
public class UpdateRequestController {
    private final ConnectionRequestRepository connectionRequestRepository;
    private final UserRepository userRepository;
    private final RequestAdditionalServicesRepository requestAdditionalServicesRepository;

    public UpdateRequestController(ConnectionRequestRepository connectionRequestRepository,
                                   UserRepository userRepository,
                                   RequestAdditionalServicesRepository requestAdditionalServicesRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.userRepository = userRepository;
        this.requestAdditionalServicesRepository = requestAdditionalServicesRepository;
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

        List<RequestAdditionalServices> requestAdditionalServicesList = new ArrayList<>();
        requestAdditionalServicesRepository.readByConnectionRequestId(
                connectionRequest.get().getConnectionRequestId()
        ).forEach(requestAdditionalServicesList::add);
        for (RequestAdditionalServices t : requestAdditionalServicesList) {
            System.out.println(t);
        }
        model.addAttribute("additionalServices", requestAdditionalServicesList);
        return "admin/requests/update";
    }

    @PostMapping
    public String processRequest(@RequestParam long requestId,
                                 @RequestParam("status") String status) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        if (connectionRequest.isEmpty()) {
            return "redirect:/admin/requests";
        }
        ConnectionRequest request = connectionRequest.get();
        request.setStatus(ConnectionRequest.RequestStatusType.valueOf(status));

        if (request.getStatus() == ConnectionRequest.RequestStatusType.REJECTED) {
            for (RequestAdditionalServices ras :
                    requestAdditionalServicesRepository.readByConnectionRequestId(requestId)) {
                requestAdditionalServicesRepository.delete(ras.getRequestId().getConnectionRequestId(),
                        ras.getServiceId().getAdditionalServiceId());
            }
        }
        connectionRequestRepository.update(request);
        return "redirect:/admin/requests";
    }
}
