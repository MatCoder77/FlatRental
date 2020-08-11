package com.flatrental.domain.user;

import com.flatrental.api.RegistrationFormDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.UserDTO;
import com.flatrental.api.UserStatisticsDTO;
import com.flatrental.domain.file.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.flatrental.infrastructure.utils.ResourcePaths.ID_PATH;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final FileMapper fileMapper;

    public UserDTO mapToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(mapToAvatarUrl(user.getAvatar()))
                .about(user.getAbout())
                .statistics(mapToUserStatisticsDTO(user.getUserStatistics()))
                .build();
    }

    public URI mapToAvatarUrl(String filename) {
        return Optional.ofNullable(filename)
                .map(fileMapper::mapToDownloadUri)
                .orElse(null);
    }

    private UserStatisticsDTO mapToUserStatisticsDTO(UserStatistics userStatistics) {
        if (userStatistics == null) {
            return null;
        }
        return UserStatisticsDTO.builder()
                .opinionsCounter(userStatistics.getOpinionsCounter())
                .rating(userStatistics.getRating())
                .build();
    }

    public User mapToUser(RegistrationFormDTO registrationFormDTO) {
        if (registrationFormDTO == null) {
            return null;
        }
        return User.builder()
                .name(registrationFormDTO.getName())
                .surname(registrationFormDTO.getSurname())
                .username(registrationFormDTO.getUsername())
                .password(registrationFormDTO.getPassword())
                .email(registrationFormDTO.getEmail())
                .phoneNumber(registrationFormDTO.getPhoneNumber())
                .build();
    }

    public ResourceDTO mapToResourceDTO(User user) {
        if (user == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(user.getId())
                .identifier(user.getUsername())
                .uri(getUserUri(user))
                .build();
    }

    private URI getUserUri(User user) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(UserController.MAIN_RESOURCE)
                .path(ID_PATH)
                .buildAndExpand(user.getId())
                .toUri();
    }

}
