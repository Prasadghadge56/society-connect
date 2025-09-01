package com.society.service;

import com.society.entity.User;
import com.society.enums.Role;
import com.society.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
// register new user
    public User register(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw  new RuntimeException("Username already exists");
        }
        return userRepository.save(user);
    }
// login user
    public User login(String username , String password){
        return userRepository.findByUsernameAndPassword(username , password)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
// get profile by id
    public User getprofile(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }
 // get all users

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public List<User> getAdminUsers() {
        return userRepository.findByRole(Role.ADMIN);
    }
}
