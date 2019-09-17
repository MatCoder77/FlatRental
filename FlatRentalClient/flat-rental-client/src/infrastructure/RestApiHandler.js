import {ACCESS_TOKEN, API_BASE_URL} from "../Constants";
import {request, uploadFilePromise, uploadFileRequest} from "./Request";

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/check/username/" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/check/email/" + email,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/current",
        method: 'GET'
    });
}

export function getBuildingTypes() {
    return request({
        url: API_BASE_URL + "/buildingtype",
        method: 'GET'
    });
}

export function getBuildingMaterialTypes() {
    return request({
        url: API_BASE_URL + "/buildingmaterial",
        method: 'GET'
    });
}

export function getCookerTypes() {
    return request({
        url: API_BASE_URL + "/cookertype",
        method: 'GET'
    });
}

export function getKitchenTypes() {
    return request({
        url: API_BASE_URL + "/kitchentype",
        method: 'GET'
    });
}

export function getWindowTypes() {
    return request({
        url: API_BASE_URL + "/windowtype",
        method: 'GET'
    });
}

export function getParkingTypes() {
    return request({
        url: API_BASE_URL + "/parkingtype",
        method: 'GET'
    });
}

export function getHeatingTypes() {
    return request({
        url: API_BASE_URL + "/heatingtype",
        method: 'GET'
    });
}

export function getApartmentStateTypes() {
    return request({
        url: API_BASE_URL + "/apartmentstate",
        method: 'GET'
    });
}

export function getApartmentAmenitiesTypes() {
    return request({
        url: API_BASE_URL + "/apartmentamenity",
        method: 'GET'
    });
}

export function getPreferences() {
    return request({
        url: API_BASE_URL + "/preferences",
        method: 'GET'
    });
}

export function getVoivodeships() {
    return request({
        url: API_BASE_URL + "/voivodeship",
        method: 'GET'
    });
}

export function getDistricts(voivodeshipId) {
    return request({
        url: API_BASE_URL + "/district/" + voivodeshipId,
        method: 'GET'
    });
}

export function getCommunes(districtId) {
    return request({
        url: API_BASE_URL + "/commune/" + districtId,
        method: 'GET'
    });
}

export function getLocalities(communeId) {
    return request({
        url: API_BASE_URL + "/locality/" + communeId,
        method: 'GET'
    });
}

export function getLocalityDistricts(localityId) {
    return request({
        url: API_BASE_URL + "/localitydistrict/" + localityId,
        method: 'GET'
    });
}

export function getLocalityPartsForParentLocality(parentLocalityId) {
    return request({
        url: API_BASE_URL + "/locality-part/for-parent-locality/" + parentLocalityId,
        method: 'GET'
    });
}

export function getLocalityPartsForParentLocalityDistrict(parentLocalityDistrictId) {
    return request({
        url: API_BASE_URL + "/locality-part/for-parent-locality-district/" + parentLocalityDistrictId,
        method: 'GET'
    });
}

export function getStreets(parentLocalityId) {
    return request({
        url: API_BASE_URL + "/street/for-parent-locality/" + parentLocalityId,
        method: 'GET'
    });
}

export function getFurnishing(furnishingType) {
    return request({
        url: API_BASE_URL + "/furnishing" + (furnishingType ? "?type=" + furnishingType : ""),
        method: 'GET'
    });
}

export function getNeighborhoodItems() {
    return request({
        url: API_BASE_URL + "/neighbourhood",
        method: 'GET'
    });
}

export function uploadFile(uploadedFile) {
    let formData = new FormData();
    formData.append("file", uploadedFile);
    return uploadFileRequest({
        url: API_BASE_URL + "/file/upload",
        method: 'POST',
        body: formData
    });
}

export function getUploadFilePromise(uploadedFile) {
    let formData = new FormData();
    formData.append("file", uploadedFile);
    return uploadFilePromise({
        url: API_BASE_URL + "/file/upload",
        method: 'POST',
        body: formData
    });
}