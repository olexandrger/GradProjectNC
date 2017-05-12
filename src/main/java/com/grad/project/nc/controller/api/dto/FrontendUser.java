package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.User;
import lombok.Builder;
import lombok.Data;

/**
 * Created by DeniG on 10.05.2017.
 */

@Data
@Builder
public class FrontendUser {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public static FrontendUser fromEntity(User user){
        return builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
