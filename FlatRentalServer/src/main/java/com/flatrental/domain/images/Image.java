package com.flatrental.domain.images;


import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Image {

    @NotNull
    String filename;

}
