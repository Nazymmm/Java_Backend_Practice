package com.example.demo.service;


import java.util.ArrayList;
import java.util.List;
import com.example.demo.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Override
    public UserDtls createUser(UserDtls user) {

        user.setPassword(passwordEncode.encode(user.getPassword()));

        user.setRole("ROLE_USER");

        return userRepo.save(user);
    }

    @Override
    public boolean checkEmail(String email) {

        return userRepo.existsByEmail(email);
    }

    public List<UserDtls> getAllUsers()
    {
        System.out.println("getAllUsers");
        List<UserDtls> result = (List<UserDtls>) userRepo.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<UserDtls>();
        }
    }

    public UserDtls getUserById(Integer id) throws RecordNotFoundException
    {
        System.out.println("getUserById");
        Optional<UserDtls> user = userRepo.findById(id);

        if(user.isPresent()) {
            return user.get();
        } else {
            throw new RecordNotFoundException("No user record exist for given id");
        }
    }

    public UserDtls createOrUpdateUser(UserDtls entity)
    {
        System.out.println("createOrUpdateUser");
        // Create new entry
        if(entity.getId() == null || entity.getEmail()  == null)
        {
            entity = userRepo.save(entity);

            return entity;
        }
        else
        {
            // update existing entry
            Optional<UserDtls> user = userRepo.findById(entity.getId());
            if(user.isPresent())
            {
                UserDtls newEntity = user.get();
                newEntity.setEmail(entity.getEmail());
                newEntity.setFirstname(entity.getFirstname());
                newEntity.setPassword(entity.getPassword());
                newEntity = userRepo.save(newEntity);

                return newEntity;
            } else {
                entity = userRepo.save(entity);
                return entity;
            }
        }
    }


    public void deleteUserById(Integer id) throws RecordNotFoundException
    {
        System.out.println("deleteUserById");

        Optional<UserDtls> user = userRepo.findById(id);

        if(user.isPresent())
        {
            userRepo.deleteById(id);
        } else {
            throw new RecordNotFoundException("No user record exist for given id");
        }
    }
}
