/**
 * <h1>Bank Project</h1>
 * @author  Maciej Ostaszewski
 * @version 1.0
 * @since   2017-12-01
 */

package com.bank_system_project.controllers;

import com.bank_system_project.models.CashFlowDate;
import com.bank_system_project.models.TransactionsHistory;
import com.bank_system_project.services.TransactionsHistoryService;
import com.bank_system_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
public class CashFlowController {

    @Autowired
    UserService userService;

    @Autowired
    TransactionsHistoryService transactionsHistoryService;


    /**
     * Returns an Image object that can then be painted on the screen.
     * The url argument must specify an absolute {@link URL}. The name
     * argument is a specifier that is relative to the url argument.
     * <p>
     * This method always returns immediately, whether or not the
     * image exists. When this applet attempts to draw the image on
     * the screen, the data will be loaded. The graphics primitives
     * that draw the image will incrementally paint on the screen.
     *
     * @param  model  an absolute URL giving the base location of the image
     * @param  name the location of the image, relative to the url argument
     * @return      the image at the specified URL
     * @see
     */

    @RequestMapping(value = "/cashFlow.html", method = RequestMethod.GET)
    public String cashFlowDateForm(Model model) {
        model.addAttribute("cashFlowDate", new CashFlowDate());

        return "cashFlow.html";
    }

    @RequestMapping(value = "/cashFlow.html", method = RequestMethod.POST)
    public String searchCashFlows(@ModelAttribute("cashFlowDate") CashFlowDate d) {

        Date dateS = new Date();
        Date dateE = new Date();
        GregorianCalendar date1 = new GregorianCalendar(d.getYear(), d.getMonth() - 1, 1);
        GregorianCalendar date2 = new GregorianCalendar(d.getYear(), d.getMonth(), 1);

        dateS = date1.getTime();
        dateE = date2.getTime();

        List<TransactionsHistory> transactionsHistoryList = transactionsHistoryService.getAllByTimeInterval(userService.getUsername(), dateS, dateE);

        if (transactionsHistoryList.size() == 0){
            return "redirect:/cashFlow.html?cashFlowNone";
        }
        float a = 0;
        for (TransactionsHistory t : transactionsHistoryList){
            a += t.getAmount().floatValue();
        }


        return "redirect:/cashFlow.html?cashFlow="+a;
    }
}
