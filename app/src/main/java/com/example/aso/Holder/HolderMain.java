package com.example.aso.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aso.R;

public class HolderMain extends RecyclerView.ViewHolder {
public ImageView ItemImage;
public TextView ItemTitle,ItemLikes;

    public HolderMain(@NonNull View itemView) {
        super(itemView);
        ItemImage = itemView.findViewById(R.id.Image);
        ItemTitle = itemView.findViewById(R.id.image_title);
    }
}
