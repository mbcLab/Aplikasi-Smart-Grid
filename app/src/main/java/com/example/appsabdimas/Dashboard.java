package com.example.appsabdimas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Dashboard extends AppCompatActivity {

    SwitchCompat switchCompat;
    ImageView imageView;

    private static String MY_PREFS = "switch_prefs";
    private static String LIGHT_STATUS = "Light On";
    private static String SWITCH_STATUS = "Switch Status";

    boolean switch_status;
    boolean light_status;

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    private HelperAdapter adapter;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        reff = FirebaseDatabase.getInstance().getReference("output");

        switchCompat = findViewById(R.id.switch_compat);
        imageView = findViewById(R.id.imageView);

        myPreferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        myEditor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        switch_status = myPreferences.getBoolean(SWITCH_STATUS, false);
        light_status = myPreferences.getBoolean(LIGHT_STATUS, false);

        if (light_status) {
            imageView.setImageResource(R.drawable.lighton);
        }
        switchCompat.setChecked(switch_status);
        if (light_status) {
            imageView.setImageResource(R.drawable.lighton);

        } else {
            imageView.setImageResource(R.drawable.lightoff);

        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    imageView.setImageResource(R.drawable.lighton);
                    myEditor.putBoolean(SWITCH_STATUS, true);
                    myEditor.putBoolean(LIGHT_STATUS, true);
                    myEditor.apply();
                    switchCompat.setChecked(true);
                    reff.child("saklar").setValue("ON");
                } else {
                    imageView.setImageResource(R.drawable.lightoff);
                    myEditor.putBoolean(SWITCH_STATUS, false);
                    myEditor.putBoolean(LIGHT_STATUS, false);
                    myEditor.apply();
                    switchCompat.setChecked(false);
                    reff.child("saklar").setValue("OFF");
                    reff.child("daya").setValue("0 Watt");
                    reff.child("arus").setValue("0 A");
                    reff.child("tegangan").setValue("0 V");
                }
            }
        });

        RecyclerView rv = findViewById(R.id.recyclerView);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase;

        FirebaseRecyclerOptions option = new FirebaseRecyclerOptions.Builder<ListData>()
                .setQuery(query, ListData.class)
                .build();

        adapter = new HelperAdapter(option);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}




