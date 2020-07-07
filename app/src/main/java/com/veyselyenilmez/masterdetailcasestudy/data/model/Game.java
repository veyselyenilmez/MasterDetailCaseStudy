
package com.veyselyenilmez.masterdetailcasestudy.data.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description_raw")
    @Expose
    private String description;
    @SerializedName("background_image")
    @Expose
    private String backgroundImage;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("released")
    @Expose
    private String released;


    public Game() {
    }

    /**
     * @param backgroundImage
     * @param rating
     * @param id
     * @param name
     */
    public Game(Integer id, String name, String backgroundImage, Double rating) {
        super();
        this.id = id;

        this.name = name;

        this.backgroundImage = backgroundImage;
        this.rating = rating;
    }


    public Game(String name, String description, String released, Double rating, String background_image) {
        super();
        this.name = name;
        this.description = description;
        this.released = released;
        this.rating = rating;
        this.backgroundImage = background_image;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
