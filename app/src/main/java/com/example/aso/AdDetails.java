package com.example.aso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.spec.ECField;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdDetails extends AppCompatActivity {

    String Price,TimeStamp,ImageUrl,UID,Details,Title,Username,ProfilePictureUrl,Phone="";

    CircleImageView Profile;
    TextView DateTV,PriceTV,DetailsTV,TitleTV,UserName,UserPhone,BrowseUserAds;
    TextView MakeCall,SendText;
    ImageView AdImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        // initializing views ..

        UserPhone=findViewById(R.id.user_phone);
        Profile=findViewById(R.id.profile_picture);
        UserName=findViewById(R.id.user_name);
        AdImage=findViewById(R.id.Image_Ads);
        TitleTV=findViewById(R.id.image_title_ads);
        DateTV=findViewById(R.id.time_TV);
        PriceTV=findViewById(R.id.ad_price_tv);
        DetailsTV=findViewById(R.id.ad_details_tv);
        BrowseUserAds=findViewById(R.id.browse_user_ads);
        MakeCall=findViewById(R.id.make_call);
        SendText=findViewById(R.id.send_sms);

        // Call and Sms functionality ..
        SendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Phone.equalsIgnoreCase("")) {
                    Intent sInt = new Intent(Intent.ACTION_VIEW);
                    sInt.putExtra("address", new String( Phone));
                    sInt.putExtra("sms_body", "'' Kisaan - From Farms to your door step ''");
                    sInt.setType("vnd.android-dir/mms-sms");
                    startActivity(sInt);
                }
            }
        });
        MakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Phone.equalsIgnoreCase("")) {


                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",  Phone, null));
                startActivity(intent);
            }

            }
        });


        // receiving values from sender intent ..

        Price=getIntent().getStringExtra("price");
        ImageUrl=getIntent().getStringExtra("image");
        Details=getIntent().getStringExtra("details");
        Title=getIntent().getStringExtra("title");
        TimeStamp=getIntent().getStringExtra("time");
        UID=getIntent().getStringExtra("uid");


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("DSMUsers");
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(TimeStamp));
        String dateformat= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();
        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //ProfilePictureUrl=dataSnapshot.child("picture").getValue().toString();
                Username=dataSnapshot.child("name").getValue().toString();
                Phone=dataSnapshot.child("phone").getValue().toString();
               try {
                   Picasso.get().load(ProfilePictureUrl).into(Profile);
                   UserName.setText(Username);
                   UserPhone.setText(Phone);
               }
               catch (Exception e)
               {

               }
               }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        BrowseUserAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdDetails.this,UserAdsActivity.class);
                i.putExtra("UID",UID);
                startActivity(i);
            }
        });
        // setting values ..

        DateTV.setText(dateformat);
        TitleTV.setText(Title);
        PriceTV.setText("Rs. "+Price);
        DetailsTV.setText(Details);
try
{
    Picasso.get().load(ImageUrl).into(AdImage);

}catch (Exception e)
{

}



    }


}

