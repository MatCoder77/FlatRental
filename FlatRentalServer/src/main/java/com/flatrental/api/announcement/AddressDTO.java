package com.flatrental.api.announcement;

import com.flatrental.api.location.CommuneDTO;
import com.flatrental.api.location.DistrictDTO;
import com.flatrental.api.location.LocalityDTO;
import com.flatrental.api.location.LocalityDistrictDTO;
import com.flatrental.api.location.LocalityPartDTO;
import com.flatrental.api.location.StreetDTO;
import com.flatrental.api.location.VoivodeshipDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class AddressDTO {

    @NotNull
    private VoivodeshipDTO voivodeship;

    @NotNull
    private DistrictDTO district;

    @NotNull
    private CommuneDTO commune;

    @NotNull
    private LocalityDTO locality;

    private LocalityDistrictDTO localityDistrict;
    private LocalityPartDTO localityPart;
    private StreetDTO street;

}
