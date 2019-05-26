package com.example.bishop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ContractInfo {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ref")
    @Expose
    private Ref ref;

    public ContractInfo() {
        this.type = "";
        this.ref = new Ref();
    }

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

    public class ExpireTime {

        @SerializedName("timeStart")
        @Expose
        private Long timeStart;
        @SerializedName("timeEnd")
        @Expose
        private Long timeEnd;

        public ExpireTime() {
            this.timeStart = 0L;
            this.timeEnd = 0L;
        }

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

    public class Plan {

        @SerializedName("company")
        @Expose
        private String company;
        @SerializedName("id")
        @Expose
        private String id;

        public Plan() {
            this.company = "";
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

    public class Ref {

        @SerializedName("plan")
        @Expose
        private Plan plan;
        @SerializedName("userInfo")
        @Expose
        private UserInfo userInfo;
        @SerializedName("garaPubKeyHashes")
        @Expose
        private List<String> garaPubKeyHashes = null;
        @SerializedName("expireTime")
        @Expose
        private ExpireTime expireTime;

        public Ref() {
            this.plan = new Plan();
            this.userInfo = new UserInfo();
            this.expireTime = new ExpireTime();
            this.garaPubKeyHashes = new ArrayList<>();
            this.garaPubKeyHashes.add("368a5b069220e0919d2481f07161c5625ee4167e0a886a9c5c01be81d7b7db12");
        }

        public Plan getPlan() {
            return plan;
        }

        public void setPlan(Plan plan) {
            this.plan = plan;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public List<String> getGaraPubKeyHashes() {
            return garaPubKeyHashes;
        }

        public void setGaraPubKeyHashes(List<String> garaPubKeyHashes) {
            this.garaPubKeyHashes = garaPubKeyHashes;
        }

        public ExpireTime getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(ExpireTime expireTime) {
            this.expireTime = expireTime;
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
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("email")
        @Expose
        private String email;

        public UserInfo() {
            this.identityCard = "";
            this.licensePlate = "";
            this.name = "";
            this.address = "";
            this.phoneNumber = "";
            this.email = "";
        }

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
