package com.example.aso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FarmProductsActivity extends AppCompatActivity {

    mehdi.sakout.fancybuttons.FancyButton DoneButton;
    RadioGroup RGVegetables,RGPoultry,RGDairy,RGAnimals,RGGrains,RGFruits;
LinearLayout LLVegetables,LLFruits,LLPoultry,LLAnimals,LLDairy,LLGrains;
// fruits
String Orange="no",Mango="no",Grapes="no",Water_Melon="no",Banana="no",Dates="no",Guava="no";
CheckBox OrangeCB,MangoCB,GrapesCB,Water_MelonCB,BananaCB,DatesCB,GuavaCB;

// vegetables
String Onion="no",Garlic="no",Turnip="no",Avacado="no",Potato="no",Tomato="no";
CheckBox OnionCB,GarlicCB,TurnipCB,AvacadoCB,PotatoCB,TomatoCB;
// dairy products
String Milk="no",Butter="no",Cheese="no",Yougart="no",Ghee="no",Lassi="no";
CheckBox MilkCB,ButterCB,CheeseCB,YougartCB,GheeCB,LassiCB;
// grains
String Wheat="no",Maze="no",Rice="no",Lobya="no";
CheckBox WheatCB,MazeCB,RiceCB,LobyaCB;
// animals ..
String Goat="no",Cow="no",Buffalo="no",Camle="no",Sheep="no";
CheckBox GoatCB,CowCB,BuffaloCB,CamleCB,SheepCB;

// poultry products ..
String Egg="no",Chicken="no";
CheckBox EggCB,ChickenCB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_products);

        // initializing views ..
        // Radio Groups ..

        RGVegetables=findViewById(R.id.radio_group_vegetable);
        RGAnimals=findViewById(R.id.radio_group_animal);
        RGFruits=findViewById(R.id.radio_group_fruit);
        RGDairy=findViewById(R.id.radio_group_dairy);
        RGGrains=findViewById(R.id.radio_group_grains);
        RGPoultry=findViewById(R.id.radio_group_poultry);


        DoneButton=findViewById(R.id.done_button);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid=user.getUid();
                HashMap<Object,String> hashMap=new HashMap<>();
                hashMap.put("Onion",Onion);
                hashMap.put("Garlic",Garlic);
                hashMap.put("Turnip",Turnip);
                hashMap.put("Tomato",Tomato);
                hashMap.put("Potato",Potato);
                hashMap.put("Avacado",Avacado);
                // fruits
                hashMap.put("Orange",Orange);
                hashMap.put("Banana",Banana);
                hashMap.put("Grapes",Grapes);
                hashMap.put("Dates",Dates);
                hashMap.put("Water Melon",Water_Melon);
                hashMap.put("Guava",Guava);

                hashMap.put("Butter", Butter);
                hashMap.put("Cheese",Cheese);
                hashMap.put("Milk",Milk);
                hashMap.put("Yougart",Yougart);
                hashMap.put("Lassi",Lassi);
                hashMap.put("Ghee",Ghee);


                hashMap.put("Egg",Egg);
                hashMap.put("Chicken",Chicken);

                hashMap.put("Maze",Maze);
                hashMap.put("Wheat",Wheat);
                hashMap.put("Lobya",Lobya);
                hashMap.put("Rice",Rice);


                hashMap.put("Goat",Goat);
                hashMap.put("Buffalo",Buffalo);
                hashMap.put("Cow",Cow);
                hashMap.put("Sheep",Sheep);
                hashMap.put("Camle",Camle);
                hashMap.put("farmer","yes");

                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference("DSMUsers");
                databaseReference.child(uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(FarmProductsActivity.this, "Your Products Added.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FarmProductsActivity.this,SelectionActivity.class));
                    finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FarmProductsActivity.this, "Ooops :("+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        // Check boxes ..


        OrangeCB=findViewById(R.id.orange_cb);
        MangoCB=findViewById(R.id.mango_cb);
        GrapesCB=findViewById(R.id.grapes_cb);
        Water_MelonCB=findViewById(R.id.water_melon_cb);
        BananaCB=findViewById(R.id.banana_cb);
        DatesCB=findViewById(R.id.dates_cb);
        GuavaCB=findViewById(R.id.guava_cb);

        // vegetables ..
        OnionCB=findViewById(R.id.onion_cb);
        GarlicCB=findViewById(R.id.garlic_cb);
        TurnipCB=findViewById(R.id.turnip_cb);
        AvacadoCB=findViewById(R.id.avacado_cb);
        PotatoCB=findViewById(R.id.potato_cb);
        TomatoCB=findViewById(R.id.tomato_cb);

        // dairy here ..
        MilkCB=findViewById(R.id.milk_cb);
        ButterCB=findViewById(R.id.butter_cb);
        CheeseCB=findViewById(R.id.cheese_cb);
        YougartCB=findViewById(R.id.yougart_cb);
        GheeCB=findViewById(R.id.ghee_cb);
        LassiCB=findViewById(R.id.lassi_cb);
        // grains hrere ..


       WheatCB=findViewById(R.id.wheat_cb);
       MazeCB=findViewById(R.id.maze_cb);
       RiceCB=findViewById(R.id.rice_cb);
       LobyaCB=findViewById(R.id.lobya_cb);

// animals ..
        GoatCB=findViewById(R.id.goat_cb);
        CowCB=findViewById(R.id.cow_cb);
        BuffaloCB=findViewById(R.id.buffalo_cb);
        CamleCB=findViewById(R.id.camle_cb);
        SheepCB=findViewById(R.id.sheep_cb);

// poultry products ..
        EggCB=findViewById(R.id.egg_cb);
        ChickenCB=findViewById(R.id.chicken_cb);

        // Linear Layouts ..

        LLVegetables=findViewById(R.id.layout_vegetables_products);
        LLFruits=findViewById(R.id.layout_fruit_products);
        LLAnimals=findViewById(R.id.layout_animal_products);
        LLDairy=findViewById(R.id.layout_dairy_products);
        LLGrains=findViewById(R.id.layout_grains_products);
        LLPoultry=findViewById(R.id.layout_poultry_products);

        RGVegetables.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_vegetable_yes)
                {
                    LLVegetables.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radio_vegetable_no)
                {
                    LLVegetables.setVisibility(View.INVISIBLE);
                }
            }
        });

        RGFruits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_fruit_yes)
                {
                    LLFruits.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radio_fruit_no)
                {
                    LLFruits.setVisibility(View.INVISIBLE);
                }
            }
        });

        RGPoultry.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_poultry_yes)
                {
                    LLPoultry.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radio_poultry_no)
                {
                    LLPoultry.setVisibility(View.INVISIBLE);
                }
            }
        });

        RGGrains.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_grains_yes)
                {
                    LLGrains.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radio_grains_no)
                {
                    LLGrains.setVisibility(View.INVISIBLE);
                }
            }
        });

        RGAnimals.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_animal_yes)
                {
                    LLAnimals.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radio_animal_no)
                {
                    LLAnimals.setVisibility(View.INVISIBLE);
                }
            }
        });

        RGDairy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_dairy_yes)
                {
                    LLDairy.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.radio_dairy_no)
                {
                    LLDairy.setVisibility(View.INVISIBLE);
                }
            }
        });


        // Check boxes data ..

      LobyaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Lobya="yes";
                }
                else {

                    Lobya="no";
                }
            }
        });

       RiceCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Rice="yes";
                }
                else {

                    Rice="no";
                }
            }
        });
        MazeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Maze="yes";
                }
                else {

                    Maze="no";
                }
            }
        });

        WheatCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                  Wheat="yes";
                }
                else {

                    Wheat="no";
                }
            }
        });

// animals ..


        SheepCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Sheep="yes";
                }
                else {

                    Sheep="no";
                }
            }
        });

        CamleCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                   Camle="yes";
                }
                else {
                    Camle="no";
                }
            }
        });

        BuffaloCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Buffalo="yes";
                }
                else {

                    Buffalo="no";
                }
            }
        });

        CowCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Cow="yes";
                }
                else {

                    Cow="no";
                }
            }
        });

        GoatCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Goat="yes";
                }
                else {

                    Goat="no";
                }
            }
        });

// poultry products ..


        ChickenCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Chicken="yes";
                }
                else {

                    Chicken="no";
                }
            }
        });
        EggCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Egg="yes";
                }
                else {

                    Egg="no";
                }
            }
        });

        LassiCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Lassi="yes";
                }
                else {

                    Lassi="no";
                }
            }
        });

       GheeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Ghee="yes";
                }
                else {

                    Ghee="no";
                }
            }
        });

        YougartCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Yougart="yes";
                }
                else {

                    Yougart="no";
                }
            }
        });

        CheeseCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                   Cheese="yes";
                }
                else {

                    Cheese="no";
                }
            }
        });

       ButterCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Butter="yes";
                }
                else {

                    Butter="no";
                }
            }
        });


        MilkCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Milk="yes";
                }
                else {

                    Milk="no";
                }
            }
        });


        OnionCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Onion="yes";
                }
                else {

                    Onion="no";
                }
            }
        });

        GarlicCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Garlic="yes";
                }
                else {

                    Garlic="no";
                }
            }
        });

        TurnipCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Turnip="yes";
                }
                else {

                   Turnip="no";
                }
            }
        });

        TomatoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Tomato="yes";
                }
                else {

                    Tomato="no";
                }
            }
        });

        PotatoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Potato="yes";
                }
                else {

                    Potato="no";
                }
            }
        });

        AvacadoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Avacado="yes";
                }
                else {

                    Avacado="no";
                }
            }
        });




        GuavaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Guava="yes";
                }
                else {

                    Guava="no";
                }
            }
        });

        DatesCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Dates="yes";
                }
                else {

                    Dates="no";
                }
            }
        });


        BananaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Banana="yes";
                }
                else {

                    Banana="no";
                }
            }
        });

        GrapesCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                   Grapes="yes";
                }
                else {

                   Grapes="no";
                }
            }
        });

        Water_MelonCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Water_Melon="yes";
                }
                else {

                   Water_Melon="no";
                }
            }
        });

        MangoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Mango="yes";
                }
                else {

                    Mango="no";
                }
            }
        });
        OrangeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // is checked is true
                    Orange="yes";
                }
                else {

                    Orange="no";
                }
            }
        });





    }

}
