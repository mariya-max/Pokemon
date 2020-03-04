package ru.maria.pokemon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maria.pokemon.Model.Data;
import ru.maria.pokemon.Model.PokemonInfo;
import ru.maria.pokemon.Model.PokemonInfoStats;
import ru.maria.pokemon.Model.PokemonPreview;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<PokemonPreview> dataList;
    List<PokemonPreview> tmpList;
    List<PokemonInfo> pokemonInfoList;
    Data data;
    DataAdapter adapter;
    Button initialization;
    CheckBox attack;
    CheckBox defense;
    CheckBox hp;
    int offset = 0;
    int maxCount = 964;
    int countCall = 0;

    boolean attackBoolean = false;
    boolean defenseBoolean = false;
    boolean hpBoolean = false;

    String BASE_URL_IMAGE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    String BASE_URL_IMAGE_TYPE = ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = new ArrayList<>();


        recyclerView = findViewById(R.id.rv);
        initialization = findViewById(R.id.initialization);
        attack = findViewById(R.id.attack);
        defense = findViewById(R.id.defense);
        hp = findViewById(R.id.hp);

        initialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.clear();
                getRandomNumber();
                getRandomPokemons();
            }
        });

        attack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    attackBoolean = true;
                } else {
                    attackBoolean = false;
                }
                init();
            }
        });

        defense.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    defenseBoolean = true;
                } else {
                    defenseBoolean = false;
                }
                init();
            }
        });

        hp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    hpBoolean = true;
                } else {
                    hpBoolean = false;
                }
                init();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new DataAdapter(recyclerView, dataList, getVision());
        recyclerView.setAdapter(adapter);
        getNewPokemons();
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                showScrollBar();
                getNewPokemons();
            }
        });
    }

    public void showScrollBar() {
        PokemonPreview pokemonPreview = new PokemonPreview("Идет загрузка...");
        dataList.add(pokemonPreview);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void hideScrollBar() {
        if (dataList.size() == 0) return;
        dataList.remove(dataList.size() - 1);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public String getIdPokemon(String urlPokemon) {
        String id = "";
        List<String> stringArray = Arrays.asList(urlPokemon.split("/"));
        id = stringArray.get(stringArray.size() - 1);
        return id;
    }

    private void getNewPokemons() {

        App.getApi().getPokemonList(30, offset).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                hideScrollBar();
                if (response.body() == null) {
                    return;
                }
                data = response.body();
                for (PokemonPreview pok : data.getPokemonPreviews()) {
                    getIdPokemon(pok.getUrl());
                    pok.setUrlImg(BASE_URL_IMAGE + getIdPokemon(pok.getUrl()) + BASE_URL_IMAGE_TYPE);
                    pok.setId(getIdPokemon(pok.getUrl()));
                    dataList.add(pok);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                adapter.setLoaded();
                offset = offset + 30;
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                hideScrollBar();
                adapter.setLoaded();
            }
        });

        adapter.setOnItemClickListener(new DataAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PokemonPreview pokemon = dataList.get(position);
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                intent.putExtra("id", pokemon.getId());
                intent.putExtra("name", dataList.get(position).getName());
                intent.putExtra("image", pokemon.getUrlImg());
                startActivity(intent);
            }
        });
    }

    private void getRandomPokemons() {

        App.getApi().getPokemonList(30, offset).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                hideScrollBar();
                if (response.body() == null) {
                    return;
                }
                data = response.body();
                for (PokemonPreview pok : data.getPokemonPreviews()) {
                    getIdPokemon(pok.getUrl());
                    pok.setUrlImg(BASE_URL_IMAGE + getIdPokemon(pok.getUrl()) + BASE_URL_IMAGE_TYPE);
                    pok.setId(getIdPokemon(pok.getUrl()));
                    dataList.add(pok);
                }
                adapter = new DataAdapter(recyclerView, dataList, getVision());
                recyclerView.setAdapter(adapter);
                adapter.setLoadMore(new ILoadMore() {
                    @Override
                    public void onLoadMore() {
                        showScrollBar();
                        getNewPokemons();
                    }
                });
                adapter.setLoaded();
                offset = offset + 30;
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                hideScrollBar();
                adapter.setLoaded();
            }
        });

        adapter.setOnItemClickListener(new DataAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PokemonPreview pokemon = dataList.get(position);
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                intent.putExtra("id", pokemon.getId());
                intent.putExtra("name", dataList.get(position).getName());
                intent.putExtra("image", pokemon.getUrlImg());
                startActivity(intent);
            }
        });
    }


    public boolean getVision() {
        if (attackBoolean || defenseBoolean || hpBoolean) {
            return true;
        }
        return false;
    }

    public void init() {
        if (!getVision()) {
            sortList();
            adapter = new DataAdapter(recyclerView, dataList, getVision());
            recyclerView.setAdapter(adapter);
            adapter.setLoadMore(new ILoadMore() {
                @Override
                public void onLoadMore() {
                    showScrollBar();
                    getNewPokemons();
                }
            });
            return;
        }
        pokemonInfoList = new ArrayList<>();
        countCall = 0;
        for (PokemonPreview elem : dataList) {
            App.getApi().getPokemon(elem.getId()).enqueue(new Callback<PokemonInfo>() {
                @Override
                public void onResponse(Call<PokemonInfo> call, Response<PokemonInfo> response) {
                    countCall++;

                    if (response.body() == null) return;

                    PokemonInfo pokemonInfo = response.body();

                    pokemonInfoList.add(pokemonInfo);
                    if (countCall == dataList.size()) {
                        if (attackBoolean) {
                            String id = getMaxAttack(pokemonInfoList);
                            showPokemon(id);
                        }
                        if (attackBoolean && defenseBoolean) {
                            String id = getMaxAttackAndMaxDefense(pokemonInfoList);
                            showPokemon(id);
                            return;
                        }
                        if (attackBoolean && hpBoolean) {
                            String id = getMaxAttackAndMaxHp(pokemonInfoList);
                            showPokemon(id);
                            return;
                        }
                        if (defenseBoolean) {
                            String id = getMaxDefense(pokemonInfoList);
                            showPokemon(id);
                            return;
                        }
                        if (hpBoolean) {
                            String id = getMaxHp(pokemonInfoList);
                            showPokemon(id);
                            return;
                        }
                    }
                }

                @Override
                public void onFailure(Call<PokemonInfo> call, Throwable t) {
                    countCall++;
                    t.printStackTrace();
                    if (countCall == dataList.size()) {
                        String id = getMaxAttack(pokemonInfoList);
                        showPokemon(id);
                    }
                }
            });
        }
        System.out.println();

    }

    public void showPokemon(String id) {
        Map<String, PokemonPreview> linkedHashMap = new LinkedHashMap<String, PokemonPreview>();
        for (PokemonPreview pok : dataList) {
            if (id.equalsIgnoreCase(pok.getId())) {
                linkedHashMap.put(pok.getId(), pok);
            }
        }
        for (PokemonPreview pok : dataList) {
            linkedHashMap.put(pok.getId(), pok);
        }
        dataList = new ArrayList<>();
        Iterator<PokemonPreview> itr = linkedHashMap.values().iterator();
        while (itr.hasNext())
            dataList.add(itr.next());
        adapter = new DataAdapter(recyclerView, dataList, getVision());
        recyclerView.setAdapter(adapter);
        showScrollBar();
    }

    //сортировка по id
    //TODO переписать на линкедсортедлист
    public void sortList() {
        List<PokemonPreview> arr = dataList;
    /*Внешний цикл каждый раз сокращает фрагмент массива,
      так как внутренний цикл каждый раз ставит в конец
      фрагмента максимальный элемент*/
        for (int i = arr.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr.get(j).getId() == null || arr.get(j + 1).getId() == null) continue;
                int thisElement = Integer.parseInt(arr.get(j).getId());
                int nextElement = Integer.parseInt(arr.get(j + 1).getId());
                if (thisElement > nextElement) {
                    PokemonPreview tmp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, tmp);
                }
            }
        }
        dataList = arr;
    }

    public String getMaxAttackAndMaxDefense(List<PokemonInfo> pokemonInfoList) {
        int maxAttack = 0;
        int maxDefense = 0;

        for (PokemonInfo pokemon : pokemonInfoList) {
            int thisAttack = Integer.parseInt(getAttack(pokemon));
            if (thisAttack > maxAttack) {
                maxAttack = thisAttack;
            }
            int thisDef = Integer.parseInt(getDefense(pokemon));
            if (thisDef > maxDefense) {
                maxDefense = thisDef;
            }
        }

        if (maxAttack > maxDefense) {
            return getMaxAttack(pokemonInfoList);
        } else {
            return getMaxDefense(pokemonInfoList);
        }
    }

    public String getMaxAttackAndMaxHp(List<PokemonInfo> pokemonInfoList) {
        //String id = "0";
        int maxAttack = 0;
        int maxHp = 0;

        for (PokemonInfo pokemon : pokemonInfoList) {
            int thisAttack = Integer.parseInt(getAttack(pokemon));
            if (thisAttack > maxAttack) {
                maxAttack = thisAttack;
                //id = pokemon.getId();
            }
            int thisHp = Integer.parseInt(getHp(pokemon));
            if (thisHp > maxHp) {
                maxHp = thisHp;
                //id = pokemon.getId();
            }
        }

        if (maxAttack > maxHp) {
            return getMaxAttack(pokemonInfoList);
        } else {
            return getMaxHp(pokemonInfoList);
        }
    }


    public String getMaxAttack(List<PokemonInfo> pokemonInfoList) {
        String id = "0";
        int maxAttack = 0;

        for (PokemonInfo pokemon : pokemonInfoList) {
            int thisAttack = Integer.parseInt(getAttack(pokemon));
            if (thisAttack > maxAttack) {
                maxAttack = thisAttack;
                id = pokemon.getId();
            }
        }
        return id;
    }

    public String getAttack(PokemonInfo pokemon) {
        ArrayList<PokemonInfoStats> pokemonStats = pokemon.getStats();
        for (PokemonInfoStats stats : pokemonStats) {
            if (stats.getStat().getName().equalsIgnoreCase("attack")) {
                return stats.getBase_stat();
            }
        }
        return "0";
    }

    public String getMaxDefense(List<PokemonInfo> pokemonInfoList) {
        String id = "0";
        int maxDefense = 0;

        for (PokemonInfo pokemon : pokemonInfoList) {
            int thisDefense = Integer.parseInt(getDefense(pokemon));
            if (thisDefense > maxDefense) {
                maxDefense = thisDefense;
                id = pokemon.getId();
            }
        }
        return id;
    }

    public String getDefense(PokemonInfo pokemon) {
        ArrayList<PokemonInfoStats> pokemonStats = pokemon.getStats();
        for (PokemonInfoStats stats : pokemonStats) {
            if (stats.getStat().getName().equalsIgnoreCase("defense")) {
                return stats.getBase_stat();
            }
        }
        return "0";
    }

    public String getHp(PokemonInfo pokemon) {
        ArrayList<PokemonInfoStats> pokemonStats = pokemon.getStats();
        for (PokemonInfoStats stats : pokemonStats) {
            if (stats.getStat().getName().equalsIgnoreCase("hp")) {
                return stats.getBase_stat();
            }
        }
        return "0";
    }

    public String getMaxHp(List<PokemonInfo> pokemonInfoList) {
        String id = "0";
        int maxHp = 0;

        for (PokemonInfo pokemon : pokemonInfoList) {
            int thisHp = Integer.parseInt(getHp(pokemon));
            if (thisHp > maxHp) {
                maxHp = thisHp;
                id = pokemon.getId();
            }
        }
        return id;
    }

    public void getRandomNumber() {
        Random r = new Random();
        offset = r.nextInt(maxCount + 1);
        System.out.println();
    }
}

