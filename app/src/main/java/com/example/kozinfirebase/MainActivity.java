package com.example.kozinfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edName;
    EditText edYear;
    EditText edCountry;
    DatabaseReference mDatabase;
    String sk = "Sport";
    ArrayList<String> st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        mDatabase = FirebaseDatabase.getInstance().getReference(sk);
        edName = findViewById(R.id.edName);
        edYear = findViewById(R.id.edYear);
        edCountry = findViewById(R.id.edCountry);
        st = new ArrayList<String>(){};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, st);
        listView1.setAdapter(arrayAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = (String) ds.child("name").getValue();
                    String year = (String) ds.child("year").getValue();
                    String country = (String) ds.child("country").getValue();
                    st.add(name + " появился в " + year + " году, в стране " + country);
                }
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Ошибка :(", Toast.LENGTH_LONG).show();
            }

        });

    }

    public void onClickSave(View view) {
        String name = edName.getText().toString();
        String year = edYear.getText().toString();
        String country = edCountry.getText().toString();
        Toast.makeText(MainActivity.this, "Данные добавлены!", Toast.LENGTH_LONG).show();
        st.add(name + " появился в " + year + " году, в стране " + country);
        Sport sport = new Sport(name, year, country);
        mDatabase.push().setValue(sport);
    }
}