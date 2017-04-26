package com.grad.project.nc.model;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
public class DiscountPrice {
    private Long discountId;
    private Long priceId;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }
}
