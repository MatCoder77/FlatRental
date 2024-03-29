import {ACCESS_TOKEN, API_BASE_URL} from "./Constants";
import {request, uploadFileRequest} from "./Request";
import {unflatten} from "./DTOUtils";

const queryString = require('query-string');

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

export function createAnnouncement(announcementDTO) {
    return request({
        url: API_BASE_URL + "/announcements",
        method: 'POST',
        body: JSON.stringify(announcementDTO)
    })
}

export function getAnnouncement(announcementId) {
    return request({
        url: API_BASE_URL + "/announcements/" + announcementId,
        method: 'GET'
    })
}

export function updateAnnouncement(announcementId, announcementDTO) {
    return request({
        url: API_BASE_URL + "/announcements/" + announcementId,
        method: 'PUT',
        body: JSON.stringify(announcementDTO)
    })
}

export function downloadFile(filename) {
    return request({
        url: API_BASE_URL + "/file/download/" + filename,
        method: 'GET'
    })
}

export function getLocations(searchText) {
    return request({
        url: API_BASE_URL + "/locations/search" + (searchText ? "?searchText=" + searchText : "") ,
        method: 'GET'
    });
}

export function searchAnnouncementsByCriteria(searchCriteriaParam, pageNumber, pageSize, sorting) {
    console.log(API_BASE_URL + "/announcements/search?searchCriteria=" + searchCriteriaParam + "&page=" + pageNumber + "&size=" + pageSize + "&sort=" + sorting);
    return request({
        url: API_BASE_URL + "/announcements/search?searchCriteria=" + searchCriteriaParam + "&page=" + pageNumber + "&size=" + pageSize + "&sort=" + sorting,
        method: 'GET'
    });
}

export function getEncodedQueryParam(queryParam) {
    return encodeURIComponent(btoa(JSON.stringify(queryParam)));
}

export function getDecodedQueryParam(queryParam) {
    return JSON.parse(atob(decodeURIComponent(queryParam)));
}

export function createComment(commentDTO) {
    return request({
        url: API_BASE_URL + "/comments",
        method: 'POST',
        body: JSON.stringify(commentDTO)
    });
}

export function createCommentForProfile(commentDTO) {
    return request({
        url: API_BASE_URL + "/comments/for-profile",
        method: 'POST',
        body: JSON.stringify(commentDTO)
    });
}

export function getComments(announcementId) {
    return request({
        url: API_BASE_URL + "/comments/" + announcementId,
        method: 'GET'
    });
}

export function getCommentsForProfile(userId) {
    return request({
        url: API_BASE_URL + "/comments/for-profile/" + userId,
        method: 'GET'
    });
}

export function deleteComment(id) {
    return request({
        url: API_BASE_URL + "/comments/" + id,
        method: 'DELETE'
    });
}

export function getUserAnnouncements(userId) {
    return request({
        url: API_BASE_URL + "/announcements/user/" + userId,
        method: 'GET'
    });
}

export function addToFavourites(announcementId) {
    return request({
        url: API_BASE_URL + "/announcements/add-to-favourites/" + announcementId,
        method: 'POST'
    });
}

export function removeFromFavourites(announcementId) {
    return request({
        url: API_BASE_URL + "/announcements/remove-from-favourites/" + announcementId,
        method: 'DELETE'
    });
}

export function getFavourites() {
    return request({
        url: API_BASE_URL + "/announcements/favourites",
        method: 'GET'
    });
}

export function changeAvatar(filename) {
    return request({
        url: API_BASE_URL + "/user/set-avatar/" + filename,
        method: 'POST'
    });
}

export function changePassword(oldPassword, newPassword) {
    let dto = new Object({password: oldPassword, value: newPassword});
    return request({
        url: API_BASE_URL + "/auth/change-password" ,
        method: 'POST',
        body: JSON.stringify(dto)
    });
}

export function changeEmail(password, email) {
    let dto = new Object({password: password, value: email});
    return request({
        url: API_BASE_URL + "/auth/change-email" ,
        method: 'POST',
        body: JSON.stringify(dto)
    });
}

export function changePhoneNumber(phoneNumber) {
    let dto = new Object({value: phoneNumber});
    return request({
        url: API_BASE_URL + "/user/change-phone" ,
        method: 'POST',
        body: JSON.stringify(dto)
    });
}

export function changeAboutUser(description) {
    let dto = new Object({value: description});
    return request({
        url: API_BASE_URL + "/user/change-profile-description" ,
        method: 'POST',
        body: JSON.stringify(dto)
    });
}

export function getUser(id) {
    return request({
        url: API_BASE_URL + "/user/" + id,
        method: 'GET',
    })
}

export function changeAnnouncementState(id, objectState) {
    return request({
        url: API_BASE_URL + "/announcements/" + id + "/change-state?state=" + objectState,
        method: 'PUT',
    })
}
