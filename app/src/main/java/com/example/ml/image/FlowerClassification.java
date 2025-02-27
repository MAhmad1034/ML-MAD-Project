package com.example.ml.image;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ml.helper.ImageHelperActivity;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

public class FlowerClassification extends ImageClassification {
    private ImageLabeler imageLabeler;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LocalModel localModel = new LocalModel.Builder().setAssetFilePath("model_flowers.tflite").build();
        CustomImageLabelerOptions options = new CustomImageLabelerOptions.Builder(localModel).
                setConfidenceThreshold(0.7f)
                .setMaxResultCount(5)
                .build();
        imageLabeler = ImageLabeling.getClient(options);
    }


    @Override
    protected  void runClassification(Bitmap bitmap)
    {

        if (imageLabeler == null) {
            Log.e("ImageHelperActivity", "ImageLabeler is not initialized.");
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
                    Log.e("ImageHelperActivity", "Image classification failed: ", e);
                    e.printStackTrace();
                });
    }
}
