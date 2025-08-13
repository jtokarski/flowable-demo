package org.defendev.spring.security.oauth2.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = {"/"})
@Controller
public class FormLoginPageController {

    private final ImaginaryUserService imaginaryUserService;

    @Autowired
    public FormLoginPageController(ImaginaryUserService imaginaryUserService) {
        this.imaginaryUserService = imaginaryUserService;
    }

    @ModelAttribute(name = "imaginaryUsers")
    public List<ImaginaryUser> imaginaryUsers() {
        return imaginaryUserService.getUsers();
    }

    @ModelAttribute(name = "signInPath")
    public String signInPath(HttpServletRequest request) {
        return WebSecurity.SIGN_IN_PATH;
    }

    @RequestMapping(path = {WebSecurity.SIGN_IN_PATH}, method = {GET})
    public ModelAndView loginPage(HttpSession session) {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("loginPage.th");

        final Object authenticationException = session.getAttribute(AUTHENTICATION_EXCEPTION);
        final String loginError;
        if (nonNull(authenticationException)) {
            loginError = authenticationException.getClass().getCanonicalName();
            session.removeAttribute(AUTHENTICATION_EXCEPTION);
        } else {
            loginError = Boolean.FALSE.toString();
        }
        mav.addObject("loginError", loginError);

        return mav;
    }

}
