package com.example.bishop;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsuranceInfo {
    @SerializedName("term")
    @Expose
    private Term term;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("id")
    @Expose
    private String id;

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
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

    public class Term {

        @SerializedName("state")
        @Expose
        private Boolean state;
        @SerializedName("pricePerYear")
        @Expose
        private Integer pricePerYear;
        @SerializedName("percentage")
        @Expose
        private Double percentage;
        @SerializedName("maxRefund")
        @Expose
        private Integer maxRefund;

        public Boolean getState() {
            return state;
        }

        public void setState(Boolean state) {
            this.state = state;
        }

        public Integer getPricePerYear() {
            return pricePerYear;
        }

        public void setPricePerYear(Integer pricePerYear) {
            this.pricePerYear = pricePerYear;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public Integer getMaxRefund() {
            return maxRefund;
        }

        public void setMaxRefund(Integer maxRefund) {
            this.maxRefund = maxRefund;
        }

    }
}
