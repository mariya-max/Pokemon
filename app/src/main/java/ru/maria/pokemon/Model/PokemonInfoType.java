package ru.maria.pokemon.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokemonInfoType {
    @SerializedName("slot")
    @Expose
    private String slot;

    @SerializedName("type")
    @Expose
    private Type type;

    public String getType() {
        return type.getName();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public class Type{
        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
