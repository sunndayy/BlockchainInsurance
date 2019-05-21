package com.example.bishopadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractInfo {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("licensePlate")
    @Expose
    private String licensePlate;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("contractId")
    @Expose
    private String contractId;
    @SerializedName("duration")
    @Expose
    private Integer duration;

    public ContractInfo() {

    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
