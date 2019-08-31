package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.street.Street;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationSerachMapper {

    public LocationSearchDTO mapToLocationSearchDTO(Voivodeship voivodeship) {
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(District district) {
        Voivodeship voivodeship = district.getVoivodeship();
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(Commune commune) {
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(Locality locality) {
        Commune commune = locality.getCommune();
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .locality(LocalitySearchDTO.fromLocality(locality))
                .build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(LocalityDistrict localityDistrict) {
        Locality locality = localityDistrict.getParentLocality();
        Commune commune = locality.getCommune();
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .locality(LocalitySearchDTO.fromLocality(locality))
                .localityDistrict(LocalityDistrictSearchDTO.fromLocalityDistrict(localityDistrict))
                .build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(LocalityPart localityPart) {
        Optional<LocalityDistrict> localityDistrict = localityPart.getLocalityDistrict();
        Locality locality = localityPart.getParentLocality();
        Commune commune = locality.getCommune();
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        LocationSearchDTO.LocationSearchDTOBuilder builder = LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .locality(LocalitySearchDTO.fromLocality(locality))
                .localityPart(LocalityPartSearchDTO.fromLocalityPart(localityPart));
        localityDistrict.ifPresent(locDistrict -> builder.localityDistrict(LocalityDistrictSearchDTO.fromLocalityDistrict(locDistrict)));
        return builder.build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(LocalityPart localityPart, Street street) {
        Optional<LocalityDistrict> localityDistrict = localityPart.getLocalityDistrict();
        Locality locality = localityPart.getParentLocality();
        Commune commune = locality.getCommune();
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        LocationSearchDTO.LocationSearchDTOBuilder builder = LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .locality(LocalitySearchDTO.fromLocality(locality))
                .localityPart(LocalityPartSearchDTO.fromLocalityPart(localityPart))
                .street(StreetSearchDTO.fromStreet(street));
        localityDistrict.ifPresent(locDistrict -> builder.localityDistrict(LocalityDistrictSearchDTO.fromLocalityDistrict(locDistrict)));
        return builder.build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(LocalityDistrict localityDistrict, Street street) {
        Locality locality = localityDistrict.getParentLocality();
        Commune commune = locality.getCommune();
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .locality(LocalitySearchDTO.fromLocality(locality))
                .localityDistrict(LocalityDistrictSearchDTO.fromLocalityDistrict(localityDistrict))
                .street(StreetSearchDTO.fromStreet(street))
                .build();
    }

    public LocationSearchDTO mapToLocationSearchDTO(Locality locality, Street street) {
        Commune commune = locality.getCommune();
        District district = commune.getDistrict();
        Voivodeship voivodeship = district.getVoivodeship();
        return LocationSearchDTO.builder()
                .voivodeship(VoivodeshipSearchDTO.fromVoivodeship(voivodeship))
                .district(DistrictSearchDTO.fromDistrict(district))
                .commune(CommuneSearchDTO.formCommune(commune))
                .locality(LocalitySearchDTO.fromLocality(locality))
                .street(StreetSearchDTO.fromStreet(street))
                .build();
    }

}
