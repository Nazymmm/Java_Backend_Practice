package com.example.demo.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @ModelAttribute
    private void userDetails(Model m, Principal p) {
        String email = p.getName();
        UserDtls user = userRepo.findByEmail(email);

        m.addAttribute("user", user);

    }

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @GetMapping("/changPass")
    public String loadChangePassword() {
        return "user/change_password";
    }

    @PostMapping("/updatePassword")
    public String changePassword(Principal p, @RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass, HttpSession session){
        String email=p.getName();
        UserDtls loginUser = userRepo.findByEmail(email);
        boolean f=passwordEncode.matches(oldPass,loginUser.getPassword());

        if(f){
            loginUser.setPassword(passwordEncode.encode(newPass));
            UserDtls updatePasswordUser=userRepo.save(loginUser);
            System.out.println("Correct");
            if (updatePasswordUser != null){
               session.setAttribute("msg","Password change success");
            }else {
                session.setAttribute("msg","something wrong on server");
            }
        }else {
            System.out.println("Wrong");
            session.setAttribute("errorMsg","Old password incorrect");
        }
        return "redirect:/changPass";
    }



}