package ru.maria.pokemon;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maria.pokemon.Model.PokemonInfo;
import ru.maria.pokemon.Model.PokemonInfoStats;

public class NewActivity extends AppCompatActivity {

    public PokemonInfo pokemonInfo;
    public TextView nameTextView;
    public TextView heightTextView;
    public TextView weightTextView;
    public TextView typeTextView;
    public ImageView imageView;
    public ListView listView;
    public ArrayList list;
    public ArrayAdapter<String> adapter;


    String id;
    String name;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        nameTextView = findViewById(R.id.nameTextView);
        heightTextView = findViewById(R.id.heightTextView);
        weightTextView = findViewById(R.id.weightTextView);
        typeTextView = findViewById(R.id.typeTextView);
        imageView = findViewById(R.id.image);
        listView = findViewById(R.id.listView);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        imgUrl = getIntent().getStringExtra("image");

        Glide.with(this)
                .load(imgUrl)
                .into(imageView);


        App.getApi().getPokemon(id).enqueue(new Callback<PokemonInfo>() {
            @Override
            public void onResponse(Call<PokemonInfo> call, Response<PokemonInfo> response) {

                if (response.body() == null) return;

                pokemonInfo = response.body();
                //String typePokemon = pokemonInfo.getTypes();
                list=getStats(pokemonInfo.getStats());
                adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.item_list, list);
                listView.setAdapter(adapter);
                nameTextView.setText("Name: " + name);
                heightTextView.setText("height: " + pokemonInfo.getHeight());
                weightTextView.setText("weight: " + pokemonInfo.getWeight());
                typeTextView.setText("type: " + pokemonInfo.getTypes());
                adapter.notifyDataSetChanged();
                System.out.println(listView.getAdapter().getCount());
                System.out.println();
                Utility.setListViewHeightBasedOnChildren(listView);
            }


            @Override
            public void onFailure(Call<PokemonInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public ArrayList getStats(ArrayList<PokemonInfoStats> statList) {
        ArrayList listStats = new ArrayList();
        for (PokemonInfoStats stats : statList) {
            String paramPokemon = stats.getStat().getName() + " = " + stats.getBase_stat();
            listStats.add(paramPokemon);
        }
        return listStats;
    }
}
