package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class Domain {
    private int domaiId;
    private String domainName;
    private int addressId;

    public int getDomaiId() {
        return domaiId;
    }

    public void setDomaiId(int domaiId) {
        this.domaiId = domaiId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
