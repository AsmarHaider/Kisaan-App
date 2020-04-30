package com.example.aso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    StorageReference storageReference;
    String StoragePath="DSMUsersProfilePictures/",UID="";
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView SeeMyAds,UserNameTV,UserPhoneTV;

    CircleImageView UserProfileImage;
    Uri image_uri;
    static final int Storage_request_code=200;
    static final int Camera_request_code=100;
    static final int Image_pick_camera_code=400;
    static final int Image_pick_gallery_code=300;
    String cameraPermission[];
    String storagePermission[]={Manifest.permission.READ_EXTERNAL_STORAGE};
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        UID=firebaseUser.getUid();
      //  Toast.makeText(ProfileActivity.this, UID, Toast.LENGTH_SHORT).show();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("DSMUsers");


        SeeMyAds=findViewById(R.id.see_my_ads);
        storageReference=getInstance().getReference();
        progressDialog=new ProgressDialog(ProfileActivity.this);
        firebaseAuth=FirebaseAuth.getInstance();

        UserProfileImage=findViewById(R.id.profile_picture);
        UserNameTV=findViewById(R.id.user_name);
        UserPhoneTV=findViewById(R.id.user_phone);

        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              //  for(DataSnapshot ds:dataSnapshot.getChildren()) {

                   // Toast.makeText(ProfileActivity.this, "ondata Change", Toast.LENGTH_SHORT).show();
                      String profilepicurl=dataSnapshot.child("picture").getValue().toString();
                    UserNameTV.setText(dataSnapshot.child("name").getValue().toString());
                   UserPhoneTV.setText(dataSnapshot.child("phone").getValue().toString());

                    if(!profilepicurl.equalsIgnoreCase("")) {
                        try {

                            Picasso.get().load(profilepicurl).into(UserProfileImage);
                        }

                        catch (Exception e)
                        {

                        }
                    }

             //   }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView EditProfile=findViewById(R.id.edit_profile);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialogue();
            }
        });

        SeeMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ProfileActivity.this,SeeMyAdsActivity.class);
                i.putExtra("UID",UID);
                startActivity(i);
            }
        });

    }


    private void showEditProfileDialogue()
    {
        String[] optionsItems = {"Update Profile Picture","Edit Name","Edit Phone","Edit Address"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Action : ").setItems(optionsItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    // profile pic

                    showEditProfilePicture();

                }
                else if(which==1)
                {
                    // name
                    showEditNamePhoneDialoge("name");
                }

                else if(which==2)
                {
                    // phone
                    showEditNamePhoneDialoge("phone");


                }


            }

        });
        builder.create().show();
    }

    public boolean checkStoragePermission()
    {
        boolean result= ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);

        return result;

    }

    public void requesStoragePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //requestPermissions(storagePermission,Storage_request_code);
            ActivityCompat.requestPermissions(ProfileActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
    }

    public boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(ProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(ProfileActivity.this,Manifest.permission.CAMERA)== (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    public void requesCameraPermission()
    {
       // Toast.makeText(getActivity(),"Request for Camera permission",Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(cameraPermission,Camera_request_code);
        }

    }


    private void showEditNamePhoneDialoge(final String key) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Update "+key);
        LinearLayout linearLayout=new LinearLayout(ProfileActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText editText=new EditText(ProfileActivity.this);

        editText.setHint("Enter "+key);
        linearLayout.addView(editText);
        alertDialog.setView(linearLayout);

        alertDialog.setPositiveButton("Update",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String value=editText.getText().toString().trim();
                HashMap<String,Object> values=new HashMap<>();
                values.put(key,value);
                if(value.equalsIgnoreCase(""))
                { }
                else {
                    databaseReference.child(firebaseUser.getUid()).updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(ProfileActivity.this,key+" Updated.",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertDialog.create().show();
    }

    private void showEditProfilePicture() {
        String[] optionsItems = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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
                      //  Toast.makeText(ProfileActivity.this,"Pick from camera",Toast.LENGTH_SHORT).show();
                        pickFromCamera();
                    }
                }
                else if(which==1) {
                    // pick from gallery ..
                    if(!checkStoragePermission())
                    {
                           Toast.makeText(ProfileActivity.this,"Storage Permission not granted",Toast.LENGTH_SHORT).show();
                        requesStoragePermission();
                    }
                    else {
                       //  Toast.makeText(ProfileActivity.this,"Pick from Gallery",Toast.LENGTH_SHORT).show();
                        pickFromGallery();
                    }
                }
            }

        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Image_pick_gallery_code);

    }

    private void pickFromCamera() {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Desc");
        image_uri=ProfileActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,Image_pick_camera_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == Image_pick_camera_code) {
                progressDialog.setTitle("Updating Photo");
                progressDialog.show();
                image_uri = data.getData();
                UploadImage(image_uri);
            }

            if (requestCode == Image_pick_gallery_code) {

                progressDialog.setTitle("Updating Photo");
                progressDialog.show();
                image_uri = data.getData();
                UploadImage(image_uri);

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
       // if(id==R.id.action_logout)

        return super.onOptionsItemSelected(item);
    }

    private void UploadImage(Uri image_uri) {

        String FileAndPathName=StoragePath+"picture"+"_"+firebaseUser.getUid();
        StorageReference storageReferenceSecond=storageReference.child(FileAndPathName);
        storageReferenceSecond.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri downloadUrl=uriTask.getResult();
                if(uriTask.isComplete())
                {
                    HashMap<String,Object> results=new HashMap<>();
                    results.put("picture",downloadUrl.toString());

                    databaseReference.child(firebaseUser.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this,"Profile Picture Updated.",Toast.LENGTH_SHORT).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
               // Toast.makeText(getActivity(),"Failed to pick image",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
