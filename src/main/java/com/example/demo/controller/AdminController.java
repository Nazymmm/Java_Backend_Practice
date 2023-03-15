package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.UserDtls;
import com.example.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.exception.RecordNotFoundException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/")
    public String home() {
        return "admin/home";
    }
    @Autowired
    UserServiceImpl service;

    @RequestMapping
    public String getAllUsers(Model model)
    {
        System.out.println("getAllUsers");

        List<UserDtls> list = service.getAllUsers();

        model.addAttribute("users", list);

        return "list-users";
    }

//    @RequestMapping(path = {"/edit", "/edit/{id}"})

    @GetMapping(path = {"/editUser", "/edit/{id}"})
    public String editUserById(Model model, @PathVariable("id") Optional<Integer> id)
            throws RecordNotFoundException
    {

        System.out.println("editUserById" + id);
        if (id.isPresent()) {
            UserDtls entity = service.getUserById(id.get());
            model.addAttribute("user", entity);
        } else {
            model.addAttribute("user", new UserDtls());
        }

        return "add-edit-user";
    }


    @GetMapping("/{id}/deleteUser")
    public String deleteUserById(Model model, @PathVariable("id") Integer id)
            throws RecordNotFoundException
    {

        System.out.println("deleteUserById" + id);

        service.deleteUserById(id);
        return "redirect:/admin";
    }

//    @RequestMapping(path = "/createUser", method = { RequestMethod.GET })


    @GetMapping("/createUser")
    public String createOrUpdateUserView(Model model)
    {
        System.out.println("createOrUpdateUser ");

        model.addAttribute("user", new UserDtls());


        return "add-edit-user";
    }

    @PostMapping("/createUser")
    public String createOrUpdateUser(UserDtls user)
    {
        System.out.println("createOrUpdateUser ");

        service.createOrUpdateUser(user);

        return "redirect:/admin";
    }





}


