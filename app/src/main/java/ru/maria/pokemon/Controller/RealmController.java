package ru.maria.pokemon.Controller;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ru.maria.pokemon.Model.Pokemon;

public class RealmController {

    private Realm realm;

    //test

    public RealmController(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    public void removeAll() {

        realm.beginTransaction();

        RealmResults<Pokemon> pokemons = realm.where(Pokemon.class).findAll();
        pokemons.clear();

        realm.commitTransaction();
    }

    public void saveAll(List<Pokemon> pokemons) {

        realm.beginTransaction();

        realm.copyToRealm(pokemons);
        realm.commitTransaction();
    }

    public void getAll() {
        RealmResults<Pokemon> pokemons = realm.where(Pokemon.class).findAll();
        System.out.println(pokemons.toString());
    }

    public RealmResults<Pokemon> getPokemonsByName(String name) {

        RealmResults<Pokemon> pokemons = realm.where(Pokemon.class).equalTo("name", name).findAll();
        System.out.println(pokemons.toString());
        return pokemons;

    }
}
