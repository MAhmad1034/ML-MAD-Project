package com.example.ml.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ml.helper.ImageHelperActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

public class ImageClassification extends ImageHelperActivity {
    private ImageLabeler imageLabeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeImageLabeler();
    }

    private void initializeImageLabeler() {
        try {
            imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions
                    .Builder()
                    .setConfidenceThreshold(0.7f)
                    .build());
            Log.d("ImageClassification", "ImageLabeler initialized successfully.");
        } catch (Exception e) {
            Log.e("ImageClassification", "Error initializing ImageLabeler: ", e);
        }
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        if (imageLabeler == null) {
            Log.e("ImageClassification", "ImageLabeler is not initialized.");
            initializeImageLabeler();
            if (imageLabeler == null) {
                Toast.makeText(this, "Failed to initialize ImageLabeler", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage)
                .addOnSuccessListener(imageLabels -> {
                    if (!imageLabels.isEmpty()) {
                        StringBuilder builder = new StringBuilder();
                        for (ImageLabel label : imageLabels) {
                            builder.append(label.getText())
                                    .append(" : ")
                                    .append(label.getConfidence())
                                    .append("\n");
                        }
                        getOutputTextView().setText(builder);
                    } else {
                        getOutputTextView().setText("Could not classify");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ImageClassification", "Image classification failed: ", e);
                    e.printStackTrace();
                });
    }
}
