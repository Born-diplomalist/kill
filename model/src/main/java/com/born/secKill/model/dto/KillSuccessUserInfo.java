package com.born.secKill.model.dto;/**
 * Created by Administrator on 2020/3/21.
 */

import com.born.secKill.model.entity.OrderInfo;

import java.io.Serializable;

/**
 *
 * 数据访问对象--秒杀成功消息的封装
 *
 *
 * 该对象包含的属性大部分来源于ItemKillSuccess这个实体类，因此直接继承它就避免再重复定义属性
 * @Author: born
 * @Date: 2020/3/21 22:02
 **/
public class KillSuccessUserInfo extends OrderInfo implements Serializable{

    private String userName;

    private String phone;

    private String email;

    private String itemName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public KillSuccessUserInfo() {
    }

    public KillSuccessUserInfo(String userName, String phone, String email, String itemName) {
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return super.toString()+"\nKillSuccessUserInfo{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KillSuccessUserInfo that = (KillSuccessUserInfo) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return itemName != null ? itemName.equals(that.itemName) : that.itemName == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        return result;
    }
}