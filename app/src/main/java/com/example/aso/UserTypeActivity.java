package com.example.aso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class UserTypeActivity extends AppCompatActivity {

    mehdi.sakout.fancybuttons.FancyButton YesButton,NoButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        mAuth=FirebaseAuth.getInstance();

    YesButton=findViewById(R.id.yes_button);
    NoButton=findViewById(R.id.no_button);

    YesButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mAuth.getCurrentUser() != null)
            {
                startActivity(new Intent(UserTypeActivity.this, FarmLocationActivity.class));
                finish();
            }
        }
    });

    NoButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mAuth.getCurrentUser()!=null)
            {
                // user is registered ..
                startActivity(new Intent(UserTypeActivity.this,SelectionActivity.class));
                finish();
            }
        }
    });
    }
}
