package com.example.ml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ml.image.FaceDetection;
import com.example.ml.image.FlowerClassification;
import com.example.ml.image.ImageClassification;


public class MainActivity extends AppCompatActivity {

    Button btnImageClassification,btnFlowerClassification,btnObjectDetection,btnFaceDetection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();


        btnImageClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToImageClassifcation();
            }
        });
        btnFlowerClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFlowerClassification();
            }
        });

        btnObjectDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToObjectDetection();
            }
        });

        btnFaceDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFaceDetection();
            }
        });
    }

    public void changeToImageClassifcation()
    {
        Intent i = new Intent(MainActivity.this, ImageClassification.class);
        startActivity(i);
    }
    public void changeToFlowerClassification()
    {
        Intent i = new Intent(MainActivity.this, FlowerClassification.class);
        startActivity(i);
    }
    public void changeToObjectDetection()
    {
        Intent i = new Intent(MainActivity.this, ObjectDetection.class);
        startActivity(i);
    }

    public void changeToFaceDetection()
    {
        Intent i = new Intent(MainActivity.this, FaceDetection.class);
        startActivity(i);
    }

    private void init()
    {

        btnImageClassification = findViewById(R.id.btnImageClassifcation);
        btnFlowerClassification= findViewById(R.id.btnFlowerClassfication);
        btnObjectDetection = findViewById(R.id.btnObjectDetction);
        btnFaceDetection = findViewById(R.id.btnFaceDetection);

    }
}