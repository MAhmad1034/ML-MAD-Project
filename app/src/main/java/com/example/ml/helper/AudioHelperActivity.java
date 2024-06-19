package com.example.ml.helper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ml.R;

public class AudioHelperActivity extends AppCompatActivity {

    protected TextView tvOutputRecording,tvAudioSpecs;
    protected Button btnStartRecording,btnStopRecording;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_audio_helper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }
        btnStartRecording = findViewById(R.id.btnStartRecording);
        btnStopRecording = findViewById(R.id.btnStopRecording);
        tvOutputRecording = findViewById(R.id.tvOuptutRecording);
        tvAudioSpecs = findViewById(R.id.tvAudioSpecs);
        btnStopRecording.setEnabled(false);
        btnStartRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(v);
            }
        });
        btnStopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording(v);
            }
        });
    }
    public void startRecording(View view)
    {
        btnStartRecording.setEnabled(false);
        btnStopRecording.setEnabled(true);
    }

    public void stopRecording(View view)
    {
        btnStartRecording.setEnabled(true);
        btnStopRecording.setEnabled(false);
    }
}