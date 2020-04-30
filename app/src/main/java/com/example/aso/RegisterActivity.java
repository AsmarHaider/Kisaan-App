package com.example.aso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText password,email,phone,Name;
    mehdi.sakout.fancybuttons.FancyButton register;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String Province="Select Province",City="Select City";
    Spinner ProvinceSpinner,CitySpinner;
    String [] ProvincesArray={"Select Province","Punjab","Sindh","Balochistan","Kpk","Gilgit"};
    String [] PunjabCities={"Select City","Lahore","Multan"};
    String [] KpkCities={"Select City","Mardan","Peshawar","Haripur","Mansehra","Sawabi"};
    String [] SindhCities={"Select City","Karachhi","Larkana"};
    String [] BalochistanCities={"Select City","Queta","ziarat"};
    String [] GilgitCities={"Select City","Gilgit city 1"};
    ArrayAdapter<String> ProvinceAdapter;
    ArrayAdapter<String> CityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Registering User ..");

        // initializing the mAuth inctance

        mAuth = FirebaseAuth.getInstance();
        register=findViewById(R.id.register_button);
        password=(EditText)findViewById(R.id.password_text);
        email=(EditText)findViewById(R.id.email_text);
        Name=findViewById(R.id.name_text);
        phone=findViewById(R.id.phone_text);


// Address Spinners things goes here ..


        // check box

        ProvinceSpinner=findViewById(R.id.province_spinner);
        CitySpinner=findViewById(R.id.city_spinner);
        ProvinceAdapter= new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,ProvincesArray);
        ProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ProvinceSpinner.setAdapter(ProvinceAdapter);
        ProvinceSpinner.setOnItemSelectedListener(RegisterActivity.this);


        CityAdapter= new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item,new String[]{"Select Province"});
        CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CitySpinner.setAdapter(CityAdapter);
        CitySpinner.setOnItemSelectedListener(RegisterActivity.this);

        ProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {// whole country , Punjab,Sindh,Balochistan,Kpk,Gilgit
                    case 0:
                        // means sselect province is selected , so no change in city dropdown menu ..
                        break;

                    case 1:
                        // punjab is selected
                        Province = "Punjab";
                        AddCitiesToSpinner(PunjabCities);
                        break;
                    case 2:
                        // Sindh is selected
                        Province = "Sindh";
                       AddCitiesToSpinner(SindhCities);
                        break;

                    case 3:
                        // Balochistan
                        Province = "Balochistan";
                        AddCitiesToSpinner(BalochistanCities);
                        break;

                    case 4:
                        // Kpk is selected
                        Province = "Kpk";
                        AddCitiesToSpinner(KpkCities);
                        break;

                    case 5:
                        // Gilgit is selected
                        Province = "Gilgit";
                       AddCitiesToSpinner(GilgitCities);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(Province.equalsIgnoreCase("Punjab"))
        {
            // cities of pinjab ..
            switch (position)
            {
                case 0:
                    // Select city
                    break;
                case 1:
                    City="Lahore";
                    break;

                case 2:
                    City="Multan";
                    break;

            }
        }
        else if (Province.equalsIgnoreCase("Kpk"))
        {
            switch (position)
            {
                case 0:
                    break;
                case 1:
                    City="Mardan";
                    break;
                case 2:
                    City="Peshawar";
                    break;
                case 3:
                    City="Haripur";
                    break;
                case 4:
                    City="Mansehra";
                    break;
                case 5:
                    City="Sawabi";
                    break;

            }
        }

        else if (Province.equalsIgnoreCase("Sindh"))
        {
            switch (position)
            {
                case 0:
                    break;
                case 1:
                    City="Karachi";
                    break;
                case 2:
                    City="Larkana";
                    break;

            }
        }
        else if (Province.equalsIgnoreCase("Balochistan"))
        {
            switch (position)
            {
                case 0:
                    break;

                case 1:
                    City="Queta";
                    break;
                case 2:
                    City="Ziarat";
                    break;

            }
        }

        else if(Province.equalsIgnoreCase("Gilgit"))
        {
            switch (position)
            {
                case 0:
                    break;
                case 1:
                    City="Gilgit City 01";
                    break;

            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                String Phone=phone.getText().toString();
                String name=Name.getText().toString();


                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    // if email is typed incorrectly ..
                    email.setError("Email is incorrect");
                    email.setFocusable(true);
                }

                if(Password.length()<8)
                {

                    email.setError("Password is too Short");
                    email.setFocusable(true);
                }
                 if(Phone.length()<10)
                {

                    email.setError("Phone is incorrect");
                    email.setFocusable(true);
                }
                 if(name.length()<2)
                {

                    email.setError("Type Name Correctly");
                    email.setFocusable(true);
                }
                 if (Province.equalsIgnoreCase("Select Province"))
                {
                    Toast.makeText(RegisterActivity.this, "Please Select Province", Toast.LENGTH_SHORT).show();
                }

                 if (City.equalsIgnoreCase("Select City"))
                {
                    Toast.makeText(RegisterActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }



                    RegisterUser(Email,Password,"0.0","0.0",Province,City);

            }
        });



    }

    public void AddCitiesToSpinner(String[] cities)
    {
        CityAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item, cities);
        CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CitySpinner.setAdapter(CityAdapter);
        //  CitySpinner.notify();
        CityAdapter.notifyDataSetChanged();
    }
    private void RegisterUser(String email, String password, final String latitude, final String longitude, final String name, final String phone) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email=user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",name);
                            hashMap.put("phone",phone);
                            hashMap.put("picture","");
                            hashMap.put("city",City);
                            hashMap.put("province",Province);
                            hashMap.put("latitude",latitude);
                            hashMap.put("longitude",longitude);
                            hashMap.put("farmer","no");

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference("DSMUsers");
                            databaseReference.child(uid).setValue(hashMap);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(RegisterActivity.this, "You are Registered.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, UserTypeActivity.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
