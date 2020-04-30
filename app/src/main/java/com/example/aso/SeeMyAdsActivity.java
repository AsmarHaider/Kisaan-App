package com.example.aso;

import android.content.Intent;
import android.os.Bundle;

import com.chootdev.recycleclick.RecycleClick;
import com.example.aso.Adapter.AdapterAds;
import com.example.aso.Model.ModelAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

public class SeeMyAdsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AdapterAds adapterAds;
    RecyclerView RecyclerViewAds;
    ArrayList<ModelAds> modelAdsArrayList;
    ModelAds modelAds;
    String UserID="";
    private int mPostsPerPage=4;
    private boolean mIsLoading=false;
    private int mTotalItemCount;
    private int mLastVisibleItemPosition;
    LinearLayoutManager linearLayoutManager;
    boolean check=false;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_my_ads);
        UserID=getIntent().getStringExtra("UID");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("DSM").child("PostedAds");
        modelAdsArrayList = new ArrayList<>();
        modelAdsArrayList.clear();
        RecyclerViewAds = findViewById(R.id.recyclerviewseeads);
        RecyclerViewAds.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(SeeMyAdsActivity.this);
        RecyclerViewAds.setLayoutManager(linearLayoutManager);
        RecyclerViewAds.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && (mTotalItemCount <= (mLastVisibleItemPosition
                        + mPostsPerPage))) {
                    getData(adapterAds.getLastItemId());
                    mIsLoading = true;
                }
            }
        });

        getData(null);


        RecycleClick.addTo(RecyclerViewAds).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(SeeMyAdsActivity.this, AdDetails.class);
                i.putExtra("uid", UserID);
                i.putExtra("price",modelAdsArrayList.get(position).getPrice());
                i.putExtra("title",modelAdsArrayList.get(position).getTitle());
                i.putExtra("time",modelAdsArrayList.get(position).getTime());
                i.putExtra("image",modelAdsArrayList.get(position).getPicture());
                i.putExtra("details",modelAdsArrayList.get(position).getDetails());

                startActivity(i);
            }



        });

        RecycleClick.addTo(RecyclerViewAds).setOnItemLongClickListener(new RecycleClick.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                // YOUR CODE
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    void getData(final String nodeId)
    {
        Query query;

        if (nodeId == null)
        {

            query =databaseReference
                    .orderByKey()
                    .limitToFirst(mPostsPerPage);}
        else {

            query = databaseReference
                    .orderByKey()
                    .startAt(nodeId)
                    .limitToFirst(mPostsPerPage);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (nodeId != null) {

                    // means it is scrolled ..
                    if (dataSnapshot.getChildrenCount() == 1) {

                        check = true;

                    }else {
                        check=false;
                    }

                }

                if (!check) {

                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if ((ds.getKey()).equalsIgnoreCase(nodeId))
                            {
                            }
                            else {
                                mIsLoading=true;
                                modelAds = new ModelAds();
                                if ((ds.child("Uid").getValue().toString()).equalsIgnoreCase(UserID)) {
                                    modelAds.setPicture(ds.child("image").getValue().toString());
                                    modelAds.setTitle(ds.child("title").getValue().toString());
                                    modelAds.setTime(ds.child("time").getValue().toString());
                                    modelAds.setPrice(ds.child("price").getValue().toString());
                                    modelAds.setUid(ds.child("Uid").getValue().toString());
                                    modelAds.setDetails(ds.child("details").getValue().toString());
                                    modelAds.setAdId(ds.child("adId").getValue().toString());
                                    modelAdsArrayList.add(modelAds);
                                }
                            }
                        }
                    } else if (modelAdsArrayList.isEmpty()) {
                        Toast.makeText(SeeMyAdsActivity.this, "Nothing yet :(", Toast.LENGTH_SHORT).show();
                    }

                    adapterAds = new AdapterAds(SeeMyAdsActivity.this, modelAdsArrayList);
                    adapterAds.notifyDataSetChanged();
                    RecyclerViewAds.setAdapter(adapterAds);
                    mIsLoading=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.see_my_ads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
