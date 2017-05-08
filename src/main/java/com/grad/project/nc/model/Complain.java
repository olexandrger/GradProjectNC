package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Complain {
    private Long complainId;
    private User user;
    private ProductInstance productInstance;
    private String complainTitle;
    private String content;
    private Category status;
    private User responsible;
    private String response;
    private OffsetDateTime openDate;
    private OffsetDateTime closeDate;
    private Category complainReason;
}
