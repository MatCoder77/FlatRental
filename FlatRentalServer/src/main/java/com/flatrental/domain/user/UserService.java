package com.flatrental.domain.user;

import com.flatrental.api.UserDTO;
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

    public User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NO_SUCH_USER, userId)));
    }

    public List<User> getUsersFromDatabase(List<Long> usersId){
        return userRepository.findAllById(usersId);
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
