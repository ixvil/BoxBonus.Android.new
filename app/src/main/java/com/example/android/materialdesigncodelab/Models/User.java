package com.example.android.materialdesigncodelab.Models;

import com.google.gson.JsonObject;

import java.math.BigInteger;

/**
 * Created by ixvil on 04.12.2016.
 */

public class User {
    static public User user;
    static public int userId;

    public User(int id, String name, int balance, int walletId, String email, BigInteger phone) {
        this.setId(id);
        this.setName(name);
        this.setBalance(balance);
        this.setWalletId(walletId);
        this.setEmail(email);
        this.setPhone(phone);
        Wallet.setWalletId(walletId);
    }


    private String name;
    private int id;
    private int balance;
    private int walletId;
    private BigInteger phone;
    private String email;


    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * @return int
     */
    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    static public boolean proceedDeAuth() {
        User.user = null;
        User.userId = 0;
        return true;
    }

    static public boolean proceedAuth(User user) {
        User.user = user;
        User.userId = user.getId();
        return true;
    }

    static public String deformatPhone(String goodPhone) {
        return goodPhone.toString()
                .replace("+", "")
                .replace(" ", "")
                .replace("-", "");
    }

    static public User createUserFromJson(JsonObject jsonObject) {

        JsonObject jOAttributes = (JsonObject) jsonObject.get("attributes");
        String name = jOAttributes.get("name").getAsString();
        String email = jOAttributes.get("email").getAsString();
        BigInteger phone = jOAttributes.get("phone").getAsBigInteger();

        JsonObject jOCustomer = (JsonObject) jOAttributes.get("customer");


        int balance = jOCustomer.get("balance").getAsInt();
        int walletId = jOCustomer.get("walletId").getAsInt();
        int id = jsonObject.get("id").getAsInt();

        User user = new User(id, name, balance, walletId, email, phone);

        return user;
    }
}
