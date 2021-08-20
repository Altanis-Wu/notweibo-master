package com.example.notweibo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // UserService.java
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> user =
                userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException(id);
        }
        return user.get();
    }

    public void addNewUser(User newUser) {
        Optional<User> existingUser = userRepository.findUserByEmail(newUser.getEmail());
        if(existingUser.isPresent()){
            throw new UserEmailAlreadyInUseException(newUser.getEmail());
        }
        userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        else{
            throw new UserNotFoundException(id);
        }
    }

    @Transactional
    public void updateUser(Long userId, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new UserNotFoundException(userId);
        }

        User user = userOptional.get();

        // Check Id
        if (updatedUser.getId() != null && updatedUser.getId() != user.getId()){
            //TODO USe custom exception.
            throw new IllegalStateException("User ID in path and in request body are different.");
        }

        // Update Name
        if (updatedUser.getName() != null &&
                !Objects.equals(updatedUser.getName(), user.getName()) &&
                updatedUser.getName().length() > 0){
            user.setName(updatedUser.getName());
        }

        // Update email, checking for duplicates.
        if (updatedUser.getEmail() != null &&
                !Objects.equals(updatedUser.getEmail(), user.getEmail()) &&
                updatedUser.getEmail().length() > 0){

            Optional<User> userByUpdatedEmail = userRepository.findUserByEmail(updatedUser.getEmail());
            if (userByUpdatedEmail.isPresent()){
                throw new UserEmailAlreadyInUseException(updatedUser.getEmail());
            }
            user.setEmail(updatedUser.getEmail());
        }
    }
}
