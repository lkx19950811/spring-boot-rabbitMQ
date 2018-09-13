package com.lkx.rabbitdemo.pojo;

import org.springframework.messaging.handler.annotation.Payload;

import java.io.Serializable;

/**
 * @author lee Cather
 * @date 2018-09-11 09:49
 * desc :
 */
public class User implements Serializable{

    private String userName;
    private String password;
    private String fullName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
