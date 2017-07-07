package com.echopen.asso.echopen.sqlite_database.models;


public class Person {
    private long id;
    private String type;
    private String sex;

    public Person() {
    }

    public Person(int id, String type, String sex) {
        this.id = id;
        this.type = type;
        this.sex = sex;
    }

    public Person( String type, String sex) {
        this.type = type;
        this.sex = sex;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getId() {

        return id;
    }

    public String getType() {
        return type;
    }

    public String getSex() {
        return sex;
    }
}

