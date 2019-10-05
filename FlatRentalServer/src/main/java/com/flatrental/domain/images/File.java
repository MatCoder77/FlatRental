package com.flatrental.domain.images;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class File {

    @NotNull
    String filename;

}
