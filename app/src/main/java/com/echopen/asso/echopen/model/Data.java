package com.echopen.asso.echopen.model;

public class Data
{

    private String title;

    private String description;

    private int image;

    public Data(String title, String description, int image)
    {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {

        this.title = title;
    }

    public String getDesc()
    {

        return description;
    }


    public void setDesc(String description)
    {

        this.description = description;
    }


    public int getImage()
    {
        return image;
    }


    public void setImage(int image)
    {
        this.image = image;
    }

}
