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
    private Long productInstanceId;
    private String complainTitle;
    private String content;
    private Long statusId;
    private Long responsiblId;
    private String response;
    private OffsetDateTime openDate;
    private OffsetDateTime closeDate;
    private Long complainReasonId;

    public static FrontendComplain fromEntity(Complain complain){
        return builder()
                .complainId(complain.getComplainId())
                .userId(complain.getUser().getUserId())
                .productInstanceId(complain.getProductInstance().getInstanceId())
                .complainTitle(complain.getComplainTitle())
                .content(complain.getContent())
                .statusId(complain.getStatus().getCategoryId())
                .responsiblId((complain.getResponsible()==null ? null:complain.getResponsible().getUserId() ))
                .response(complain.getResponse())
                .openDate(complain.getOpenDate())
                .closeDate(complain.getCloseDate())
                .complainReasonId(complain.getComplainReason().getCategoryId())
                .build();
    }
}
