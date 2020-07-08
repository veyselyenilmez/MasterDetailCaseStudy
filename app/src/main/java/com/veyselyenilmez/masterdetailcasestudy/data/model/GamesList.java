
package com.veyselyenilmez.masterdetailcasestudy.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GamesList implements Serializable
{

    @SerializedName("results")
    @Expose
    private List<Game> games = new ArrayList<Game>();

    public GamesList() {
    }

    /**
     *
     * @param games
     */
    public GamesList(List<Game> games) {
        super();
        this.games = games;
    }

    @Override
    public String toString() {
        return "GamesList{" +
                "Games=" + games +
                '}';
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }


}
