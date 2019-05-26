package com.example.garaadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOrderInfo {
    @SerializedName("user")
    @Expose
    private User user;

    public PostOrderInfo() {
        this.user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("birthday")
        @Expose
        private Birthday birthday;
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

        public User(){
            this.name = "";
            this.birthday = new Birthday();
            this.identityCard = "";
            this.sex = true;
            this.address = "";
            this.phoneNumber = "";
            this.email = "";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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
            this.day = 1;
            this.month = 1;
            this.year = 1970;
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
