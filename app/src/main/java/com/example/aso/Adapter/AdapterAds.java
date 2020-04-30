package com.example.aso.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aso.Holder.HolderAds;
import com.example.aso.Model.ModelAds;
import com.example.aso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
public class AdapterAds extends RecyclerView.Adapter<HolderAds> {

    Context context;
    ArrayList<ModelAds> adsArrayList;

    public AdapterAds(Context context, ArrayList<ModelAds> adsArrayList) {
        this.context = context;
        this.adsArrayList = adsArrayList;
    }

    @NonNull
    @Override
    public HolderAds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_ads,null);
        return new HolderAds(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAds holder, int position) {

        try
        {
            Picasso.get().load(adsArrayList.get(position).getPicture()).into(holder.Image);

        }catch (Exception e)
        {
Picasso.get().load(R.drawable.loading).into(holder.Image);
        }

        try
        {

            holder.TitleTV.setText(adsArrayList.get(position).getTitle());
            String date=adsArrayList.get(position).getTime();

                    Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(Long.parseLong(date));
            String dateformat= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

            holder.DateTV.setText(dateformat);

            holder.PriceTV.setText("Rs. "+adsArrayList.get(position).getPrice());
        }catch (Exception e)
        {

        }



    }

    @Override
    public int getItemCount() {
        return adsArrayList.size();
    }

    public String getLastItemId() {

        return adsArrayList.get(adsArrayList.size()-1).getAdId();

    }
}
