package com.example.event5.demo_event5.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class LoginController {
    @RequestMapping(value = "/login",method = {RequestMethod.GET,RequestMethod.POST})
    public String displayLoginPage(@RequestParam(value = "error",required = false)String error,
                                   @RequestParam(value = "logout",required = false)String logout,
                                   Model model){
        String errorMessage=null;
        String logout1=null;

        if(error!=null){
            errorMessage="Username or Password is incorrect";
        }
        else if(logout!=null){
            logout1="You have been succesfully logged out!!";
        }
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("logout",logout1);

        return "login.html";

    }
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

}