package com.flatrental.domain.user;

import com.flatrental.domain.statistics.StatisticsService;
import com.flatrental.infrastructure.exceptions.IllegalArgumentAppException;
import com.flatrental.infrastructure.security.UserInfo;
import com.flatrental.infrastructure.utils.Exceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final String USERNAME_ALREADY_TAKEN_MSG = "User with username {0} already exists!";
    private static final String EMAIL_ALREADY_TAKEN_MSG = "User with email {0} already exists!";
    private static final String NO_SUCH_USER = "There is no user with id {0}";
    private static final String PASSWORD_NOT_PASSED_VALIDATION_RULES = "Password not passed validation rules";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_REGEX = Pattern.compile("[^@ ]+@[^@ ]+\\.[^@ ]+");
    private static final String EMAIL_INCORRECT = "Supplied email is incorrect";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StatisticsService statisticsService;

    public User createUser(User userToCreate) {
        validateUserOnCreate(userToCreate);
        prepareUserBeforeCreate(userToCreate);
        return userRepository.save(userToCreate);
    }

    private void validateUserOnCreate(User user) {
        validateUsernameUniqueness(user);
        validateEmail(user.getEmail());
        validateEmailUniqueness(user.getEmail());
        validatePasswordRules(user.getPassword());
    }

    private void validateUsernameUniqueness(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentAppException(MessageFormat.format(USERNAME_ALREADY_TAKEN_MSG, user.getUsername()));
        }
    }

    private void validateEmailUniqueness(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentAppException(MessageFormat.format(EMAIL_ALREADY_TAKEN_MSG, email));
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentAppException(EMAIL_INCORRECT);
        }
    }

    private void validatePasswordRules(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentAppException(PASSWORD_NOT_PASSED_VALIDATION_RULES);
        }
    }

    private void prepareUserBeforeCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserStatistics(new UserStatistics());
        user.setRole(UserRole.ROLE_USER);
    }

    public User getExistingUser(UserInfo userInfo) {
        return Optional.ofNullable(userInfo)
                .map(UserInfo::getId)
                .map(this::getExistingUser)
                .orElseThrow(Exceptions::getCannotGetUserFromContextException);
    }

    public User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentAppException(MessageFormat.format(NO_SUCH_USER, userId)));
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updatePassword(Long userId, String newPassword) {
        validatePasswordRules(newPassword);
        User user = getExistingUser(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    public void updateEmail(Long userId, String email) {
        validateEmail(email);
        validateEmailUniqueness(email);
        User user = getExistingUser(userId);
        user.setEmail(email);
    }

    public void updateAvatar(Long userId, String filename) {
        User user = getExistingUser(userId);
        user.setAvatar(filename);
    }

    public void updatePhoneNumber(Long userId, String phoneNumber) {
        User user = getExistingUser(userId);
        user.setPhoneNumber(phoneNumber);
    }

    public void updateAbout(Long userId, String description) {
        User user = getExistingUser(userId);
        user.setAbout(description);
    }

    public void updateUserStatistics(User user) {
        statisticsService.updateUserStatistics(user);
        userRepository.save(user);
    }

}
