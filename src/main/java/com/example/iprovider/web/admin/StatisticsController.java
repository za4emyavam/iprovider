package com.example.iprovider.web.admin;

import com.example.iprovider.data.ChecksRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.data.UserRepository;
import com.example.iprovider.data.UserTariffsRepository;
import com.example.iprovider.entities.Tariff;
import com.example.iprovider.entities.User;
import com.example.iprovider.entities.UserTariffs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class StatisticsController {

    private final UserTariffsRepository userTariffsRepository;
    private final TariffRepository tariffRepository;
    private final ChecksRepository checksRepository;
    private final UserRepository userRepository;

    public StatisticsController(UserTariffsRepository userTariffsRepository,
                                TariffRepository tariffRepository,
                                ChecksRepository checksRepository,
                                UserRepository userRepository) {
        this.userTariffsRepository = userTariffsRepository;
        this.tariffRepository = tariffRepository;
        this.checksRepository = checksRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/admin/statistics", method = RequestMethod.GET)
    public String getStaticsPage(Model model) {
        model.addAttribute("firstInRating", sortRating(getRating()).entrySet().iterator().next());
        model.addAttribute("lastCheck", checksRepository.readLast());
        model.addAttribute("amountOfUsers", userRepository.getAmount());
        model.addAttribute("amountOfSubscribers", userTariffsRepository.getAmountOfSubscribers());
        return "admin/statistics";
    }

    @RequestMapping(value = "/admin/statistics/rating", method = RequestMethod.GET)
    public String getRatingPage(Model model) {
        model.addAttribute("rating", sortRating(getRating()));
        return "admin/statistics/rating";
    }

    @RequestMapping(value = "/admin/statistics/users", method = RequestMethod.GET)
    public String getUsersPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size) {
        Iterable<User> users = userRepository.readAllHasTariffs();
        model.addAttribute("userList", users);
        model.addAttribute("amount", StreamSupport.stream(users.spliterator(), false).count());
        return "admin/statistics/users";
    }

    private Map<Tariff, Long> getRating() {
        Map<Tariff, Long> result = new HashMap<>();
        for (Tariff t : tariffRepository.readAll()) {
            result.put(t, 0L);
        }
        for(UserTariffs ut : userTariffsRepository.readAll()) {
            result.put(ut.getTariffId(), result.get(ut.getTariffId()) + 1);
        }
        return result;
    }

    private Map<Tariff, Long> sortRating(Map<Tariff, Long> rating) {
        return rating.entrySet().stream().
                sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).
                collect(Collectors.toMap(Map.Entry :: getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
