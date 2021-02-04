package com.intellex.hometek;

import android.content.Context;
import android.content.SharedPreferences;

/** Created by Hometek on 7/21/2017. */
public class UserInfo {
    private Context context;
    private String address1;
    private String address2;
    private String address3;
    private String sipIp;
    private boolean isRegister = false;
    private boolean isFcmRegister = false;
    private String mType;
    private static UserInfo ourInstance;

    public static UserInfo getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserInfo(context);
        }
        return ourInstance;
    }

    private UserInfo(Context context) {
        this.context = context;
        SharedPreferences settings = context.getSharedPreferences("Preference", 0);
        this.sipIp = settings.getString("sip_ip", "192.168.0.1");
        this.address1 = settings.getString("address1", "");
        this.address2 = settings.getString("address2", "");
        this.address3 = settings.getString("address3", "");
        this.isRegister = settings.getBoolean("isRegister", false);
        this.isFcmRegister = settings.getBoolean("isFcmRegister", false);
    }

    public void save() {
        SharedPreferences settings = context.getSharedPreferences("Preference", 0);
        settings.edit().putString("sip_ip", sipIp).apply();
        settings.edit().putString("address1", address1).apply();
        settings.edit().putString("address2", address2).apply();
        settings.edit().putString("address3", address3).apply();
        settings.edit().putBoolean("isRegister", isRegister).apply();
        settings.edit().putBoolean("isFcmRegister", isFcmRegister).apply();
        settings.edit().putString("type", mType).apply();
    }

    public String getSipIp() {
        return this.sipIp;
    }

    public String getPath() {
        return String.format(
                "%04d%02d%02d",
                Integer.valueOf(address1), Integer.valueOf(address2), Integer.valueOf(address3));
    }

    public String getAddress1() {
        return this.address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public String getAddress3() {
        return this.address3;
    }

    public void setSipIp(String ip) {
        this.sipIp = ip;
    }

    public void setAddress1(String address) {
        this.address1 = address;
    }

    public void setAddress2(String address) {
        this.address2 = address;
    }

    public void setAddress3(String address) {
        this.address3 = address;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
