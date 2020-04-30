package com.example.aso.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aso.R;

public class HolderAds extends RecyclerView.ViewHolder {

    public ImageView Image;
    public TextView PriceTV,DateTV,TitleTV;

    public HolderAds(@NonNull View itemView) {
        super(itemView);
        Image=itemView.findViewById(R.id.Image);
        PriceTV=itemView.findViewById(R.id.price_tv);
        DateTV=itemView.findViewById(R.id.date_tv);
        TitleTV=itemView.findViewById(R.id.image_title);


    }
}
