package com.example.demo.service;

import com.example.demo.model.UserDtls;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

     UserDtls createUser(UserDtls user);

     boolean checkEmail(String email);

     List<UserDtls> getAllUsers();

}
