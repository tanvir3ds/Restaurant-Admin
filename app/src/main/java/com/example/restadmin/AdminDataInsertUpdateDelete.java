package com.example.restadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AdminDataInsertUpdateDelete extends AppCompatActivity {

    private RelativeLayout AddFood, ShowFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Admin ");
        setContentView(R.layout.activity_admin_data_insert_update_delete);


        //AdminAddLayout_ID

        AddFood=findViewById(R.id.AdminAddLayout_ID);
        AddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), AdminFoodAddActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"i m pizza activity", Toast.LENGTH_SHORT).show();
            }
        });

        //AdminShowLayout_ID
        ShowFood=findViewById(R.id.AdminShowLayout_ID);
        ShowFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), AdminShowFood.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"i m pizza activity", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
