package com.flatrental.domain.user;

import com.flatrental.api.UserDTO;
import com.flatrental.domain.file.FileService;
import com.flatrental.domain.statistics.StatisticsService;
import com.flatrental.domain.userrole.UserRole;
import com.flatrental.domain.userrole.UserRoleName;
import com.flatrental.infrastructure.exceptions.IllegalArgumentAppException;
import com.flatrental.infrastructure.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
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
    private final FileService fileService;
    private final StatisticsService statisticsService;

    public User registerUser(User newUser) {
        validateUsernameUniqueness(newUser);
        validateEmail(newUser.getEmail());
        validateEmailUniqueness(newUser.getEmail());
        validatePasswordRules(newUser.getPassword());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
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

    public Optional<User> findUser(UserInfo userInfo) {
        return Optional.ofNullable(userInfo)
                .map(UserInfo::getId)
                .map(this::getExistingUser);
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

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(Optional.ofNullable(user.getAvatar())
                        .map(fileService::getDownloadUri)
                        .orElse(null))
                .roles(user.getRoles().stream()
                        .map(UserRole::getName)
                        .map(UserRoleName::name)
                        .collect(Collectors.toSet()))
                .statistics(statisticsService.mapToUserStatisticsDTO(user.getUserStatistics()))
                .about(user.getAbout())
                .build();
    }

    public void setAvatar(String filename, Long userId) {
        User user = getExistingUser(userId);
        user.setAvatar(filename);
        userRepository.save(user);
    }

    public void setNewPassword(User user, String newPassword) {
        validatePasswordRules(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void setNewEmail(User user, String email) {
        validateEmail(email);
        validateEmailUniqueness(email);
        user.setEmail(email);
        userRepository.save(user);
    }

    public void setNewPhoneNumber(User user, String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    public void setUserProfileDescription(User user, String description) {
        user.setAbout(description);
        userRepository.save(user);
    }

    public void updateUserStatistics(User user) {
        statisticsService.updateUserStatistics(user);
        userRepository.save(user);
    }

}
