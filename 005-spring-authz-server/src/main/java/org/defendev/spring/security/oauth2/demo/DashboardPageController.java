package org.defendev.spring.security.oauth2.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = {"/"})
@Controller
public class DashboardPageController {

    @RequestMapping(path = {"dashboard/"}, method = {GET})
    public ModelAndView dashboardPage() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("dashboard.th");
        return mav;
    }

    @RequestMapping(path = {"info/"}, method = {GET})
    public ModelAndView infoPage() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("info.th");
        return mav;
    }

}
