package com.cookapp.ViewHder;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookapp.FoodsDetail;
import com.cookapp.Model.Favorites;
import com.cookapp.Interface.IClickListener;
import com.cookapp.R;

import java.util.ArrayList;
import java.util.List;

class FavoritesVHder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView FoodName, FoodCalories, FoodTime;
    public ImageView btnIFav;

    private IClickListener itemClickListener;

    public FavoritesVHder(@NonNull View itemView) {
        super(itemView);
        FoodName = itemView.findViewById(R.id.FoodsName);
        FoodCalories = itemView.findViewById(R.id.FoodsCalories);
        FoodTime = itemView.findViewById(R.id.FoodsTime);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(IClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesVHder>{

    private List<Favorites> listFavorites= new ArrayList<>();
    private Context context;

public FavoritesAdapter(List<Favorites> listFavorites,Context context){
    this.listFavorites =listFavorites;
    this.context = context;
}

    @NonNull
    @Override
    public FavoritesVHder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.favorites_item,parent,false);
        return new FavoritesVHder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesVHder holder, final int position) {
        //dane przekazane do widoku FavoritesVHder
        holder.FoodName.setText(listFavorites.get(position).getFoodName());
        holder.FoodCalories.setText(listFavorites.get(position).getFoodCalories());
        holder.FoodTime.setText(listFavorites.get(position).getFoodTime());

        holder.setItemClickListener(new IClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isClick) {
                Intent intent = new Intent(context, FoodsDetail.class);
                intent.putExtra("FoodsId",listFavorites.get(pos).getFoodId());
                context.startActivity(intent);
            }
        });
    }
        //zlicza elementy dodane do listy
    @Override
    public int getItemCount() {
        return  listFavorites.size();
    }
}
