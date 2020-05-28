package com.cookapp.ViewHder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.cookapp.Interface.IClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookapp.R;

public class MenuVHder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView viewImage;
    public TextView txtNameMenu;

    private IClickListener itemClickListener;

    public MenuVHder(View itemView) {
        super(itemView);
        txtNameMenu = itemView.findViewById(R.id.MenuName);
        viewImage = itemView.findViewById(R.id.ImageMenu);
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
