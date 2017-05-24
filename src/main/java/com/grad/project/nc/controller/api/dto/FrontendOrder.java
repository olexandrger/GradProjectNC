package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductOrder;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
public class FrontendOrder {
    private Long productOrderId;
    private Long productInstanceId;
    private String userName;
    private String orderAim;
    private String status;
    private Long statusId;
    private Long responsibleId;
    private String responsibleEmail;
    private Long domainId;
    private String domain;
    private Long productId;
    private String productName;
    private OffsetDateTime openDate;
    private OffsetDateTime closeDate;
    private Map<Long, String> possibleDomains;
    private Map<Long, String> possibleProducts;

    public static FrontendOrder fromEntity(ProductOrder item) {
        return FrontendOrder.builder()
                .productOrderId(item.getProductOrderId())
                .productInstanceId(item.getProductInstance().getInstanceId())
                .userName(item.getUser().getFirstName() + " " + item.getUser().getLastName())
                .orderAim(item.getOrderAim().getCategoryName())
                .status(item.getStatus().getCategoryName())
                .statusId(item.getStatus().getCategoryId())
                .responsibleId(item.getResponsible() == null ? null : item.getResponsible().getUserId())
                .responsibleEmail(item.getResponsible() == null ? null : item.getResponsible().getEmail())
                .openDate(item.getOpenDate())
                .closeDate(item.getCloseDate())
                .domainId(item.getProductInstance().getDomain().getDomainId())
                .domain(item.getProductInstance().getDomain().getDomainName())
                .productId(item.getProductInstance().getPrice().getProduct().getProductId())
                .productName(item.getProductInstance().getPrice().getProduct().getProductName())
                .build();
    }
}