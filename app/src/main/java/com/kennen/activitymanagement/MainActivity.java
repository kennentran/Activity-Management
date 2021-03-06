package com.kennen.activitymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    List<Activity> activityList = null;
    RecyclerView recyclerView;
    FloatingActionButton addActivity;
    static FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();
    static DatabaseReference myReference = myDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        recyclerView = (RecyclerView) findViewById(R.id.rv_activities);  //initialize RecyclerView

        myReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                activityList = new ArrayList<>();
                activityList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())    //get all child
                {
                    activityList.add(data.getValue(Activity.class));
                    Log.e("DB log", "get data complete");
                }
                RVAdapter rvAdapter = new RVAdapter(MainActivity.this, activityList);    //initialize Adapter
                recyclerView.setAdapter(rvAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));   //set Layout Manager
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e("DB log", databaseError.getMessage());
            }
        });

        addActivity = (FloatingActionButton) findViewById(R.id.fab_addActivities);  //set click on Floating Action Button
        addActivity.setOnClickListener(v ->
        {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }
}
