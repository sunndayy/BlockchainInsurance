package com.example.insuranceadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tx {
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ref")
    @Expose
    private Ref ref;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public class ExpireTime {

        @SerializedName("timeStart")
        @Expose
        private String timeStart;
        @SerializedName("timeEnd")
        @Expose
        private String timeEnd;

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
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
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("birthday")
        @Expose
        private String birthday;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
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
