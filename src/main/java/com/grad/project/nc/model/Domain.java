package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class Domain {
    private Long domaiId;
    private String domainName;
    private Long addressId;
    private Long domainTypeId;

    public Long getDomaiId() {
        return domaiId;
    }

    public void setDomaiId(Long domaiId) {
        this.domaiId = domaiId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getDomainTypeId() {
        return domainTypeId;
    }

    public void setDomainTypeId(Long domainTypeId) {
        this.domainTypeId = domainTypeId;
    }
}
