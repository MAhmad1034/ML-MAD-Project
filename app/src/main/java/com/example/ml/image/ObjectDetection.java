package com.example.ml.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.ml.helper.BoxWithLabel;
import com.example.ml.helper.ImageHelperActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class ObjectDetection extends ImageHelperActivity {

    private ObjectDetector objectDetector;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //multiple object detection in static images
        ObjectDetectorOptions options =
                new ObjectDetectorOptions
                        .Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .build();
        objectDetector = com.google.mlkit.vision.objects.ObjectDetection.getClient(options);
    }


    @Override
    protected  void runClassification(Bitmap bitmap)
    {
        InputImage inputImage = InputImage.fromBitmap(bitmap,0);
        objectDetector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
                    @Override
                    public void onSuccess(List<DetectedObject> detectedObjects) {
                        if(!detectedObjects.isEmpty()){
                            StringBuilder builder = new StringBuilder();
                            List<BoxWithLabel> boxes = new ArrayList<>();
                            for(DetectedObject object:detectedObjects){
                                if(!object.getLabels().isEmpty())
                                {

                                    String label = object.getLabels().get(0).getText();
                                    builder.append(label).append(" : ")
                                            .append((object.getLabels().get(0).getConfidence()))
                                            .append("\n");


                                    boxes.add(new BoxWithLabel(object.getBoundingBox(),label));
                                    Log.d("Object Detetion","Object Detected");

                                }
                                else{
                                    builder.append("Unknown").append("\n");
                                }

                            }

                            getOutputTextView().setText(builder.toString());
                            drawDetectionResult(boxes,bitmap);

                        }
                        else{
                            getOutputTextView().setText("Unable to Detect Objects");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
