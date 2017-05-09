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
    private Long responsibleId;
    private Long domainId;
    private Long productId;
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
                .responsibleId(item.getResponsible() == null ? null : item.getResponsible().getUserId())
                .openDate(item.getOpenDate())
                .closeDate(item.getCloseDate())
                .domainId(item.getProductInstance().getDomain().getDomainId())
                .productId(item.getProductInstance().getPrice().getProduct().getProductId())
                .build();
    }
}