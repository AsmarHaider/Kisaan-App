package com.example.aso.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aso.Holder.HolderMain;
import com.example.aso.Model.ModelMain;
import com.example.aso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMain extends RecyclerView.Adapter<HolderMain>
{
    Context context;
    ArrayList<ModelMain> arrayList;

    public AdapterMain(Context context, ArrayList<ModelMain> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HolderMain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main,null);
        return new HolderMain(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMain holder, int position) {

        holder.ItemTitle.setText(arrayList.get(position).getTitle());
        try
        {
            Picasso.get().load(arrayList.get(position).getImage()).into(holder.ItemImage);
        }
        catch (Exception e)
        {
            Picasso.get().load(R.drawable.loading).into(holder.ItemImage);

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public String getLastItemId() {

        return arrayList.get(arrayList.size()-1).getId();

    }
}
