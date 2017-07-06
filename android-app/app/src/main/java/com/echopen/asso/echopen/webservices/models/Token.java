package com.echopen.asso.echopen.webservices.models;

/**
 * Created by matthieu on 06/07/17.
 */

public class Token {

    private String name;
    private int client_id;
    private int num;

    public void setName(String name) {
        this.name = name;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getNum() {
        return num;
    }
}
