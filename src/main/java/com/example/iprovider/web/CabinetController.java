package com.example.iprovider.web;

import com.example.iprovider.data.TransactionRepository;
import com.example.iprovider.data.UserRepository;
import com.example.iprovider.data.UserTariffsRepository;
import com.example.iprovider.entities.Transaction;
import com.example.iprovider.entities.User;
import com.example.iprovider.entities.forms.ChangePasswordForm;
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

@Slf4j
@Controller
public class CabinetController {
    private final UserTariffsRepository userTariffsRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CabinetController(UserTariffsRepository userTariffsRepository,
                             TransactionRepository transactionRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.userTariffsRepository = userTariffsRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        Transaction transaction1 = transactionRepository.create(transaction);
        System.out.println(transaction1);
        return "redirect:/cabinet";
    }

    @RequestMapping(value = "/cabinet/history", method = RequestMethod.GET)
    public String getHistory(Model model, Authentication authentication,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size) {
        User userDetails = (User) authentication.getPrincipal();
        model.addAttribute("transactions", transactionRepository.readAllByUserBalanceId(userDetails.getUserId(), page, size));

        int number = transactionRepository.getAmountByUserBalanceId(userDetails.getUserId());
        int maxPage = (int)Math.ceil(number * 1.0 / size);
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
}
