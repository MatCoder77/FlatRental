package com.flatrental.domain.user;

import com.flatrental.api.UserDTO;
import com.flatrental.domain.userrole.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class UserService {

    private static final String USERNAME_ALREADY_TAKEN_MSG = "User with username {0} already exists!";
    private static final String EMAIL_ALREADY_TAKEN_MSG = "User with email {0} already exists!";
    private static final String NO_SUCH_USER = "There is no user with id {0}";

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    public User registerUser(User newUser) {
        validateUsernameUniqueness(newUser);
        validateEmailUniqueness(newUser);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    private void validateUsernameUniqueness(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(MessageFormat.format(USERNAME_ALREADY_TAKEN_MSG, user.getUsername()));
        }
    }

    private void validateEmailUniqueness(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(MessageFormat.format(EMAIL_ALREADY_TAKEN_MSG, user.getEmail()));
        }
    }

    public User getUserFormDatabase(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NO_SUCH_USER, userId)));
    }

    public List<User> getUsersFromDatabase(List<Long> usersId){
        return userRepository.findAllById(usersId);
    }

}