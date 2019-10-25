package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSpecificInfoDTO {

    Boolean doesUserLikeIt;
    Boolean isMarkedAsFavourite;

}
