package com.cookapp.ViewHder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookapp.Interface.IClickListener;
import com.cookapp.R;


public class FoodsVHder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView ImageFoods, ImageFav;
    public TextView FoodsName;

    private IClickListener itemClickListener;

    public FoodsVHder(@NonNull View itemView) {
        super(itemView);
        FoodsName = (TextView)itemView.findViewById(R.id.FoodsName);
        ImageFoods = itemView.findViewById(R.id.ImageFoods);
        ImageFav = itemView.findViewById(R.id.ImageFav);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(IClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
