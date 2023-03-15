package com.example.demo.controller;


//import javax.servlet.http.HttpSession;

//import jakarta.servlet.http.HttpSession;http
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.UserDtls;
import com.example.demo.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @ModelAttribute
    private void userDetails(Model m, Principal p){
        if(p!= null){
            String email = p.getName();
            UserDtls user = userRepo.findByEmail(email);
            m.addAttribute("user",user);
        }
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/createUser")
    public String createuser(@ModelAttribute UserDtls user, HttpSession session) {

        // System.out.println(user);

        boolean f = userService.checkEmail(user.getEmail());

        if (f) {
            session.setAttribute("msg", "Email Id already exists");
        }

        else {
            UserDtls userDtls = userService.createUser(user);
            if (userDtls != null) {
                session.setAttribute("msg", "Register Successfully");
            } else {
                session.setAttribute("msg", "Something wrong on server");
            }
        }

        return "redirect:/signin";
    }
    @GetMapping("/loadForgotPassword")
    public String loadForgotPassword(){
        return "forgot_password";
    }

    @GetMapping("/loadResetPassword/{id}")
    public String loadResetPassword(@PathVariable int id, Model m){
        m.addAttribute("id", id);
        return "reset_password";
    }



    @PostMapping("forgotPassword")
    public String forgotPassword(@RequestParam String email,@RequestParam String mobileNum,HttpSession session){

        UserDtls user = userRepo.findByEmailAndMobileNumber(email,mobileNum);
        if (user!=null){
            return "redirect:/loadResetPassword/" + user.getId();
        }else {
            session.setAttribute("msg","invalid email & mobile number");
            return "forgot_password";
        }
    }
    @PostMapping("/changePassword")
    public String resetPassword(@RequestParam String psw,@RequestParam Integer id,HttpSession session){
        UserDtls user = userRepo.findById(id).get();
        String encryptPsw=passwordEncoder.encode(psw);
        user.setPassword(encryptPsw);
        UserDtls updateuser = userRepo.save(user);

        if(updateuser!=null){
            session.setAttribute("msg","Password Change Successfully");
        }
        return "redirect:/loadForgotPassword";
    }
}
