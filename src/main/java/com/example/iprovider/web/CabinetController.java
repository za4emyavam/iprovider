package com.example.iprovider.web;

import com.example.iprovider.data.*;
import com.example.iprovider.entities.*;
import com.example.iprovider.entities.forms.ChangePasswordForm;
import com.example.iprovider.entities.forms.ConnectionRequestForm;
import com.example.iprovider.entities.forms.UserDetailsForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
public class CabinetController {
    private final UserTariffsRepository userTariffsRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    private final RequestAdditionalServicesRepository requestAdditionalServicesRepository;
    private final TariffRepository tariffRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final PasswordEncoder passwordEncoder;

    public CabinetController(UserTariffsRepository userTariffsRepository,
                             TransactionRepository transactionRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             ConnectionRequestRepository connectionRequestRepository,
                             RequestAdditionalServicesRepository requestAdditionalServicesRepository,
                             TariffRepository tariffRepository,
                             AdditionalServiceRepository additionalServiceRepository) {
        this.userTariffsRepository = userTariffsRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.connectionRequestRepository = connectionRequestRepository;
        this.requestAdditionalServicesRepository = requestAdditionalServicesRepository;
        this.tariffRepository = tariffRepository;
        this.additionalServiceRepository = additionalServiceRepository;
    }

    @RequestMapping(value = "/cabinet", method = RequestMethod.GET)
    public String cabinet() {
        return "redirect:/cabinet/profile";
    }

    @RequestMapping(value = "/cabinet/profile", method = RequestMethod.GET)
    public String cabinetModel(Model model, Authentication authentication) {
        //Updates authentication
        User user = userRepository.findByUsername(authentication.getName());
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        User userDetails = (User) updatedAuthentication.getPrincipal();
        List<UserTariffs> tariffs = new ArrayList<>();
        userTariffsRepository.readById(userDetails.getUserId()).forEach(tariffs::add);

        model.addAttribute("tariffs", tariffs);
        return "cabinet/profile";
    }

    @RequestMapping(value = "/cabinet/profile", method = RequestMethod.POST)
    public String unsubscribeTariff(@RequestParam("tariffId") long userTariffId) {
        Optional<UserTariffs> ut = userTariffsRepository.read(userTariffId);
        if (ut.isPresent())
            userTariffsRepository.delete(userTariffId);

        return "redirect:/cabinet";
    }


    @RequestMapping(value = "/cabinet/refill", method = RequestMethod.GET)
    public String getRefill(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "cabinet/refill";
    }

    @RequestMapping(value = "/cabinet/refill", method = RequestMethod.POST)
    public String processRefill(@ModelAttribute("transaction") @Valid Transaction transaction,
                                BindingResult bindingResult,
                                Authentication authentication) {
        if (bindingResult.hasErrors()) {
            log.error("Validation error {}", bindingResult);
            return "cabinet/refill";
        }
        User userDetails = (User) authentication.getPrincipal();
        transaction.setBalanceId(userDetails.getUserId());
        transaction.setType(Transaction.TransactionType.REFILL);
        transaction.setTransactionStatus(Transaction.TransactionStatusType.SUCCESSFUL);
        transactionRepository.create(transaction);
        return "redirect:/cabinet";
    }

    @RequestMapping(value = "/cabinet/history", method = RequestMethod.GET)
    public String getHistory(Model model, Authentication authentication,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size) {
        User userDetails = (User) authentication.getPrincipal();
        model.addAttribute("transactions", transactionRepository.readAllByUserBalanceId(userDetails.getUserId(), page, size));

        int number = transactionRepository.getAmountByUserBalanceId(userDetails.getUserId());
        int maxPage = (int) Math.ceil(number * 1.0 / size);
        if (page <= 0 || page > maxPage) {
            page = 1;
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("maxPage", maxPage);
        return "cabinet/history";
    }

    @RequestMapping(value = "/cabinet/services", method = RequestMethod.GET)
    public String getService(Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "cabinet/services";
    }

    @RequestMapping(value = "/cabinet/services", method = RequestMethod.POST)
    public String processNewPassword(Model model, @ModelAttribute("changePasswordForm") @Valid ChangePasswordForm changePasswordForm,
                                     BindingResult bindingResult,
                                     Authentication authentication) {
        //If have jakarta.validation errors
        if (bindingResult.hasErrors()) {
            log.error("Validation error, {}", bindingResult);
            return "cabinet/services";
        }

        //Check, is old password matches to current
        User user = (User) authentication.getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        if (!passwordEncoder.matches(changePasswordForm.getOldPass(), user.getPassword())) {
            model.addAttribute("passError", "Wrong password");
            return "cabinet/services";
        }

        //If new passwords do not match
        if (!changePasswordForm.getNewPass().equals(changePasswordForm.getOneMoreNewPass())) {
            model.addAttribute("newPassError", "Passwords do not match");
            return "cabinet/services";
        }

        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPass()));
        userRepository.updatePass(user);

        return "redirect:/cabinet/profile";
    }

    @RequestMapping(value = "/cabinet/requests", method = RequestMethod.GET)
    public String getRequests(Model model, Authentication authentication) {
        List<ConnectionRequest> connectionRequestList = new ArrayList<>();
        User userDetails = (User) authentication.getPrincipal();
        connectionRequestRepository.readAllBySubscriber(userDetails.getUserId()).forEach(connectionRequestList::add);
        model.addAttribute("connectionRequest", connectionRequestList);
        return "cabinet/requests";
    }

    @RequestMapping(value = "/cabinet/requests/request_info", method = RequestMethod.GET)
    public String getRequestInfoPage(@RequestParam Long requestId, Model model) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        if (connectionRequest.isEmpty()) {
            return "redirect:/cabinet/requests";
        }
        model.addAttribute("request", connectionRequest.get());

        List<RequestAdditionalServices> requestAdditionalServices = new ArrayList<>();
        requestAdditionalServicesRepository.
                readByConnectionRequestId(requestId).forEach(requestAdditionalServices::add);
        model.addAttribute("additionalServices", requestAdditionalServices);
        return "cabinet/requests/request_info";
    }

    @RequestMapping(value = "/cabinet/requests/update", method = RequestMethod.GET)
    public String getUpdateRequestPage(@RequestParam Long requestId, Model model, Authentication authentication) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        User userDetails = (User) authentication.getPrincipal();
        if (connectionRequest.isEmpty() ||
                connectionRequest.get().getStatus() != ConnectionRequest.RequestStatusType.IN_PROCESSING ||
                !Objects.equals(connectionRequest.get().getSubscriber(), userDetails.getUserId())
        )
            return "redirect:/cabinet/requests";

        List<Long> additionalServicesId = new ArrayList<>();
        requestAdditionalServicesRepository.readByConnectionRequestId(connectionRequest.get().getConnectionRequestId())
                .forEach(r -> additionalServicesId.add(r.getServiceId().getAdditionalServiceId()));

        model.addAttribute("currentConnectionRequest", connectionRequest.get());
        model.addAttribute("currentAddServices", additionalServicesId);
        model.addAttribute("connectionRequestForm", new ConnectionRequestForm());
        model.addAttribute("tariffList", tariffRepository.readAll());
        model.addAttribute("addServices", additionalServiceRepository.readAll());

        return "cabinet/requests/update";
    }

    @RequestMapping(value = "/cabinet/requests/update", method = RequestMethod.POST)
    public String processUpdateRequest(@RequestParam Long requestId,
                                       @ModelAttribute("connectionRequestForm") @Valid ConnectionRequestForm connectionRequestForm,
                                       BindingResult bindingResult,
                                       Model model,
                                       Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            log.error("Validation error: {}", bindingResult);

            Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
            if (connectionRequest.isEmpty() ||
                    connectionRequest.get().getStatus() != ConnectionRequest.RequestStatusType.IN_PROCESSING ||
                    !Objects.equals(connectionRequest.get().getSubscriber(), userDetails.getUserId()))
                return "redirect:/cabinet/requests";

            List<Long> additionalServicesId = new ArrayList<>();
            requestAdditionalServicesRepository.readByConnectionRequestId(connectionRequest.get().getConnectionRequestId())
                    .forEach(r -> additionalServicesId.add(r.getServiceId().getAdditionalServiceId()));

            model.addAttribute("currentConnectionRequest", connectionRequest.get());
            model.addAttribute("currentAddServices", additionalServicesId);
            model.addAttribute("tariffList", tariffRepository.readAll());
            model.addAttribute("addServices", additionalServiceRepository.readAll());
            model.addAttribute("connectionRequestForm", connectionRequestForm);

            return "cabinet/requests/update";
        }

        ConnectionRequest connectionRequest = new ConnectionRequest(
                requestId,
                userDetails.getUserId(),
                connectionRequestForm.getCity(),
                connectionRequestForm.getAddress(),
                connectionRequestForm.getTariff(),
                new Date(),
                ConnectionRequest.RequestStatusType.IN_PROCESSING
        );
        for (RequestAdditionalServices ras :
                requestAdditionalServicesRepository.readByConnectionRequestId(requestId)) {
            requestAdditionalServicesRepository.delete(ras.getRequestId().getConnectionRequestId(),
                    ras.getServiceId().getAdditionalServiceId());
        }

        connectionRequest = connectionRequestRepository.update(connectionRequest);
        if (connectionRequestForm.getAdditionalServiceList() != null) {
            for (AdditionalService service :
                    connectionRequestForm.getAdditionalServiceList()) {
                requestAdditionalServicesRepository.create(new RequestAdditionalServices(
                        connectionRequest, service, RequestAdditionalServices.Status.expected
                ));
            }
        }
        return "redirect:/cabinet/requests";
    }

    @RequestMapping(value = "/cabinet/requests/request_info/delete", method = RequestMethod.POST)
    public String processDeleteRequest(@RequestParam Long requestId) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        if (connectionRequest.isEmpty() || connectionRequest.get().getStatus() != ConnectionRequest.RequestStatusType.IN_PROCESSING)
            return "redirect:/cabinet/requests";
        for (RequestAdditionalServices ras :
                requestAdditionalServicesRepository.readByConnectionRequestId(requestId)) {
            requestAdditionalServicesRepository.delete(ras.getRequestId().getConnectionRequestId(),
                    ras.getServiceId().getAdditionalServiceId());
        }
        ConnectionRequest updatedConnectionRequest = connectionRequest.get();
        updatedConnectionRequest.setStatus(ConnectionRequest.RequestStatusType.REJECTED);
        connectionRequestRepository.update(updatedConnectionRequest);
        return "redirect:/cabinet/requests";
    }

    @RequestMapping(value = "/cabinet/details", method = RequestMethod.GET)
    public String getUpdateDetailsPage(Model model) {
        model.addAttribute("newUserDetails", new UserDetailsForm());
        return "cabinet/details";
    }

    @RequestMapping(value = "/cabinet/details", method = RequestMethod.POST)
    public String processUpdateUserDetails(@ModelAttribute("newUserDetails") @Valid UserDetailsForm newUserDetails,
                                           BindingResult bindingResult, Authentication authentication,
                                           Model model) {
        //If have jakarta.validation errors
        if (bindingResult.hasErrors()) {
            log.error("Validation error, {}", bindingResult);
            model.addAttribute("newUserDetails", newUserDetails);
            return "cabinet/details";
        }

        //Check, is old password matches to current
        User userDetails = (User) authentication.getPrincipal();
        userDetails = userRepository.findByUsername(userDetails.getUsername());
        if (!passwordEncoder.matches(newUserDetails.getPassword(), userDetails.getPassword())) {
            model.addAttribute("passError", "Wrong password");
            return "cabinet/details";
        }

        //Check, is new email free
        if (!userDetails.getEmail().equals(newUserDetails.getEmail()) && userRepository.findByUsername(newUserDetails.getEmail()) != null) {
            model.addAttribute("error_email", "This email is already taken");
            model.addAttribute("newUserDetails", newUserDetails);
            return "cabinet/details";
        }

        newUserDetails.setUserId(userDetails.getUserId());
        userRepository.updateDetails(newUserDetails);

        //TODO validation
        return "redirect:/cabinet/profile";
    }
}
