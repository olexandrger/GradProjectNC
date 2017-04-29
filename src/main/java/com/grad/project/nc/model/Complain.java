package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class Complain {
    private Long complainId;
    private Long userId;
    private Long productInstanceId;
    private String complainTitle;
    private String content;
    private Long statusId;
    private Long responsibleId;
    private String response;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Long complainReasonId;
}
