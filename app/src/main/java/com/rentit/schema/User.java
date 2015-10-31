package com.rentit.schema;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AKhare on 02-Jul-15.
 */
public class User {
    @SerializedName("name")private String mName;
    @SerializedName("number")private String mPhoneNumber;
    @SerializedName("address")private String mAddress;
    @SerializedName("password")private String password;
    @SerializedName("ownerId")private String mUserId;
    @SerializedName("mongoId")private String mMongoId;

    private User(UserBuilder builder){
        this.mName = builder.Name;
        this.mPhoneNumber = builder.Number;
        this.mAddress = builder.Address;
        password = builder.Password;
        mUserId =  builder.UserId;
        mMongoId = builder.MongoId;
    }

    public String getmName() {
        return mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmMongoId() {
        return mMongoId;
    }

    /**
     * Builder class for User/owner
     */
    public  class UserBuilder{
        private String Name;
        private String Number;
        private String Address;
        private String Password;
        private String UserId;
        private String MongoId;

        /********************Setters ******************/
        public UserBuilder setName(String name){
            Name = name;
            return this;
        }

        public UserBuilder setNumber(String num){
            Number = num;
            return this;
        }
        public UserBuilder setAddress(String address){
            Address = address;
            return this;
        }
        public UserBuilder setPassword(String pwd){
            Password = pwd;
            return this;
        }
        public UserBuilder setUserId(String uid){
            UserId = uid;
            return this;
        }

        @Override
        public String toString() {
            return "UserBuilder{" +
                    "Name='" + Name + '\'' +
                    ", Number='" + Number + '\'' +
                    ", Address='" + Address + '\'' +
                    ", Password='" + Password + '\'' +
                    ", UserId='" + UserId + '\'' +
                    ", MongoId='" + MongoId + '\'' +
                    '}';
        }

        public UserBuilder setMongoId(String mid){
            MongoId = mid;
            return this;
        }

        public User getNewUser(){
            return new User(this);
        }
    }
}
