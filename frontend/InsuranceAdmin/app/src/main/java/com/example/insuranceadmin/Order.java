package com.example.insuranceadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("expireTime")
    @Expose
    private ExpireTime expireTime;
    @SerializedName("garaPubKeyHashes")
    @Expose
    private List<String> garaPubKeyHashes = null;
    @SerializedName("refunds")
    @Expose
    private List<Refund> refunds = null;

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ExpireTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ExpireTime expireTime) {
        this.expireTime = expireTime;
    }

    public List<String> getGaraPubKeyHashes() {
        return garaPubKeyHashes;
    }

    public void setGaraPubKeyHashes(List<String> garaPubKeyHashes) {
        this.garaPubKeyHashes = garaPubKeyHashes;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    public class ExpireTime {

        @SerializedName("timeStart")
        @Expose
        private Long timeStart;
        @SerializedName("timeEnd")
        @Expose
        private Long timeEnd;

        public Long getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(Long timeStart) {
            this.timeStart = timeStart;
        }

        public Long getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(Long timeEnd) {
            this.timeEnd = timeEnd;
        }

    }

    public class Refund {

        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("refund")
        @Expose
        private Integer refund;
        @SerializedName("time")
        @Expose
        private Long time;
        @SerializedName("garaPubKeyHash")
        @Expose
        private String garaPubKeyHash;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getRefund() {
            return refund;
        }

        public void setRefund(Integer refund) {
            this.refund = refund;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getGaraPubKeyHash() {
            return garaPubKeyHash;
        }

        public void setGaraPubKeyHash(String garaPubKeyHash) {
            this.garaPubKeyHash = garaPubKeyHash;
        }

    }

    public class UserInfo {

        @SerializedName("identityCard")
        @Expose
        private String identityCard;
        @SerializedName("licensePlate")
        @Expose
        private String licensePlate;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("birthday")
        @Expose
        private Long birthday;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("email")
        @Expose
        private String email;

        public String getIdentityCard() {
            return identityCard;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getBirthday() {
            return birthday;
        }

        public void setBirthday(Long birthday) {
            this.birthday = birthday;
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
