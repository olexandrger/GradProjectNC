package com.grad.project.nc.model;

import java.time.LocalDateTime;


/**
 * Created by Alex on 4/24/2017.
 */
public class ProductOrder {
    private Long productOrderId;
    private Long productInstanceId;
    private Long userId;
    private Long categoryId;
    private Long statusID;
    private Long responsibleId;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;

    public Long getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(Long productOrderId) {
        this.productOrderId = productOrderId;
    }

    public Long getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(Long productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStatusId() {
        return statusID;
    }

    public void setStatusID(Long statusID) {
        this.statusID = statusID;
    }

    public Long getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Long responsibleId) {
        this.responsibleId = responsibleId;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }
}
