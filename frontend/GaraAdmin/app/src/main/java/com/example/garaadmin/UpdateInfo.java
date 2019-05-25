package com.example.garaadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateInfo {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("licensePlate")
    @Expose
    private String licensePlate;
    @SerializedName("insurence")
    @Expose
    private Insurence insurence;
    @SerializedName("total")
    @Expose
    private Integer total;

    public UpdateInfo() {
        this.status = true;
        this.licensePlate = "";
        this.insurence = new Insurence();
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

    public Insurence getInsurence() {
        return insurence;
    }

    public void setInsurence(Insurence insurence) {
        this.insurence = insurence;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public class Insurence {

        @SerializedName("company")
        @Expose
        private String company;
        @SerializedName("id")
        @Expose
        private String id;

        public Insurence() {
            this.company = "phhoang";
            this.id = "";
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
