package com.aptitude.challengetwo;

import android.content.Intent;
import android.os.Bundle;

import com.aptitude.challengetwo.adapters.ProductsAdapter;
import com.aptitude.challengetwo.datamodel.PostModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProductsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayout progresslayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            }
        });
        recyclerView = findViewById(R.id.rv_product);
        progresslayout  = findViewById(R.id.progresslayout);
        adapter = new ProductsAdapter(MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        progresslayout.setVisibility(View.VISIBLE);
        super.onResume();
        FirebaseDatabase.getInstance().getReference("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progresslayout.setVisibility(View.GONE);
                if (dataSnapshot.getChildrenCount()>=1){
                    ArrayList<PostModel> objectArrayList = new ArrayList<PostModel>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        PostModel postModel = dataSnapshot1.getValue(PostModel.class);
                        Log.d("Count " ,"Data Count "+postModel.toString());
                        objectArrayList.add(postModel);
                    }
                    Log.d("Count " ,"Data Count new "+objectArrayList.toString());
                    adapter.addItems(objectArrayList);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Unable to retrieve data", Toast.LENGTH_LONG).show();
                progresslayout.setVisibility(View.GONE);
                Log.d("DatabaseError " ,"DatabaseError error "+databaseError.getMessage());
                databaseError.toException().printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
