package com.example.android.secretsanta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //called when the user taps the Get Started button
    public void takeNames(View view) {
        Intent intent = new Intent(this, ListLayout.class);
        startActivity(intent);
    }

    public void showInfo(View view) {
        Intent intent = new Intent(this, ExplainRules.class);
        startActivity(intent);
    }

}
