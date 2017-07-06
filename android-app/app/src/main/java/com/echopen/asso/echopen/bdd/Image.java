package com.echopen.asso.echopen.bdd;

public class Image {
    private Long id = null;
    private String imgName = null;
    private String settings = null;
    private String note = null;

    public Image() {
        super();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public String getImgName() {
        return imgName;
    }

    public String getSettings() {
        return settings;
    }

    public String getNote() {
        return note;
    }
}
