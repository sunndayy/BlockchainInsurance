package com.example.insuranceadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsurancePost {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ref")
    @Expose
    private Ref ref;
    @SerializedName("action")
    @Expose
    private Action action;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public InsurancePost() {
        this.type = "";
        this.ref = new Ref();
        this.action = new Action();
    }

    public class Action {

        @SerializedName("create")
        @Expose
        private Create create;

        public Create getCreate() {
            return create;
        }

        public void setCreate(Create create) {
            this.create = create;
        }

        public Action() {
            this.create = new Create();
        }

    }


    public class Create {

        @SerializedName("term")
        @Expose
        private Term term;

        public Term getTerm() {
            return term;
        }

        public void setTerm(Term term) {
            this.term = term;
        }

        public Create() {
            this.term = new Term();
        }

    }

    public class Ref {

        @SerializedName("company")
        @Expose
        private String company;
        @SerializedName("id")
        @Expose
        private String id;

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

        public Ref() {
            this.company = "";
            this.id = "";
        }

    }

    public class Term {

        @SerializedName("pricePerYear")
        @Expose
        private Integer pricePerYear;
        @SerializedName("percentage")
        @Expose
        private Double percentage;
        @SerializedName("maxRefund")
        @Expose
        private Integer maxRefund;
        @SerializedName("state")
        @Expose
        private Boolean state;

        public Term() {
            this.pricePerYear = 0;
            this.percentage = 0.0;
            this.maxRefund = 0;
            this.state = true;
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

        public Boolean getState() {
            return state;
        }

        public void setState(Boolean state) {
            this.state = state;
        }

    }
}
