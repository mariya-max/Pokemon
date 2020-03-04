
package ru.maria.pokemon.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PokemonPreview implements Comparable<PokemonPreview> {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;

    private String urlImg;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PokemonPreview(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    @Override
    public int compareTo(PokemonPreview pokemonPreview) {
        int i = Integer.parseInt(this.id);
        int j = Integer.parseInt(pokemonPreview.getId());
        return (int) (i - j);
    }
}
