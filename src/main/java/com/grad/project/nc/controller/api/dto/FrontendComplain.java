package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Complain;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Created by DeniG on 16.05.2017.
 */
@Data
@Builder
public class FrontendComplain {
    private Long complainId;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userNumber;
    private Long productInstanceId;
    private String productInstanceName;
    private String complainTitle;
    private String content;
    private String status;
    private Long responsiblId;
    private String responsibleEmail;
    private String responsibleNumber;
    private String responsibleName;
    private String response;
    private OffsetDateTime openDate;
    private OffsetDateTime closeDate;
    private String complainReason;

    public static FrontendComplain fromEntity(Complain complain) {
        return builder()
                .complainId(complain.getComplainId())
                .userId(complain.getUser().getUserId())
                .userName(complain.getUser().getFirstName() + " " + complain.getUser().getLastName())
                .userEmail(complain.getUser().getEmail())
                .userNumber(complain.getUser().getPhoneNumber())
                .productInstanceId((complain.getProductInstance() == null) ?
                        null : complain.getProductInstance().getInstanceId())
                .productInstanceName((complain.getProductInstance() == null) ?
                        null : complain.getProductInstance().getPrice().getProduct().getProductName())
                .complainTitle(complain.getComplainTitle())
                .content(complain.getContent())
                .status(complain.getStatus().getCategoryName())
                .responsiblId((complain.getResponsible() == null ? null : complain.getResponsible().getUserId()))
                .responsibleEmail(complain.getResponsible() == null ? null : complain.getResponsible().getEmail())
                .responsibleNumber(complain.getResponsible() == null ? null : complain.getResponsible().getPhoneNumber())
                .responsibleName(complain.getResponsible() == null ?
                        null : complain.getResponsible().getFirstName()+" "+complain.getResponsible().getLastName())
                .response(complain.getResponse())
                .openDate(complain.getOpenDate())
                .closeDate(complain.getCloseDate())
                .complainReason(complain.getComplainReason().getCategoryName())
                .build();
    }
}
