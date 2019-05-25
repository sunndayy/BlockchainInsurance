package com.example.garaadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Item() {
        this.status = true;
        this.id = 100000;
        this.user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public class User {

        @SerializedName("birthday")
        @Expose
        private Birthday birthday;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("identityCard")
        @Expose
        private String identityCard;
        @SerializedName("sex")
        @Expose
        private Boolean sex;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("email")
        @Expose
        private String email;

        public User() {
            this.name = "";
            this.identityCard = "";
            this.sex = true;
            this.address = "";
            this.phoneNumber = "";
            this.email = "";
            this.birthday = new Birthday();
        }

        public Birthday getBirthday() {
            return birthday;
        }

        public void setBirthday(Birthday birthday) {
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdentityCard() {
            return identityCard;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public Boolean getSex() {
            return sex;
        }

        public void setSex(Boolean sex) {
            this.sex = sex;
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

        public Birthday() {
            this.day = 0;
            this.month = 0;
            this.year = 0;
        }

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
}
