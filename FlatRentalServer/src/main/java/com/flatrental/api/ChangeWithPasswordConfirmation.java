package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeWithPasswordConfirmation {

    private String password;
    private String value;

}
