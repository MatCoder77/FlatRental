package com.flatrental.domain.announcement.address;

import com.flatrental.api.AddressDTO;
import com.flatrental.api.CommuneDTO;
import com.flatrental.api.DistrictDTO;
import com.flatrental.api.LocalityDTO;
import com.flatrental.api.LocalityDistrictDTO;
import com.flatrental.api.LocalityPartDTO;
import com.flatrental.api.StreetDTO;
import com.flatrental.api.VoivodeshipDTO;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.commune.CommuneService;
import com.flatrental.domain.locations.district.DistrictService;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.localitypart.LocalityPartService;
import com.flatrental.domain.locations.street.StreetService;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final VoivodeshipService voivodeshipService;
    private final DistrictService districtService;
    private final CommuneService communeService;
    private final LocalityService localityService;
    private final LocalityDistrictService localityDistrictService;
    private final LocalityPartService localityPartService;
    private final StreetService streetService;

    public Address mapToAddress(AddressDTO addressDTO) {
        var builder = Address.builder();

        Optional.ofNullable(addressDTO.getVoivodeship())
                .map(VoivodeshipDTO::getId)
                .map(voivodeshipService::getExistingVoivodeship)
                .ifPresent(builder::voivodeship);
        Optional.ofNullable(addressDTO.getDistrict())
                .map(DistrictDTO::getId)
                .map(districtService::getExistingDistrict)
                .ifPresent(builder::district);
        Optional.ofNullable(addressDTO.getCommune())
                .map(CommuneDTO::getId)
                .map(communeService::getExistingCommune)
                .ifPresent(builder::commune);
        Optional.ofNullable(addressDTO.getLocality())
                .map(LocalityDTO::getId)
                .map(localityService::getExistingLocality)
                .map(AbstractLocality::fromLocality)
                .ifPresent(builder::locality);
        Optional.ofNullable(addressDTO.getLocalityDistrict())
                .map(LocalityDistrictDTO::getId)
                .map(localityDistrictService::getExistingLocalityDistrict)
                .map(AbstractLocality::fromLocalityDistrict)
                .ifPresent(builder::localityDistrict);
        Optional.ofNullable(addressDTO.getLocalityPart())
                .map(LocalityPartDTO::getId)
                .map(localityPartService::getExistingLocalityPart)
                .map(AbstractLocality::fromLocalityPart)
                .ifPresent(builder::localityPart);
        Optional.ofNullable(addressDTO.getStreet())
                .map(StreetDTO::getId)
                .map(streetService::getExistingStreet)
                .ifPresent(builder::street);

        Address address = builder.build();
        validateAddressItemsHierarchy(address);
        return address;
    }


    private void validateAddressItemsHierarchy(Address address) {
        // TODO: implement validation
    }

    public AddressDTO mapToAddressDTO(Address address) {
        var builder = AddressDTO.builder();
        Optional.ofNullable(address.getVoivodeship())
                .map(voivodeshipService::mapToVoivodeshipDTO)
                .ifPresent(builder::voivodeship);
        Optional.ofNullable(address.getDistrict())
                .map(districtService::mapToDistrictDTO)
                .ifPresent(builder::district);
        Optional.ofNullable(address.getCommune())
                .map(communeService::mapToCommuneDTO)
                .ifPresent(builder::commune);
        Optional.ofNullable(address.getLocality())
                .map(Locality::fromAbstractLocality)
                .map(localityService::mapToLocalityDTO)
                .ifPresent(builder::locality);
        Optional.ofNullable(address.getLocalityDistrict())
                .map(LocalityDistrict::fromAbstractLocality)
                .map(localityDistrictService::mapToLocalityDistrictDTO)
                .ifPresent(builder::localityDistrict);
        Optional.ofNullable(address.getLocalityPart())
                .map(LocalityPart::fromAbstractLocality)
                .map(localityPartService::mapToLocalityPart)
                .ifPresent(builder::localityPart);
        Optional.ofNullable(address.getStreet())
                .map(streetService::mapToStreetDTO)
                .ifPresent(builder::street);
        return builder.build();
    }

}
