package com.example.aso;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner ProvinceSpinner,CitySpinner,StreetSpinner;
    String [] ProvincesArray={"Whole Country","Punjab","Sindh","Balochistan","Kpk","Gilgit"};
    String [] PunjabCities={"Whole Province","Punjab city 1","city 2","city 3","city 4"};
    String [] KpkCities={"Whole Province","Kpk city 1","city 2","city 3","city 4"};
    String [] SindhCities={"Whole Province","Sindh city 1","city 2","city 3","city 4"};
    String [] BalochistanCities={"Whole Province","Balochistan city 1","city 2","city 3","city 4"};
    String [] GilgitCities={"Whole Province","Gilgit city 1","city 2","city 3","city 4"};
    String [] PunjabCityOneStreets={"A","B","C","D"};
    String [] SindhCityOneStreets={"A","B","C","D"};
    String [] BalochistanCityOneStreets={"A","B","C","D"};
    String [] KpkCityOneStreets={"A","B","C","D"};
    String [] GilgitCityOneStreets={"A","B","C","D"};
    String Province="Whole Country";
    String City="Whole Province";
    String Street="Whole City";
    String ReferenceCategory="";
    ArrayAdapter<String> ProvinceAdapter;
    ArrayAdapter<String> CityAdapter;
    ArrayAdapter<String> StreetAdapter;
    EditText MinPriceET,MaxPriceET;
    int MinPrice=0,MaxPrice=0;
    mehdi.sakout.fancybuttons.FancyButton ApplyFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        MinPriceET=findViewById(R.id.min_price_et);
        MaxPriceET=findViewById(R.id.max_price_et);
        ApplyFilter=findViewById(R.id.apply_filter);
        ProvinceSpinner=findViewById(R.id.province_spinner);
        CitySpinner=findViewById(R.id.city_spinner);
        StreetSpinner=findViewById(R.id.street_spinner);
ReferenceCategory=getIntent().getStringExtra("cat");
        ProvinceAdapter= new ArrayAdapter<String>(FilterActivity.this,
                android.R.layout.simple_spinner_item,ProvincesArray);
        ProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ProvinceSpinner.setAdapter(ProvinceAdapter);
        ProvinceSpinner.setOnItemSelectedListener(FilterActivity.this);


        CityAdapter= new ArrayAdapter<String>(FilterActivity.this,
                android.R.layout.simple_spinner_item,new String[]{"Select Province"});
        CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CitySpinner.setAdapter(CityAdapter);
        CitySpinner.setOnItemSelectedListener(FilterActivity.this);

        StreetAdapter= new ArrayAdapter<String>(FilterActivity.this,
                android.R.layout.simple_spinner_item,new String[]{"Select City"});
        StreetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StreetSpinner.setAdapter(StreetAdapter);
        StreetSpinner.setOnItemSelectedListener(FilterActivity.this);

ApplyFilter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int MinPrice= Integer.parseInt(MinPriceET.getText().toString());
        int MaxPrice= Integer.parseInt(MaxPriceET.getText().toString());
        Intent i=new Intent(FilterActivity.this,AdsActivity.class);
        if (!(MaxPrice==0))
        {

           i.putExtra("min",MinPrice);
           i.putExtra("max",MaxPrice);
           i.putExtra("pricefilter","yes");

        }
        else
        {
            Toast.makeText(FilterActivity.this,"price filter is no ",Toast.LENGTH_SHORT).show();
            i.putExtra("pricefilter","no"); // if price filter is no , there is no need to mention min and max price ..
        }

        i.putExtra("province",Province);
        i.putExtra("city",City);
        i.putExtra("street",Street);
        i.putExtra("filter","yes"); // enabling the filter block
        i.putExtra("cat",ReferenceCategory);    // sending back the recieved reference category for ads ..
        startActivity(i);
        finish();
    }
});
ProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {// whole country , Punjab,Sindh,Balochistan,Kpk,Gilgit
            case 0:
                // means whole country is selected , so no change in city dropdown menu ..
                break;
            case 1:
                // punjab is selected
                Province = "Punjab";
                Toast.makeText(FilterActivity.this, "punjab selected from ", Toast.LENGTH_SHORT).show();
                ArrayList<String> punjabCities = new ArrayList<>();
                punjabCities.add("city 1");
                punjabCities.add("city 2");
                CityAdapter = new ArrayAdapter<String>(FilterActivity.this,
                        android.R.layout.simple_spinner_item, punjabCities);
                CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                CitySpinner.setAdapter(CityAdapter);
                //  CitySpinner.notify();
                CityAdapter.notifyDataSetChanged();
                break;
            case 2:
                // Sindh is selected
                Province = "Sindh";
                CityAdapter = new ArrayAdapter<String>(FilterActivity.this,
                        android.R.layout.simple_spinner_item, SindhCities);
                CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                CitySpinner.setAdapter(CityAdapter);
                break;

            case 3:
                // Balochistan
                Province = "Balochistan";
                CityAdapter = new ArrayAdapter<String>(FilterActivity.this,
                        android.R.layout.simple_spinner_item, BalochistanCities);
                CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                CitySpinner.setAdapter(CityAdapter);
                break;

            case 4:
                // Kpk is selected
                Province = "Kpk";
                CityAdapter = new ArrayAdapter<String>(FilterActivity.this,
                        android.R.layout.simple_spinner_item, KpkCities);
                CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                CitySpinner.setAdapter(CityAdapter);
                break;

            case 5:
                // Gilgit is selected
                Province = "Gilgit";
                CityAdapter = new ArrayAdapter<String>(FilterActivity.this,
                        android.R.layout.simple_spinner_item, GilgitCities);
                CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                CitySpinner.setAdapter(CityAdapter);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});




    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        if (view==findViewById(R.id.province_spinner))
        {
            switch (position)
            {// whole country , Punjab,Sindh,Balochistan,Kpk,Gilgit
                case 0:
                    // means whole country is selected , so no change in city dropdown menu ..
                    break;
                case 1:
                    // punjab is selected
                    Province="Punjab";
                   // Toast.makeText(FilterActivity.this,"punjab selected from ",Toast.LENGTH_SHORT).show();
                    ArrayList<String> punjabCities=new ArrayList<>();
                    punjabCities.add("city 1");
                    punjabCities.add("city 2");
                    CityAdapter= new ArrayAdapter<String>(FilterActivity.this,
                            android.R.layout.simple_spinner_item,punjabCities);
                    CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CitySpinner.setAdapter(CityAdapter);
                  //  CitySpinner.notify();
                    CityAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    // Sindh is selected
                    Province="Sindh";
                    CityAdapter= new ArrayAdapter<String>(FilterActivity.this,
                            android.R.layout.simple_spinner_item,SindhCities);
                    CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CitySpinner.setAdapter(CityAdapter);
                    break;

                case 3:
                    // Balochistan
                    Province="Balochistan";
                    CityAdapter= new ArrayAdapter<String>(FilterActivity.this,
                            android.R.layout.simple_spinner_item,BalochistanCities);
                    CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CitySpinner.setAdapter(CityAdapter);
                    break;

                case 4:
                    // Kpk is selected
                    Province="Kpk";
                    CityAdapter= new ArrayAdapter<String>(FilterActivity.this,
                            android.R.layout.simple_spinner_item,KpkCities);
                    CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CitySpinner.setAdapter(CityAdapter);
                    break;

                case 5:
                    // Gilgit is selected
                    Province="Gilgit";
                    CityAdapter= new ArrayAdapter<String>(FilterActivity.this,
                            android.R.layout.simple_spinner_item,GilgitCities);
                    CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CitySpinner.setAdapter(CityAdapter);
                    break;
            }


        }

        else if (view==findViewById(R.id.city_spinner)) {

            if (!Province.equalsIgnoreCase("Whole Country"))
            {
                // means some province is selected ..

                if (Province.equalsIgnoreCase("Punjab"))
                {
                    switch (position) {
                        case 0:
                            // whole city
                            break;
                        case 1:
                            // punjab city 1 selected ..

                            StreetAdapter= new ArrayAdapter<String>(FilterActivity.this,
                                    android.R.layout.simple_spinner_item,PunjabCityOneStreets);
                            StreetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            StreetSpinner.setAdapter(StreetAdapter);
                            break;

                    }
                }
                else if (Province.equalsIgnoreCase("Sindh"))
                {
                    switch (position) {
                        case 0:
                            // whole city
                            break;
                        case 1:
                            // Sindh city 1 selected ..
                            StreetAdapter= new ArrayAdapter<String>(FilterActivity.this,
                                    android.R.layout.simple_spinner_item,SindhCityOneStreets);
                            StreetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            StreetSpinner.setAdapter(StreetAdapter);
                            break;

                    }
                }
                else if (Province.equalsIgnoreCase("Balochistan"))
                {
                    switch (position) {
                        case 0:
                            // whole city
                            break;
                        case 1:
                            //Balochistan city 1 selected ..
                            StreetAdapter= new ArrayAdapter<String>(FilterActivity.this,
                                    android.R.layout.simple_spinner_item,BalochistanCityOneStreets);
                            StreetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            StreetSpinner.setAdapter(StreetAdapter);
                            break;

                    }
                }
                else if (Province.equalsIgnoreCase("Kpk"))
                {
                    switch (position) {
                        case 0:
                            // whole city
                            break;
                        case 1:
                            //Kpk city 1 selected ..
                            StreetAdapter= new ArrayAdapter<String>(FilterActivity.this,
                                    android.R.layout.simple_spinner_item,KpkCityOneStreets);
                            StreetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            StreetSpinner.setAdapter(StreetAdapter);
                            break;

                    }
                }
                else if (Province.equalsIgnoreCase("Gilgit"))
                {
                    switch (position) {
                        case 0:
                            // whole city
                            break;
                        case 1:
                            // Gilgit city 1 selected ..
                            StreetAdapter= new ArrayAdapter<String>(FilterActivity.this,
                                    android.R.layout.simple_spinner_item,GilgitCityOneStreets);
                            StreetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            StreetSpinner.setAdapter(StreetAdapter);
                            break;

                    }

                }

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
