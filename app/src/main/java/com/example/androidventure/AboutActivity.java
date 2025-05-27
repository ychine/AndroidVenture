package com.example.androidventure;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        buttonBack = findViewById(R.id.buttonBack);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(
                "<b>AndroidVenture</b> is an exciting mobile application that combines adventure and learning. " +
                        "It provides an interactive platform for users to explore various features and functionalities " +
                        "while learning about Android development concepts in a fun and engaging way."
        ));


        TextView creatorTextView = findViewById(R.id.creatorTextView);
        creatorTextView.setText("Created by: Benitez Richelle, Martinez Ed, Santos Milan for COMP 106 Case Study");


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }



}