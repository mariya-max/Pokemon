package ru.maria.pokemon.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokemonInfo {
    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("types")
    @Expose
    private ArrayList<PokemonInfoType> types;

    @SerializedName("stats")
    @Expose
    private ArrayList<PokemonInfoStats> stats;

    public ArrayList<PokemonInfoStats> getStats() {
        return stats;
    }

    public void setStats(ArrayList<PokemonInfoStats> stats) {
        this.stats = stats;
    }

    public String getTypes() {
        String typeList = "";
        for (PokemonInfoType pokemonInfoType : types) {
            if (typeList.length() == 0)
                typeList = pokemonInfoType.getType();
            else {
                typeList = typeList + ", " + pokemonInfoType.getType();
            }
        }
        return typeList;
    }

    public void setTypes(ArrayList<PokemonInfoType> types) {
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
