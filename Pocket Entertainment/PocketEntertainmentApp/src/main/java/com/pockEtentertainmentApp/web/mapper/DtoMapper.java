package com.pockEtentertainmentApp.web.mapper;

import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.web.dto.EditAccountRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EditAccountRequest mapToEditAccountRequest(User user) {
        return EditAccountRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
