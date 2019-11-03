package com.flatrental.domain.authentication;

import com.flatrental.domain.user.User;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class RegistrationConfirmationEvent extends ApplicationEvent {

    private User user;

    public RegistrationConfirmationEvent(User newlyRegisteredUser) {
        super(newlyRegisteredUser);
        this.user = newlyRegisteredUser;
    }

}
