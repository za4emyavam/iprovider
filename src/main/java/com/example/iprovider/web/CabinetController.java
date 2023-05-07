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
import org.springframework.validation.Errors;
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

    @RequestMapping(value = "/cabinet/refill", method = RequestMethod.GET)
    public String getRefill(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "cabinet/refill";
    }

    @RequestMapping(value = "/cabinet/profile", method = RequestMethod.GET)
    public String cabinetModel(Model model, Authentication authentication) {
        //Updates authentication
        User user = userRepository.findByUsername(authentication.getName());
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        User userDetails = (User) updatedAuthentication.getPrincipal();
        model.addAttribute("tariffs", userTariffsRepository.readById(userDetails.getUserId()));
        return "cabinet/profile";
    }

    @RequestMapping(value = "/cabinet/profile", method = RequestMethod.POST)
    public String unsubscribeTariff(Authentication authentication, @RequestParam long tariffId) {
        User user = (User) authentication.getPrincipal();
        //TODO delete by userTariffs id
        userTariffsRepository.deleteByUserIdTariffId(user.getUserId(), tariffId);

        return "redirect:/cabinet";
    }

    @RequestMapping(value = "/cabinet/refill", method = RequestMethod.POST)
    public String processRefill(@ModelAttribute @Valid Transaction transaction,
                                Errors errors,
                                Authentication authentication) {
        if (errors.hasErrors()) {
            log.error("Validation error {}", errors);
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
    public String processNewPassword(Model model, @ModelAttribute @Valid ChangePasswordForm changePasswordForm,
                                     Errors errors,
                                     Authentication authentication) {
        if (errors.hasErrors()) {
            log.error("Validation error, {}", errors);
            return "cabinet/services";
        }
        User user = (User) authentication.getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        if (passwordEncoder.matches(changePasswordForm.getOldPass(), user.getPassword())) {
            if (changePasswordForm.getNewPass().equals(changePasswordForm.getOneMoreNewPass())) {
                user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPass()));
                userRepository.updatePass(user);
                return "redirect:/cabinet";
            } else {
                model.addAttribute("msg", "newPassesNotEqual");
            }
        } else {
            model.addAttribute("msg", "oldPassError");
        }
        return "cabinet/services";
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
                                       @ModelAttribute ConnectionRequestForm connectionRequestForm, Model model,
                                       Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();
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
            requestAdditionalServicesRepository.delete(ras.getRequestAdditionalServicesId());
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

        //TODO Validation
        return "redirect:/cabinet/requests";
    }

    @RequestMapping(value = "/cabinet/requests/request_info/delete", method = RequestMethod.POST)
    public String processDeleteRequest(@RequestParam Long requestId) {
        Optional<ConnectionRequest> connectionRequest = connectionRequestRepository.read(requestId);
        if (connectionRequest.isEmpty() || connectionRequest.get().getStatus() != ConnectionRequest.RequestStatusType.IN_PROCESSING)
            return "redirect:/cabinet/requests";
        for (RequestAdditionalServices ras :
                requestAdditionalServicesRepository.readByConnectionRequestId(requestId)) {
            requestAdditionalServicesRepository.delete(ras.getRequestAdditionalServicesId());
        }
        ConnectionRequest updatedConnectionRequest = connectionRequest.get();
        updatedConnectionRequest.setStatus(ConnectionRequest.RequestStatusType.REJECTED);
        connectionRequestRepository.update(updatedConnectionRequest);
        return "redirect:/cabinet/requests";
    }

    @RequestMapping(value = "/cabinet/details", method = RequestMethod.GET)
    public String getUpdateDetailsPage(Model model, Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();

        model.addAttribute("newUserDetails", new UserDetailsForm());
        model.addAttribute("userDetails", new UserDetailsForm(
                userDetails.getEmail(),
                userDetails.getFirstname(),
                userDetails.getSurname(),
                userDetails.getTelephoneNumber()
        ));

        return "cabinet/details";
    }

    @RequestMapping(value = "/cabinet/details", method = RequestMethod.POST)
    public String processUpdateUserDetails(@ModelAttribute UserDetailsForm newUserDetails,
                                           Authentication authentication,
                                           Model model) {
        User userDetails = (User) authentication.getPrincipal();
        if (!userDetails.getEmail().equals(newUserDetails.getEmail())) {
            if (userRepository.findByUsername(newUserDetails.getEmail()) != null) {
                model.addAttribute("error_email", "This email is already taken");
                model.addAttribute("newUserDetails", new UserDetailsForm());
                model.addAttribute("userDetails", new UserDetailsForm(
                        userDetails.getEmail(),
                        userDetails.getFirstname(),
                        userDetails.getSurname(),
                        userDetails.getTelephoneNumber()
                ));
            }
        }

        newUserDetails.setUserId(userDetails.getUserId());
        userRepository.updateDetails(newUserDetails);

        //TODO validation
        return "redirect:/cabinet/profile";
    }
}
