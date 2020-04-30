package com.example.aso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SelectionActivity extends AppCompatActivity {

    ImageView Vegetables,Fruits,Grains,Poultry,Dairy,Animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Vegetables=findViewById(R.id.vegetables);
        Fruits=findViewById(R.id.fruit);
        Grains=findViewById(R.id.grains);
        Dairy=findViewById(R.id.dairy);
        Poultry=findViewById(R.id.poltry);
        Animals=findViewById(R.id.animal);


        try {
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/discover-pakistan-fa761.appspot.com/o/DSMImages%2Fvegetable.jpg?alt=media&token=49da99c2-dfe3-4b0c-8350-04393adbc1b4").placeholder(R.drawable.loading).into(Vegetables);

            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/discover-pakistan-fa761.appspot.com/o/DSMImages%2Ffruit.jpg?alt=media&token=0109821c-ea8a-4b56-ba14-f6431ba00b89").into(Fruits);
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/discover-pakistan-fa761.appspot.com/o/DSMImages%2Fgrains.jpg?alt=media&token=ecb30ddb-7d36-4965-8d7b-f539242c26ba").into(Grains);
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/discover-pakistan-fa761.appspot.com/o/DSMImages%2Fdairy.jpg?alt=media&token=f308bb2c-9773-422d-8734-a7fbd5c10816").into(Dairy);
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/discover-pakistan-fa761.appspot.com/o/DSMImages%2Fpoultry.jpg?alt=media&token=d1b1b02d-8ce7-462f-b327-007b72026aad").into(Poultry);
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/ssdd-bd399.appspot.com/o/KisaanImages%2Fdairy_aimals.jpg?alt=media&token=90005733-9dbd-4900-be8a-f72660bb1be6").placeholder(R.drawable.loading).into(Animals);
        }
        catch (Exception e)
        {
            Toast.makeText(SelectionActivity.this,"Error while loading Data : "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        Dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectionActivity.this,MainActivity.class);
                intent.putExtra("selection","dairy");
                startActivity(intent);
            }
        });

        Poultry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectionActivity.this,MainActivity.class);
                intent.putExtra("selection","poultry");
                startActivity(intent);
            }
        });

        Vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectionActivity.this,MainActivity.class);
                intent.putExtra("selection","vegetables");
                startActivity(intent);
            }
        });

        Fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectionActivity.this,MainActivity.class);
                intent.putExtra("selection","fruits");
             //   Toast.makeText(SelectionActivity.this,"fruits")
                startActivity(intent);
            }
        });

        Grains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectionActivity.this,MainActivity.class);
                intent.putExtra("selection","grains");
                startActivity(intent);
            }
        });

        Animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectionActivity.this,MainActivity.class);
                intent.putExtra("selection","animals");
                startActivity(intent);
            }
        });

    }
}
