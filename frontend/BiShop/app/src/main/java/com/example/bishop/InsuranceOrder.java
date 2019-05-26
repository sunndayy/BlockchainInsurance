package com.example.bishop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsuranceOrder {
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("expireTime")
    @Expose
    private ExpireTime expireTime;
    @SerializedName("plan")
    @Expose
    private Plan plan;

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

    public ExpireTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ExpireTime expireTime) {
        this.expireTime = expireTime;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public class ExpireTime {

        @SerializedName("timeEnd")
        @Expose
        private Long timeEnd;
        @SerializedName("timeStart")
        @Expose
        private Long timeStart;

        public Long getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(Long timeEnd) {
            this.timeEnd = timeEnd;
        }

        public Long getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(Long timeStart) {
            this.timeStart = timeStart;
        }

    }

    public class Plan {

        @SerializedName("term")
        @Expose
        private Term term;

        public Term getTerm() {
            return term;
        }

        public void setTerm(Term term) {
            this.term = term;
        }

    }

    public class Term {

        @SerializedName("state")
        @Expose
        private Boolean state;
        @SerializedName("pricePerYear")
        @Expose
        private Long pricePerYear;
        @SerializedName("percentage")
        @Expose
        private Double percentage;
        @SerializedName("maxRefund")
        @Expose
        private Long maxRefund;

        public Boolean getState() {
            return state;
        }

        public void setState(Boolean state) {
            this.state = state;
        }

        public Long getPricePerYear() {
            return pricePerYear;
        }

        public void setPricePerYear(Long pricePerYear) {
            this.pricePerYear = pricePerYear;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public Long getMaxRefund() {
            return maxRefund;
        }

        public void setMaxRefund(Long maxRefund) {
            this.maxRefund = maxRefund;
        }

    }
}
