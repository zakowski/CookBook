package com.cookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cookapp.Common.Common;
import com.cookapp.Database.Database;
import com.cookapp.Interface.IClickListener;
import com.cookapp.Model.Category;
import com.cookapp.Model.Favorites;
import com.cookapp.Model.Foods;
import com.cookapp.ViewHder.FoodsVHder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FoodsList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId="";
    FirebaseRecyclerAdapter<Foods, FoodsVHder>adapter;

    //Ulubione
    Database localDB;

    //Swipe layout
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);

        //Dostęp do bazy
        database =FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        //LocalDB - Ulubione
        localDB = new Database(this);

        recyclerView = findViewById(R.id.recyclerFoods);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        //Swipe layout
        swipeRefreshLayout = findViewById(R.id.swipeLayoutFoods);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        //Domyslny widok podczas pierwszego ładowania
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Intent
                if(getIntent() != null) {
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if(!categoryId.isEmpty()){
                    if (Common.connectToInternet(getBaseContext())) {
                        loadFoods(categoryId);
                    }
                    else{
                        Toast.makeText(getBaseContext(),"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Intent
                if(getIntent() != null) {
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if(!categoryId.isEmpty()){
                    if (Common.connectToInternet(getBaseContext())) {
                        loadFoods(categoryId);
                    }
                    else{
                        Toast.makeText(getBaseContext(),"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


//        //Intent
//        if(getIntent() != null) {
//            categoryId = getIntent().getStringExtra("CategoryId");
//        }
//        if(!categoryId.isEmpty()){
//            if (Common.connectToInternet(FoodsList.this)) {
//                loadFoods(categoryId);
//            }
//            else{
//                Toast.makeText(FoodsList.this,"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    private void loadFoods(String categoryId) {
        Query foods = foodList.orderByChild("MenuId").equalTo(categoryId);  //foodList to query sql do firebase - np. database/Foods/MenuId/01
        FirebaseRecyclerOptions<Foods> options = new FirebaseRecyclerOptions.Builder<Foods>()
                .setQuery(foods, Foods.class)//select all from foods where..
                .build();

        adapter = new FirebaseRecyclerAdapter<Foods, FoodsVHder>(options) {
            @NonNull
            @Override
            public FoodsVHder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.foods_item,parent,false);
                return new FoodsVHder(item);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FoodsVHder foodsVHder, final int i, @NonNull final Foods foods) {
                //Nazwa przekazana do widoku Viewholder
                foodsVHder.FoodsName.setText(foods.getName());
                //Wczytuje obrazy do widoku
                Picasso.with(getBaseContext()).load(foods.getImage())
                        .into(foodsVHder.ImageFoods);

                //Dodane do Ulubionych
                if(localDB.isFavorite(adapter.getRef(i).getKey(),Common.currentUser.getNrTel())){
                    foodsVHder.ImageFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                }

                //Zmiana stanu Ulubionych
                foodsVHder.ImageFav.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {    //metoda
                        if(!localDB.isFavorite(adapter.getRef(i).getKey(),Common.currentUser.getNrTel())){

                            new Database(getBaseContext()).addToFavorite(new Favorites(
                                    adapter.getRef(i).getKey(),
                                    Common.currentUser.getNrTel(),
                                    foods.getName(),//here
                                    foods.getCalories(),
                                    foods.getTime()
                            ));
                            //localDB.addFavorite(adapter.getRef(i).getKey(),Common.currentUser.getNrTel());
                            foodsVHder.ImageFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodsList.this, ""+foods.getName()+" dodano do ulubionych",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            localDB.removeFavorite(adapter.getRef(i).getKey(),Common.currentUser.getNrTel());
                            foodsVHder.ImageFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(FoodsList.this, ""+foods.getName()+" usunięto z ulubionych",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                //final Foods clickItem = foods;
                foodsVHder.setItemClickListener(new IClickListener() {
                    @Override
                    public void onClick(View view, int pos, boolean isClick) {
                        //Startujemy z activity FoodsDetail
                        Intent intentDetail = new Intent(FoodsList.this,FoodsDetail.class);
                        intentDetail.putExtra("FoodsId",adapter.getRef(pos).getKey());
                        startActivity(intentDetail);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
