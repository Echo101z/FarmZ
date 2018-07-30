package com.farmz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Aboutme extends AppCompatActivity {
EditText ename,phno,addr,quer;
    DatabaseReference databasefarmer;
 Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);
ename=(EditText)findViewById(R.id.textname);
phno=(EditText)findViewById(R.id.phone);
        addr=(EditText)findViewById(R.id.address);
        quer=(EditText)findViewById(R.id.query);
save=(Button)findViewById(R.id.save);

        databasefarmer= FirebaseDatabase.getInstance().getReference("farmer");




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addfarm();
            }
        });}



        private void addfarm() {
            String name = ename.getText().toString().trim();
            String ph = phno.getText().toString().trim();
            String ad = addr.getText().toString().trim();
            String que = quer.getText().toString().trim();

            if (!TextUtils.isEmpty(name)) {


                String id = databasefarmer.push().getKey();
                farmer fr = new farmer(name,id,ph,que,ad);
                databasefarmer.child(id).setValue(fr);
                Toast.makeText(this, "Farmer added", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(this, "you should enter all fields", Toast.LENGTH_SHORT).show();
            }






    }



    }

