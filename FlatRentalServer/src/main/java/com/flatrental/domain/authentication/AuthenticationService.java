package com.flatrental.domain.authentication;

import com.flatrental.api.LoginDTO;
import com.flatrental.api.RegistrationFormDTO;
import com.flatrental.api.TokenDTO;
import com.flatrental.domain.statistics.UserStatistics;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.domain.userrole.UserRole;
import com.flatrental.domain.userrole.UserRoleName;
import com.flatrental.domain.userrole.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private static final int VERIFICATION_TOKEN_EXPIRATION = 60 * 24;

    public UsernamePasswordAuthenticationToken getAuthenticationToken(LoginDTO loginDTO) {
        return new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword());
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    public TokenDTO mapToTokenDTO(String token) {
        return new TokenDTO(token);
    }

    public User createUserBasedOnRegistrationForm(RegistrationFormDTO registrationFormDTO) {
        UserRole userRole = userRoleService.getUserRoleFromDatabase(UserRoleName.ROLE_USER);
        User newUser = new User(null,
                registrationFormDTO.getName(),
                registrationFormDTO.getSurname(),
                registrationFormDTO.getUsername(),
                registrationFormDTO.getPassword(),
                registrationFormDTO.getEmail(),
                registrationFormDTO.getPhoneNumber(),
                null,
                null,
                new UserStatistics(),
                false,
                Collections.singleton(userRole),
                null);
        return userService.registerUser(newUser);
    }

    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        Date expiryDate = getExpiryDate(VERIFICATION_TOKEN_EXPIRATION);
        VerificationToken verificationToken = new VerificationToken(token, user, expiryDate);
        return verificationTokenRepository.save(verificationToken);
    }

    private Date getExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
