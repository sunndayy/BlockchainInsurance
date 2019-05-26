package com.example.policeadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("policeInfo")
    @Expose
    private PoliceInfo policeInfo;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PoliceInfo getPoliceInfo() {
        return policeInfo;
    }

    public void setPoliceInfo(PoliceInfo policeInfo) {
        this.policeInfo = policeInfo;
    }

    public class Birthday {

        @SerializedName("day")
        @Expose
        private Integer day;
        @SerializedName("month")
        @Expose
        private Integer month;
        @SerializedName("year")
        @Expose
        private Integer year;

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

    }

    public class PoliceInfo {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("licensePlate")
        @Expose
        private String licensePlate;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

    }

    public class Product {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("producer")
        @Expose
        private String producer;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getProducer() {
            return producer;
        }

        public void setProducer(String producer) {
            this.producer = producer;
        }

    }

    public class User {

        @SerializedName("birthday")
        @Expose
        private Birthday birthday;
        @SerializedName("identityCard")
        @Expose
        private String identityCard;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("email")
        @Expose
        private String email;

        public Birthday getBirthday() {
            return birthday;
        }

        public void setBirthday(Birthday birthday) {
            this.birthday = birthday;
        }

        public String getIdentityCard() {
            return identityCard;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

}