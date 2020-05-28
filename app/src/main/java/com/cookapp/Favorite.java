package com.cookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.cookapp.Common.Common;
import com.cookapp.Database.Database;
import com.cookapp.ViewHder.FavoritesAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.cookapp.Model.Favorites;

import java.util.ArrayList;
import java.util.List;

public class Favorite extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;

    TextView FoodName, FoodTime, FoodCalories;

    List<Favorites> list_fav = new ArrayList<>();
    FavoritesAdapter adapter;

    String UserNrTel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Firebase
        database = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.listFav);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FoodName = findViewById(R.id.FoodsName);
        FoodCalories = findViewById(R.id.FoodsCalories);
        FoodTime = findViewById(R.id.FoodsTime);



        loadFavorite();
    }

    private void loadFavorite() {
        UserNrTel = Common.currentUser.getNrTel();
        list_fav = new Database(this).getFavorites(UserNrTel);
        adapter= new FavoritesAdapter(list_fav,this);
        recyclerView.setAdapter(adapter);

    }
}
