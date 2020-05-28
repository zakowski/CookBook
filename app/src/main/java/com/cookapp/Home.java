package com.cookapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cookapp.Common.Common;
import com.cookapp.Interface.IClickListener;
import com.cookapp.Model.Banner;
import com.cookapp.Model.Category;
import com.cookapp.ViewHder.MenuVHder;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

   // private AppBarConfiguration mAppBarConfiguration;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtNameFull;

    RecyclerView recycle_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter <Category,MenuVHder> adapter;

    //deklaracja swipe dla content_home layout
    SwipeRefreshLayout swipeRefreshLayout;

    //Banner
    HashMap<String,String> imageList;
    SliderLayout Banner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        //Polacz z baza
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //Widok swipe layout---------------------------------swipe menu
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        //Domyslny widok podczas pierwszego ładowania
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.connectToInternet(getBaseContext())) {
                    loadMenu();
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getBaseContext(),"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.connectToInternet(getBaseContext())) {
                    loadMenu();
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getBaseContext(),"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Ustaw imie dla uzytkownika
        View headView = navigationView.getHeaderView(0);
        txtNameFull = headView.findViewById(R.id.txtNameFull);
        txtNameFull.setText(Common.currentUser.getName());

        //Laduj menu kategorii
        recycle_menu = findViewById(R.id.recycleMenu);
        recycle_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycle_menu.setLayoutManager(layoutManager);


        //Ustawiam baner
        setupSlider();

        //test obrazka
//        ImageView imageView = findViewById(R.id.imgTest);
//        String url = "https://images.freeimages.com/images/large-previews/723/tomates-soup-1329671.jpg";
//        Picasso.with(this).load(url).into(imageView);

    }

    private void setupSlider() {
        Banner = findViewById(R.id.slider);
        imageList = new HashMap<>();

        final DatabaseReference banner = database.getReference("Banner");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Banner banner = postSnapShot.getValue(Banner.class);
                    imageList.put(banner.getName()+"@@@"+banner.getId(),banner.getImage());
                    //nazwa @@@ id, dla opisu i id kategorii
                }

                for (String key:imageList.keySet()){
                    String[] keyS = key.split("@@@");
                    String NameFood = keyS[0];
                    String FoodId = keyS[1];

                    //Tworze Banner-slider
                    final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(NameFood)
                            .image(imageList.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    //wysyłam FoodId do FoodsDetail
                                    Intent intent = new Intent(Home.this, FoodsDetail.class);
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }
                            });
                    //Dodaje Bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodsId",FoodId);

                    Banner.addSlider(textSliderView);
                    banner.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Banner.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        Banner.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        Banner.setCustomAnimation(new DescriptionAnimation());
        Banner.setDuration(3950);
    }

    private void loadMenu(){
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuVHder>(options){
            @Override
            public MenuVHder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemV = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
                return new MenuVHder(itemV);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuVHder menuVHder, final int i, Category category) {
                menuVHder.txtNameMenu.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).into(menuVHder.viewImage);

                //final Category clickItem = category;
                menuVHder.setItemClickListener(new IClickListener() {
                    @Override
                    public void onClick(View view, int pos, boolean isClick) {
                        ///po id kategori wyslij do nowego Activity
                        Intent intentFood = new Intent(Home.this, FoodsList.class);
                        //dlatego że CategoryId to klucz, wiec wezmiemy klucz adaptera
                        intentFood.putExtra("CategoryId",adapter.getRef(pos).getKey());
                        startActivity(intentFood);
                        //Toast.makeText(Home.this, ""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.startListening();
        recycle_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void LogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wylogowywanie...")
                .setMessage("Naprawdę chcesz się wylgować?")
                .setNegativeButton("ANULUJ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Common.currentUser = null;
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(Home.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //-------------------MENU---------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment;

        if (id == R.id.nav_menu) {
            return true;
        }else if(id == R.id.nav_home){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);

      }else if(id == R.id.nav_add){
            //fragment = new Fragment(R.layout.activity_foods);
            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.replace(R.id.mainFrame, fragment);
            //ft.commit();
            Intent intent = new Intent(this, Notepad.class);
            startActivity(intent);

        }else if(id == R.id.nav_fav){
            Intent intent = new Intent(this, Favorite.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout){
            LogOut();
//            Intent wyloguj = new Intent(Home.this, MainActivity.class);
//            wyloguj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(wyloguj);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//
//        //adapter.startListening();
//        //FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
//    }

//        @Override
//        protected void onStop() {
//            EventBus.getDefault().unregister(this);
//            super.onStop();
//            if(mAuthStateListener !=null){
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
//        }
//        }

    @Override
    protected void onStop(){
        super.onStop();
        //Banner.stopAutoCycle();
    }
}
