package com.flatrental.domain.authentication;

import com.flatrental.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    private Date expiryDate;

    VerificationToken(String token, User user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

}
