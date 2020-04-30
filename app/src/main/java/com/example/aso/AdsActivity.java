package com.example.aso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.example.aso.Adapter.AdapterAds;
import com.example.aso.Adapter.AdapterMain;
import com.example.aso.Model.ModelAds;
import com.example.aso.Model.ModelMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdsActivity extends AppCompatActivity {


    AdapterAds adapterAds;
    RecyclerView RecyclerViewAds;
    ArrayList<ModelAds> modelAdsArrayList;
    ModelAds modelAds;
    LinearLayoutManager linearLayoutManager;
    private int mPostsPerPage=3;
    private boolean mIsLoading=false;
    private int mTotalItemCount;
    private int mLastVisibleItemPosition;
    String ReferenceCategory="",Filter;
    Boolean PriceFilter=false;
    int MinPrice=0,MaxPrice=0;
    DatabaseReference databaseReference;
    mehdi.sakout.fancybuttons.FancyButton NearbyFarms;
    boolean check=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        ReferenceCategory = getIntent().getStringExtra("cat"); // recieving category as banana for fruit's sub catagory
        Filter = getIntent().getStringExtra("filter");
       databaseReference = FirebaseDatabase.getInstance().getReference().child("DSM").child("PostedAds");
        //.child(ReferenceCategory);
        NearbyFarms=findViewById(R.id.nearby_farms_button);

        NearbyFarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fetch the current location and then show the nearby farms ..

                if (!ReferenceCategory.equalsIgnoreCase("")) {
                Intent i = new Intent(AdsActivity.this, NearbyFarmActivity.class);
                i.putExtra("cat", ReferenceCategory);
                startActivity(i);
            }

            }
        });

        linearLayoutManager = new LinearLayoutManager(this);

        modelAdsArrayList = new ArrayList<>();
        modelAdsArrayList.clear();
        RecyclerViewAds = findViewById(R.id.recyclerviewads);
        RecyclerViewAds.setHasFixedSize(true);
       // linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        RecyclerViewAds.setLayoutManager(linearLayoutManager);
        //getData(null);
getData();
        RecyclerViewAds.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItemPosition
                        + mPostsPerPage))
                {

                // getData(adapterAds.getLastItemId());
                 //mIsLoading = true;

                }
            }
        });
        RecycleClick.addTo(RecyclerViewAds).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(AdsActivity.this, AdDetails.class);
                i.putExtra("uid", modelAdsArrayList.get(position).getUid());
                i.putExtra("price", modelAdsArrayList.get(position).getPrice());
                i.putExtra("title", modelAdsArrayList.get(position).getTitle());
                i.putExtra("time", modelAdsArrayList.get(position).getTime());
                i.putExtra("image", modelAdsArrayList.get(position).getPicture());
                i.putExtra("details", modelAdsArrayList.get(position).getDetails());

                // Toast.makeText(AdsActivity.this,modelAdsArrayList.get(position).getUid(),Toast.LENGTH_SHORT).show();

                startActivity(i);
            }
        });
    }

    private void getData(final String nodeId) {


        Query query;

        if (nodeId == null) {


            query = databaseReference
                    .orderByKey()
                    .limitToFirst(mPostsPerPage);
        }
        else {
            Toast.makeText(this, "node not null", Toast.LENGTH_SHORT).show();

            query = databaseReference
                    .orderByKey()
                    .startAt(nodeId)
                    .limitToFirst(mPostsPerPage);
        }


            if (Filter.equalsIgnoreCase("no")) {
                query.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (nodeId!=null)
                        {

                            // means it is scrolled ..
                            if (dataSnapshot.getChildrenCount() == 1)
                            {

                                check=true;

                            }

                        }
                        Toast.makeText(AdsActivity.this,String.valueOf( dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                        //mIsLoading=true;
                        if (!check) {
                         //   int i=1;
                         //  modelAdsArrayList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                               // Toast.makeText(AdsActivity.this, "i ="+i, Toast.LENGTH_SHORT).show();
                                if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                    if ((ds.getKey()).equalsIgnoreCase(nodeId))
                                    {
                                    }else {
                                        mIsLoading=true;
                                        modelAds = new ModelAds();
                                        modelAds.setPrice(ds.child("price").getValue().toString());
                                        modelAds.setPicture(ds.child("image").getValue().toString());
                                        modelAds.setTitle(ds.child("title").getValue().toString());
                                        modelAds.setTime(ds.child("time").getValue().toString());
                                        modelAds.setUid(ds.child("Uid").getValue().toString());
                                        modelAds.setDetails(ds.child("details").getValue().toString());
                                        modelAdsArrayList.add(modelAds);
                                    }
                                }
                               // ++i;
                            }
                        }


                        //  perform sorting if needed ..
                        adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                        adapterAds.notifyDataSetChanged();
                        RecyclerViewAds.setAdapter(adapterAds);
                        mIsLoading=false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if (Filter.equalsIgnoreCase("yes")) {

                // filter is yes , it is to be applied .

                final String Province = getIntent().getStringExtra("province");
                final String City = getIntent().getStringExtra("city");
                final String Street = getIntent().getStringExtra("street");

                if (Province.equalsIgnoreCase("Whole Country")) {
                    // means no check on province  , i.e. whole country ..

                    // so check if there is any check on price or not ..
                    String PF=getIntent().getStringExtra("pricefilter");
                    Toast.makeText(AdsActivity.this,PF,Toast.LENGTH_SHORT).show();

                    if (!PF.equalsIgnoreCase("no")) {

                        // means there are min and max price parameters ..
                        MinPrice = getIntent().getIntExtra("min", 0);
                        MaxPrice = getIntent().getIntExtra("max", 0);


                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildrenCount() != 0) {

                                   // modelAdsArrayList.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        //int price .... check price for prince range from to this ..
                                        if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                        int price = Integer.parseInt(ds.child("price").getValue().toString());
                                        if (price >= MinPrice && price <= MaxPrice) {
                                            mIsLoading=true;
                                            modelAds = new ModelAds();
                                            modelAds.setPrice(ds.child("price").getValue().toString());
                                            modelAds.setPicture(ds.child("image").getValue().toString());
                                            modelAds.setTitle(ds.child("title").getValue().toString());
                                            modelAds.setTime(ds.child("time").getValue().toString());
                                            modelAds.setUid(ds.child("Uid").getValue().toString());
                                            modelAds.setDetails(ds.child("details").getValue().toString());
                                            modelAdsArrayList.add(modelAds);
                                        }}
                                    }

                                } else {
                                    Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                }

                                //  perform sorting if needed ..
                                adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                adapterAds.notifyDataSetChanged();
                                RecyclerViewAds.setAdapter(adapterAds);
                                mIsLoading=false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        // no price filter ..
                        //so display ads from whole country ..
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildrenCount() != 0) {


                                   // modelAdsArrayList.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                            mIsLoading=true;
                                        modelAds = new ModelAds();
                                        modelAds.setPrice(ds.child("price").getValue().toString());
                                        modelAds.setPicture(ds.child("image").getValue().toString());
                                        modelAds.setTitle(ds.child("title").getValue().toString());
                                        modelAds.setTime(ds.child("time").getValue().toString());
                                        modelAds.setUid(ds.child("Uid").getValue().toString());
                                        modelAds.setDetails(ds.child("details").getValue().toString());
                                        modelAdsArrayList.add(modelAds);
                                    }}
                                } else {
                                    Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                }

                                //  perform sorting if needed ..
                                adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                adapterAds.notifyDataSetChanged();
                                RecyclerViewAds.setAdapter(adapterAds);
                                mIsLoading=false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else {

                    // means some province is selected ..
                    if (City.equalsIgnoreCase("Whole Province")) {
                        //  means no check on city ..
                        String PF=getIntent().getStringExtra("pricefilter");
                        if (!PF.equalsIgnoreCase("no")) {
                            PriceFilter = true;
                            // means there are min and max price parameters ..
                            MinPrice = getIntent().getIntExtra("min", 0);
                            MaxPrice = getIntent().getIntExtra("max", 0);


                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() != 0) {

                                     //   modelAdsArrayList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            //int price .... check price for prince range from to this ..
                                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                               mIsLoading=true;
                                                int price = Integer.parseInt(ds.child("price").getValue().toString());

                                                if ((price >= MinPrice && price <= MaxPrice) && ds.child("province").equals(Province)) {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                    }

                                    //  perform sorting if needed ..
                                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                    adapterAds.notifyDataSetChanged();
                                    RecyclerViewAds.setAdapter(adapterAds);
                                    mIsLoading=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // no price filter ..
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() != 0)
                                    {


                                      //  modelAdsArrayList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                                if ((ds.child("province").equals(Province))) {
                                                    mIsLoading=true;
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                    }

                                    //  perform sorting if needed ..
                                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                    adapterAds.notifyDataSetChanged();
                                    RecyclerViewAds.setAdapter(adapterAds);
                                    mIsLoading=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }


                    else {
                        // means some city is also selected ..
                        if (Street.equalsIgnoreCase("Whole City"))
                        {
                            // means no street is specified ..
                            String PF=getIntent().getStringExtra("pricefilter");
                            if (!PF.equalsIgnoreCase("no")) {
                                // means there are min and max price parameters ..
                                MinPrice = getIntent().getIntExtra("min", 0);
                                MaxPrice = getIntent().getIntExtra("max", 0);


                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getChildrenCount() != 0) {

                                           // modelAdsArrayList.clear();

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    //int price .... check price for prince range from to this ..
                                                    if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                                    int price = Integer.parseInt(ds.child("price").getValue().toString());
mIsLoading=true;
                                                    if ((price >= MinPrice && price <= MaxPrice) && (ds.child("city").equals(City)) && (ds.child("province").equals(Province))) {
                                                        modelAds = new ModelAds();
                                                        modelAds.setPrice(ds.child("price").getValue().toString());
                                                        modelAds.setPicture(ds.child("image").getValue().toString());
                                                        modelAds.setTitle(ds.child("title").getValue().toString());
                                                        modelAds.setTime(ds.child("time").getValue().toString());
                                                        modelAds.setUid(ds.child("Uid").getValue().toString());
                                                        modelAds.setDetails(ds.child("details").getValue().toString());
                                                        modelAdsArrayList.add(modelAds);
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                        }

                                        //  perform sorting if needed ..
                                        adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                        adapterAds.notifyDataSetChanged();
                                        RecyclerViewAds.setAdapter(adapterAds);
                                        mIsLoading=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                // no price filter ..

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getChildrenCount() != 0) {


                                           // modelAdsArrayList.clear();
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                                mIsLoading=true;
                                                if ((ds.child("city").equals(City)) && (ds.child("province").equals(Province))) {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);

                                                }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                        }

                                        //  perform sorting if needed ..
                                        adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                        adapterAds.notifyDataSetChanged();
                                        RecyclerViewAds.setAdapter(adapterAds);
                                        mIsLoading=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                        else
                        {
                            // means some street is also selected ..
                            String PF=getIntent().getStringExtra("pricefilter");
                            if (!PF.equalsIgnoreCase("no")) {
                                // means there are min and max price parameters ..
                                MinPrice = getIntent().getIntExtra("min", 0);
                                MaxPrice = getIntent().getIntExtra("max", 0);


                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getChildrenCount() != 0) {

                                          //  modelAdsArrayList.clear();
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                //int price .... check price for prince range from to this ..
                                                if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                                    int price = Integer.parseInt(ds.child("price").getValue().toString());

                                                    mIsLoading=true;
                                                    if ((price >= MinPrice && price <= MaxPrice)
                                                            && (ds.child("city").equals(City))
                                                            && (ds.child("province").equals(Province))
                                                            && (ds.child("street").equals(Street))) {
                                                        modelAds = new ModelAds();
                                                        modelAds.setPrice(ds.child("price").getValue().toString());
                                                        modelAds.setPicture(ds.child("image").getValue().toString());
                                                        modelAds.setTitle(ds.child("title").getValue().toString());
                                                        modelAds.setTime(ds.child("time").getValue().toString());
                                                        modelAds.setUid(ds.child("Uid").getValue().toString());
                                                        modelAds.setDetails(ds.child("details").getValue().toString());
                                                        modelAdsArrayList.add(modelAds);
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                        }

                                        //  perform sorting if needed ..
                                        adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                        adapterAds.notifyDataSetChanged();
                                        RecyclerViewAds.setAdapter(adapterAds);
                                        mIsLoading=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {
                                // no price filter ..

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getChildrenCount() != 0) {


                                         //   modelAdsArrayList.clear();
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                                mIsLoading=true;
                                                if ((ds.child("city").equals(City))
                                                        && (ds.child("province").equals(Province))
                                                        && (ds.child("street").equals(Street)))
                                                {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);

                                                }}
                                            }
                                        } else {
                                            Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                        }

                                        //  perform sorting if needed ..
                                        adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                        adapterAds.notifyDataSetChanged();
                                        RecyclerViewAds.setAdapter(adapterAds);
                                        mIsLoading=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }


                    }

                }





            }



        }
    private void getData() {


        Query query;

            query = databaseReference
                    .orderByKey();

        if (Filter.equalsIgnoreCase("no")) {
            query.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   // Toast.makeText(AdsActivity.this,String.valueOf( dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                    //mIsLoading=true;

                        //   int i=1;
                         modelAdsArrayList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            // Toast.makeText(AdsActivity.this, "i ="+i, Toast.LENGTH_SHORT).show();
                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                    mIsLoading=true;
                                    modelAds = new ModelAds();
                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                    modelAds.setTime(ds.child("time").getValue().toString());
                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                    modelAdsArrayList.add(modelAds);


                        }
                    }


                    //  perform sorting if needed ..
                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                    adapterAds.notifyDataSetChanged();
                    RecyclerViewAds.setAdapter(adapterAds);
                    mIsLoading=false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (Filter.equalsIgnoreCase("yes")) {

            // filter is yes , it is to be applied .

            final String Province = getIntent().getStringExtra("province");
            final String City = getIntent().getStringExtra("city");
            final String Street = getIntent().getStringExtra("street");

            if (Province.equalsIgnoreCase("Whole Country")) {
                // means no check on province  , i.e. whole country ..

                // so check if there is any check on price or not ..
                String PF=getIntent().getStringExtra("pricefilter");
                Toast.makeText(AdsActivity.this,PF,Toast.LENGTH_SHORT).show();

                if (!PF.equalsIgnoreCase("no")) {

                    // means there are min and max price parameters ..
                    MinPrice = getIntent().getIntExtra("min", 0);
                    MaxPrice = getIntent().getIntExtra("max", 0);


                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getChildrenCount() != 0) {

                                 modelAdsArrayList.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    //int price .... check price for prince range from to this ..
                                    if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                        int price = Integer.parseInt(ds.child("price").getValue().toString());
                                        if (price >= MinPrice && price <= MaxPrice) {
                                            mIsLoading=true;
                                            modelAds = new ModelAds();
                                            modelAds.setPrice(ds.child("price").getValue().toString());
                                            modelAds.setPicture(ds.child("image").getValue().toString());
                                            modelAds.setTitle(ds.child("title").getValue().toString());
                                            modelAds.setTime(ds.child("time").getValue().toString());
                                            modelAds.setUid(ds.child("Uid").getValue().toString());
                                            modelAds.setDetails(ds.child("details").getValue().toString());
                                            modelAdsArrayList.add(modelAds);
                                        }}
                                }

                            } else {
                                Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                            }

                            //  perform sorting if needed ..
                            adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                            adapterAds.notifyDataSetChanged();
                            RecyclerViewAds.setAdapter(adapterAds);
                            mIsLoading=false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    // no price filter ..
                    //so display ads from whole country ..
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getChildrenCount() != 0) {


                                 modelAdsArrayList.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                        mIsLoading=true;
                                        modelAds = new ModelAds();
                                        modelAds.setPrice(ds.child("price").getValue().toString());
                                        modelAds.setPicture(ds.child("image").getValue().toString());
                                        modelAds.setTitle(ds.child("title").getValue().toString());
                                        modelAds.setTime(ds.child("time").getValue().toString());
                                        modelAds.setUid(ds.child("Uid").getValue().toString());
                                        modelAds.setDetails(ds.child("details").getValue().toString());
                                        modelAdsArrayList.add(modelAds);
                                    }}
                            } else {
                                Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                            }

                            //  perform sorting if needed ..
                            adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                            adapterAds.notifyDataSetChanged();
                            RecyclerViewAds.setAdapter(adapterAds);
                            mIsLoading=false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
            else {

                // means some province is selected ..
                if (City.equalsIgnoreCase("Whole Province")) {
                    //  means no check on city ..
                    String PF=getIntent().getStringExtra("pricefilter");
                    if (!PF.equalsIgnoreCase("no")) {
                        PriceFilter = true;
                        // means there are min and max price parameters ..
                        MinPrice = getIntent().getIntExtra("min", 0);
                        MaxPrice = getIntent().getIntExtra("max", 0);


                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildrenCount() != 0) {

                                       modelAdsArrayList.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        //int price .... check price for prince range from to this ..
                                        if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                            mIsLoading=true;
                                            int price = Integer.parseInt(ds.child("price").getValue().toString());

                                            if ((price >= MinPrice && price <= MaxPrice) && ds.child("province").equals(Province)) {
                                                modelAds = new ModelAds();
                                                modelAds.setPrice(ds.child("price").getValue().toString());
                                                modelAds.setPicture(ds.child("image").getValue().toString());
                                                modelAds.setTitle(ds.child("title").getValue().toString());
                                                modelAds.setTime(ds.child("time").getValue().toString());
                                                modelAds.setUid(ds.child("Uid").getValue().toString());
                                                modelAds.setDetails(ds.child("details").getValue().toString());
                                                modelAdsArrayList.add(modelAds);
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                }

                                //  perform sorting if needed ..
                                adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                adapterAds.notifyDataSetChanged();
                                RecyclerViewAds.setAdapter(adapterAds);
                                mIsLoading=false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        // no price filter ..
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildrenCount() != 0)
                                {


                                     modelAdsArrayList.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                            if ((ds.child("province").equals(Province))) {
                                                mIsLoading=true;
                                                modelAds = new ModelAds();
                                                modelAds.setPrice(ds.child("price").getValue().toString());
                                                modelAds.setPicture(ds.child("image").getValue().toString());
                                                modelAds.setTitle(ds.child("title").getValue().toString());
                                                modelAds.setTime(ds.child("time").getValue().toString());
                                                modelAds.setUid(ds.child("Uid").getValue().toString());
                                                modelAds.setDetails(ds.child("details").getValue().toString());
                                                modelAdsArrayList.add(modelAds);
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                }

                                //  perform sorting if needed ..
                                adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                adapterAds.notifyDataSetChanged();
                                RecyclerViewAds.setAdapter(adapterAds);
                                mIsLoading=false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }


                else {
                    // means some city is also selected ..
                    if (Street.equalsIgnoreCase("Whole City"))
                    {
                        // means no street is specified ..
                        String PF=getIntent().getStringExtra("pricefilter");
                        if (!PF.equalsIgnoreCase("no")) {
                            // means there are min and max price parameters ..
                            MinPrice = getIntent().getIntExtra("min", 0);
                            MaxPrice = getIntent().getIntExtra("max", 0);


                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() != 0) {

                                         modelAdsArrayList.clear();

                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            //int price .... check price for prince range from to this ..
                                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                                int price = Integer.parseInt(ds.child("price").getValue().toString());
                                                mIsLoading=true;
                                                if ((price >= MinPrice && price <= MaxPrice) && (ds.child("city").equals(City)) && (ds.child("province").equals(Province))) {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                    }

                                    //  perform sorting if needed ..
                                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                    adapterAds.notifyDataSetChanged();
                                    RecyclerViewAds.setAdapter(adapterAds);
                                    mIsLoading=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // no price filter ..

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() != 0) {


                                         modelAdsArrayList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                                mIsLoading=true;
                                                if ((ds.child("city").equals(City)) && (ds.child("province").equals(Province))) {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);

                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                    }

                                    //  perform sorting if needed ..
                                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                    adapterAds.notifyDataSetChanged();
                                    RecyclerViewAds.setAdapter(adapterAds);
                                    mIsLoading=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                    else
                    {
                        // means some street is also selected ..
                        String PF=getIntent().getStringExtra("pricefilter");
                        if (!PF.equalsIgnoreCase("no")) {
                            // means there are min and max price parameters ..
                            MinPrice = getIntent().getIntExtra("min", 0);
                            MaxPrice = getIntent().getIntExtra("max", 0);


                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() != 0) {

                                         modelAdsArrayList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            //int price .... check price for prince range from to this ..
                                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {

                                                int price = Integer.parseInt(ds.child("price").getValue().toString());

                                                mIsLoading=true;
                                                if ((price >= MinPrice && price <= MaxPrice)
                                                        && (ds.child("city").equals(City))
                                                        && (ds.child("province").equals(Province))
                                                        && (ds.child("street").equals(Street))) {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                    }

                                    //  perform sorting if needed ..
                                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                    adapterAds.notifyDataSetChanged();
                                    RecyclerViewAds.setAdapter(adapterAds);
                                    mIsLoading=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else
                        {
                            // no price filter ..

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() != 0) {


                                          modelAdsArrayList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.child("subCategory").getValue().toString().equalsIgnoreCase(ReferenceCategory)) {
                                                mIsLoading=true;
                                                if ((ds.child("city").equals(City))
                                                        && (ds.child("province").equals(Province))
                                                        && (ds.child("street").equals(Street)))
                                                {
                                                    modelAds = new ModelAds();
                                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                                    modelAds.setTime(ds.child("time").getValue().toString());
                                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                                    modelAdsArrayList.add(modelAds);

                                                }}
                                        }
                                    } else {
                                        Toast.makeText(AdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                                    }

                                    //  perform sorting if needed ..
                                    adapterAds = new AdapterAds(AdsActivity.this, modelAdsArrayList);
                                    adapterAds.notifyDataSetChanged();
                                    RecyclerViewAds.setAdapter(adapterAds);
                                    mIsLoading=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }


                }

            }





        }



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            showSortDialogue();
            return true;
        }
        else if (id==R.id.action_filter)
        {
            Intent i=new Intent(AdsActivity.this,FilterActivity.class);
            i.putExtra("cat", ReferenceCategory);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSortDialogue()
    {
        String[] optionsItems = {"Oldest","Newest"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdsActivity.this);
        builder.setTitle("Sort By :").setItems(optionsItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    // oldest post first ..
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);
                    //recreate();
                }
                else if(which==1)
                {
                    // newest post first ..

                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                   // recreate();
                }
                }


            }
            );
        builder.create().show();
    }



}