package com.cookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookapp.Common.Common;
import com.cookapp.Database.Database;
import com.cookapp.Model.Favorites;
import com.cookapp.Model.Foods;
import com.cookapp.Model.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodsDetail extends AppCompatActivity {
    //Declaration functionality
    TextView FoodsName, FoodsDescription, FoodsCalories, FoodsTime;
    ImageView ImageFoods;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnFav;

    String foodsId="";

    DatabaseReference foods;
    FirebaseDatabase database;

    //Ulubione
    Database localDB;
    Foods currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //LocalDB - Ulubione
        localDB = new Database(this);

        //Init view, backend
        btnFav = findViewById(R.id.btnFav);
        foodsId = getIntent().getStringExtra("FoodsId");

        if(localDB.isFavorite(foodsId,Common.currentUser.getNrTel())){
            btnFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!localDB.isFavorite(foodsId,Common.currentUser.getNrTel())){

                new Database(getBaseContext()).addToFavorite(new Favorites(
                        foodsId,
                        Common.currentUser.getNrTel(),
                        currentFood.getName(),//here
                        currentFood.getCalories(),
                        currentFood.getTime()
                ));
                    btnFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Toast.makeText(FoodsDetail.this,""+currentFood.getName()+" dodano do ulubionych",Toast.LENGTH_SHORT).show();
                }
                else{
                    localDB.removeFavorite(foodsId,Common.currentUser.getNrTel());
                    btnFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(FoodsDetail.this, ""+currentFood.getName()+" usunięto z ulubionych",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FoodsDescription = findViewById(R.id.FoodsDescription);
        FoodsCalories= findViewById(R.id.FoodsCalories);
        FoodsName = findViewById(R.id.FoodsName);
        FoodsTime= findViewById(R.id.FoodsTime);
        ImageFoods =findViewById(R.id.ImageFoods);

        collapsingToolbarLayout =findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        //Wez FoodsId z Intent
        if(getIntent() !=null) {
            foodsId = getIntent().getStringExtra("FoodsId");
        }
        if(!foodsId.isEmpty()){
            if (Common.connectToInternet(getBaseContext())) {
                loadDetail(foodsId);
            }
            else{
                Toast.makeText(this,"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadDetail(String foodsId) {
        foods.child(foodsId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               currentFood = dataSnapshot.getValue(Foods.class);

                //Ustawiam obraz w widoku
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(ImageFoods);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                FoodsCalories.setText(currentFood.getCalories());
                FoodsName.setText(currentFood.getName());
                FoodsDescription.setText(currentFood.getDescription());
                FoodsTime.setText(currentFood.getTime());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
