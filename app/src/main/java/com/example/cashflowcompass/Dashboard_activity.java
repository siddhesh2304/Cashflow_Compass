package com.example.cashflowcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Dashboard_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void createtransaction(View view){
        Intent intent = new Intent(this, TransactionsActivity.class);
        startActivity(intent);
    }

}