package com.example.aso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class PostAdActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner FirstSpinner,SecondSpinner;
    String url="",MainCategory="";
    Uri image_uri;
    String Category="Select Category";
    String [] SubCategoryFruits={"Select Category","Orange","Banana","Grapes","Guava","Mango"};
    ArrayAdapter<String> adapter;
    String [] SubCategoryVegetables={"Select Category","Onion","Garlic","Turnip","Potato","Tomato","Avacado"};
    String [] SubCategoryGrains={"Select Category","Wheat","Maze","Brown Rice"};
    String [] SubCategoryPoultry={"Select Category","Eggs","Chicks"};
    String [] SubCategoryDairy={"Select Category","Milk","Lassi","Ghee","Butter","Cheese","Yougart"};
    String [] SubCategoryAnimal={"Select Category","Cow","Goat","Buffalo","Sheep","Camle"};
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String city="",street="",province="";
    EditText StreetET,CityET,ProvinceET,PriceET,TitleET,DetailsET;
    String UID;
    mehdi.sakout.fancybuttons.FancyButton PostAd;
    ProgressDialog progressDialog;
    ImageView SelectImage;
    StorageReference storageReference;
    String StoragePath="DSMAdsPictures/";


    static final int Storage_request_code=200;
    static final int Camera_request_code=100;
    static final int Image_pick_camera_code=400;
    static final int Image_pick_gallery_code=300;
    String cameraPermission[];
    String storagePermission[];
   Uri downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
        MainCategory=getIntent().getStringExtra("selection");

        StreetET=findViewById(R.id.user_location);
        CityET=findViewById(R.id.user_city);
        ProvinceET=findViewById(R.id.user_province);
        progressDialog=new ProgressDialog(PostAdActivity.this);
        progressDialog.setCancelable(true);

        storageReference=getInstance().getReference();

        // initializing views ..

        SelectImage=findViewById(R.id.add_picture);
        PriceET=findViewById(R.id.ad_price);
        TitleET=findViewById(R.id.ad_title);
        DetailsET=findViewById(R.id.ad_details);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


        // initializing permissions ..
        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};


        // add picture button clicked ..

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // criteria for selecting image ..
                showSelectPictureFrom();


            }
        });

        PostAd=findViewById(R.id.post_ad_button);

        PostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostTheAd();
            }
        });

        UID=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("DSM").child("DSMUsers").child(UID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    city=ds.child("city").getValue().toString();
                    province=ds.child("province").getValue().toString();

                }

                try
                {
                    CityET.setText(city);
                    ProvinceET.setText(province);

                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Spinner element
        FirstSpinner = (Spinner) findViewById(R.id.first_spinner);
        if(MainCategory.equals("fruits"))
        {

            adapter = new ArrayAdapter<String>(PostAdActivity.this,
                    android.R.layout.simple_spinner_item,SubCategoryFruits);

        }
        else if(MainCategory.equals("vegetables"))
        {

           adapter = new ArrayAdapter<String>(PostAdActivity.this,
                    android.R.layout.simple_spinner_item,SubCategoryVegetables);

        }
        else if(MainCategory.equals("grains"))
        {

          adapter = new ArrayAdapter<String>(PostAdActivity.this,
                    android.R.layout.simple_spinner_item,SubCategoryGrains);
        }
        else if(MainCategory.equals("dairy"))
        {

            adapter = new ArrayAdapter<String>(PostAdActivity.this,
                    android.R.layout.simple_spinner_item,SubCategoryDairy);
        }
        else if(MainCategory.equals("poultry"))
        {

            adapter = new ArrayAdapter<String>(PostAdActivity.this,
                    android.R.layout.simple_spinner_item,SubCategoryPoultry);
        }
        else if(MainCategory.equals("animal"))
        {

            adapter = new ArrayAdapter<String>(PostAdActivity.this,
                    android.R.layout.simple_spinner_item,SubCategoryAnimal);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirstSpinner.setAdapter(adapter);
      FirstSpinner.setOnItemSelectedListener(PostAdActivity.this);
    }


    public boolean checkStoragePermission()
    {
        boolean result= ContextCompat.checkSelfPermission(PostAdActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    public void requesStoragePermission()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(storagePermission,Storage_request_code);
        }

    }

    public boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(PostAdActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(PostAdActivity.this,Manifest.permission.CAMERA)== (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    public void requesCameraPermission()
    {
        Toast.makeText(PostAdActivity.this,"Request for Camera permission",Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(cameraPermission,Camera_request_code);
        }


    }


    private void showSelectPictureFrom() {
        String[] optionsItems = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostAdActivity.this);
        builder.setTitle("Pick Image :").setItems(optionsItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    // pick from camera ..
                    if(!checkCameraPermission())
                    {
                        requesCameraPermission();
                    }
                    else {
                        Toast.makeText(PostAdActivity.this,"Pick from camera",Toast.LENGTH_SHORT).show();
                        pickFromCamera();
                    }
                }
                else if(which==1) {
                    // pick from gallery ..
                    if(!checkStoragePermission())
                    {
                          Toast.makeText(PostAdActivity.this,"Storage Permission not granted",Toast.LENGTH_SHORT).show();
                        requesStoragePermission();
                    }
                    else {
                        // Toast.makeText(getActivity(),"Pick from Gallery",Toast.LENGTH_SHORT).show();
                        pickFromGallery();
                    }
                }
            }

        });
        builder.create().show();
    }

    private void PostTheAd() {
        final String price, title, details;
        //String imageURL= UploadImage(image_uri);
        price = PriceET.getText().toString();
        title = TitleET.getText().toString();
        details = DetailsET.getText().toString();

        city = CityET.getText().toString();
        street = StreetET.getText().toString();
        province = ProvinceET.getText().toString();
        if (Category.equalsIgnoreCase("Select Category")
                 || price.equalsIgnoreCase("")
                || title.equalsIgnoreCase("") || details.equalsIgnoreCase("")) {

            if(Category.equalsIgnoreCase("Select Category"))
            {
                Toast.makeText(PostAdActivity.this,"Please Select Category",Toast.LENGTH_SHORT).show();
            }
            else if(price.equalsIgnoreCase(""))
            {
                PriceET.setError("Please Enter Price");
            }
            else if(title.equalsIgnoreCase(""))
            {
                TitleET.setError("Please Enter Title");
            }
            else if ( details.equalsIgnoreCase(""))
            {
                DetailsET.setError("Please Enter Details");
            }
        }

        else {


            final String time = String.valueOf(System.currentTimeMillis());
            if (image_uri != null) {
                progressDialog.setTitle("Posting Ad");

                progressDialog.show();
                String FileAndPathName = StoragePath + "" + "Picture" + "_" + firebaseUser.getUid();
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                StorageReference storageReferenceSecond = storageReference.child(FileAndPathName + timeStamp);
                storageReferenceSecond.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        downloadUrl = uriTask.getResult();
                        if (uriTask.isComplete()) {
                            progressDialog.dismiss();

                            url = downloadUrl.toString();

                            String childKey = FirebaseDatabase.getInstance().getReference().push().getKey();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("title", title);
                            hashMap.put("price", price);
                            hashMap.put("details", details);
                            hashMap.put("Uid", UID);
                            hashMap.put("mainCategory",MainCategory);
                            hashMap.put("subCategory", Category);
                            hashMap.put("image", url);
                            hashMap.put("street", street);
                            hashMap.put("city", city);
                            hashMap.put("province", province);
                            hashMap.put("time", time);
                            hashMap.put("adId", childKey);


                            databaseReference = FirebaseDatabase.getInstance().getReference().child("DSM").child("PostedAds");
                            databaseReference.child(childKey).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(PostAdActivity.this, "Ad Posted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PostAdActivity.this, "Failed to pick image", Toast.LENGTH_SHORT).show();


                    }
                });
            } else {
                Toast.makeText(PostAdActivity.this, "Select Image Again ", Toast.LENGTH_SHORT).show();

            }


        }
    }


    private void pickFromGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent,Image_pick_gallery_code);

    }

    private void pickFromCamera() {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Desc");
        image_uri=PostAdActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,Image_pick_camera_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == Image_pick_camera_code) {
                //  progressDialog.setTitle("Uplosiding Photo");
                //progressDialog.show();
                image_uri = data.getData();
                try {


                    Picasso.get().load(image_uri.toString()).into(SelectImage);
                }catch (Exception e)
                {
                    Toast.makeText(PostAdActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                // UploadImage(image_uri);
            }

            if (requestCode == Image_pick_gallery_code) {

                //progressDialog.setTitle("Uploading Photo");
                //progressDialog.show();
                image_uri = data.getData();
                //  UploadImage(image_uri);
                try {
                    Picasso.get().load(image_uri.toString()).into(SelectImage);
                }catch (Exception e)
                {
                    Toast.makeText(PostAdActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private String UploadImage(Uri image_uri) {


        if (image_uri != null) {

            String FileAndPathName = StoragePath + "" + "Picture" + "_" + firebaseUser.getUid();
            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

            StorageReference storageReferenceSecond = storageReference.child(FileAndPathName + timeStamp);
            storageReferenceSecond.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    downloadUrl = uriTask.getResult();
                    if (uriTask.isComplete()) {
                        progressDialog.dismiss();

url=downloadUrl.toString();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostAdActivity.this, "Failed to pick image", Toast.LENGTH_SHORT).show();
                    url="";

                }
            });
        }
        else {
            Toast.makeText(PostAdActivity.this,"Select Image Again ",Toast.LENGTH_SHORT).show();

        }
        return url;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if(MainCategory.equals("fruits"))
        {
            switch (position) {
                case 0:
                    // Select Category
                    Category = "Select Category";

                    break;
                case 1:
                    //
                    Category = "Orange";
                    break;
                case 2:
                    // Potato
                    Category = "Banana";
                    break;
                case 3:
                    // Tomato

                    Category = "Grapes";
                    break;
                case 4:
                    // Tomato

                    Category = "Guava";
                    break;
                case 5:
                    // Tomato

                    Category = "Mango";
                    break;
            }
        }
        else if(MainCategory.equals("vegetables"))
        {
            switch (position) {
                case 0:
                    Category = "Select Category";
                    break;
                case 1:
                    Category = "Onion";
                    break;
                case 2:
                    Category = "Garlic";
                    break;
                case 3:
                    Category = "Turnip";
                    break;

                case 4:
                    Category="Potato";
                    break;
                case 5:
                    Category="Tomato";
                    break;
                case 6:
                    Category="Avacado";
                    break;

            }
        }
        else if(MainCategory.equals("grains"))
        {
            switch (position) {
                case 0:
                    Category = "Select Category";

                    break;
                case 1:
                    Category = "Wheat";

                    break;
                case 2:
                    Category = "Maze";

                    break;
                case 3:

                    Category = "Rice";
                    break;
            }
  }
        else if (MainCategory.equals("poultry"))
        {
            switch (position) {
                case 0:
                    // first thing .. always selected
                    Category = "Select Category";

                    break;
                case 1:
                    // first option
                    Category = "Eggs";

                    break;
                case 2:
                    // second option
                    Category = "Chicks";

                    break;

            }
        }
        else if(MainCategory.equals("dairy"))
        {
            switch (position) {
                case 0:
                    Category = "Select Category";

                    break;
                case 1:
                    Category = "Milk";

                    break;
                case 2:
                    Category = "Lassi";

                    break;
                case 3:
                    Category = "Ghee";
                    break;

                case 4:

                    Category="Butter";
                    break;
                case 5:
                    Category="Cheese";
                    break;
                case 6:
                    Category="Yougart";
            }
        }

        else if(MainCategory.equals("animal"))
        {
            switch (position) {
                case 0:
                    Category = "Select Category";

                    break;
                case 1:
                    Category = "Cow";

                    break;
                case 2:
                    Category = "Goat";

                    break;
                case 3:
                    Category = "Buffalo";
                    break;

                case 4:

                    Category="Sheep";
                    break;
                case 5:
                    Category="Camle";
                    break;

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
