package com.flatrental.domain.images;


import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class File {

    @NotNull
    String filename;

}
