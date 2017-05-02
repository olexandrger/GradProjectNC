package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
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
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Category complainReason;
}
