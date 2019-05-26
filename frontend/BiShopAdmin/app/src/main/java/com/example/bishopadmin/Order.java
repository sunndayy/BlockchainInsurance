package com.example.bishopadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("policeInfo")
    @Expose
    private PoliceInfo policeInfo;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("id")
    @Expose
    private Integer id;

    public PoliceInfo getPoliceInfo() {
        return policeInfo;
    }

    public void setPoliceInfo(PoliceInfo policeInfo) {
        this.policeInfo = policeInfo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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

    public class Item {

        @SerializedName("price")
        @Expose
        private Long price;
        @SerializedName("product")
        @Expose
        private Product product;

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
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

        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("describe")
        @Expose
        private String describe;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("price")
        @Expose
        private Long price;
        @SerializedName("producer")
        @Expose
        private String producer;

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
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
        @SerializedName("username")
        @Expose
        private String username;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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
