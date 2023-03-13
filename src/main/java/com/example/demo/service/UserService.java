package com.example.demo.service;

import com.example.demo.model.UserDtls;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public UserDtls createUser(UserDtls user);

    public boolean checkEmail(String email);

}