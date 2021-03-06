/**
 * <h1>Bank Project</h1>
 * @author  Maciej Ostaszewski
 * @version 1.0
 * @since   2017-12-01
 */
package com.bank_system_project.controllers;


import com.bank_system_project.models.*;
import com.bank_system_project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Controller
public class TopUpController {


    @Autowired
    TopUpService topUpService;

    @Autowired
    TransactionsHistoryService transactionsHistoryService;

    @Autowired
    UserService userService;

    @Autowired
    UserDetailsService userDetailsService;


    /**
     * Wyświetla strone z formularzem na doładowanie
     * @param model przechowuje obiekt do formularza
     * @return strona z formularzem
     */
    @RequestMapping(value = "/topUpForm.html", method = RequestMethod.GET)
    public String topUpForm(Model model){

        model.addAttribute("topUp", new TopUp());

        return "topUpForm.html";
    }

    /**
     * Zapisuje dołądowanie, zmienia stan konta użytkownika
     * @param t obiekt doładowania z formularza
     * @param bindingResult zawiera błędy z formularza
     * @return strona z formularzem na doladowanie
     * @return strona z informacją o błędzie
     * @return strona z informacją o sukcesie
     */
    @RequestMapping(value = "/topUpForm.html", method = RequestMethod.POST)
    public String processTopUpForm(@Valid @ModelAttribute("topUp") TopUp t, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "topUpForm.html";
        }

        if (t.getAmount().compareTo(userService.getCurrentLoggedUser().getUserDetails().getMeans()) > 0){

            return "redirect:/?transferErr";
        }

        t.setExecutionDate(new Date());
        User user = userService.getCurrentLoggedUser();
        UserDetails userDetails = userDetailsService.getOne(user.getUserDetails().getId());
        userDetails.setMeans(userDetails.getMeans().subtract(t.getAmount()));
        userDetailsService.save(userDetails);
        t.setUser(user);

        transactionsHistoryService.save(t);
        topUpService.save(t);


        return "redirect:/?topUpSuccess";
    }
    /**
     * Pobiera liste sieci komórkowych
     * @return lista sieci
     */
    @ModelAttribute("mobileNetworks")
    public List<MobileNetwork> loadMobileNetworks(){
        return topUpService.getAllMobileNetwork();
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        binder.registerCustomEditor(BigDecimal.class, "amount", new CustomNumberEditor(BigDecimal.class, numberFormat, false));

    }

}
