package com.example.ml.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.ml.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImageHelperActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1000;
    private static final int REQUEST_CAPTURE_IMAGE = 1001;
    private ImageView ivInput;
    private TextView tvOutput;
    private Button btnPickImage, btnOpenCamera;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_helper);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        init();

        if (btnPickImage != null) {
            btnPickImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickImage(v);
                }
            });
        }

        if (btnOpenCamera != null) {
            btnOpenCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartCamera(v);
                }
            });
        }
    }

    private void init() {
        ivInput = findViewById(R.id.ivInput);
        tvOutput = findViewById(R.id.tvOutput);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);

        if (ivInput == null) {
            Log.e("ImageHelperActivity", "ivInput is not initialized.");
        } else {
            Log.d("ImageHelperActivity", "ivInput is initialized.");
        }
        if (tvOutput == null) {
            Log.e("ImageHelperActivity", "tvOutput is not initialized.");
        } else {
            Log.d("ImageHelperActivity", "tvOutput is initialized.");
        }
        if (btnPickImage == null) {
            Log.e("ImageHelperActivity", "btnPickImage is not initialized.");
        } else {
            Log.d("ImageHelperActivity", "btnPickImage is initialized.");
        }
        if (btnOpenCamera == null) {
            Log.e("ImageHelperActivity", "btnOpenCamera is not initialized.");
        } else {
            Log.d("ImageHelperActivity", "btnOpenCamera is initialized.");
        }
    }

    private void pickImage(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }

    public void onStartCamera(View view) {
        // Create a file to share with camera
        photoFile = createPhotoFile();

        Uri fileUri = FileProvider.getUriForFile(this, "com.example.ml.fileprovider", photoFile);

        // Create an intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // Start activity for result
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }

    private File createPhotoFile() {
        // If API >= 24 can't share any URI; needed permissions
        File photoFileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ML_IMAGE_HELPER");

        if (!photoFileDir.exists()) {
            photoFileDir.mkdirs();
        }
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "JPEG_" + timeStamp + "_";
        return new File(photoFileDir.getPath() + File.separator + imageName + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Bitmap bitmap = loadFromUri(uri);
                        ivInput.setImageBitmap(bitmap);
                        runClassification(bitmap);
                    } else {
                        Log.e("ImageHelperActivity", "Uri is null");
                    }
                } else {
                    Log.e("ImageHelperActivity", "Intent data is null");
                }
            } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
                Log.e("ML", "Received Callback from camera");
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivInput.setImageBitmap(bitmap);
                runClassification(bitmap);
            }
        }
    }

    protected Bitmap loadFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(ImageHelperActivity.class.getSimpleName(), "Grant result for " + permissions[0] + " is " + grantResults[0]);
    }

    protected void runClassification(Bitmap bitmap) {
        // This method should be overridden by subclasses
    }

    protected TextView getOutputTextView() {
        return tvOutput;
    }

    protected ImageView getInputImageView() {
        return ivInput;
    }

    protected void drawDetectionResult(List<BoxWithLabel> boxes,Bitmap bitmap){
        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(outputBitmap);
        Paint penRec = new Paint();
        penRec.setColor(Color.RED);
        penRec.setStyle(Paint.Style.STROKE);
        penRec.setStrokeWidth(8f);

        Paint penLabel = new Paint();
        penLabel.setColor(Color.YELLOW);
        penLabel.setStyle(Paint.Style.FILL_AND_STROKE);
        penLabel.setTextSize(96f);

        for(BoxWithLabel boxWithLabel : boxes ){
            canvas.drawRect(boxWithLabel.rect,penRec);

            //Rect
            Rect labelSize = new Rect(0,0,0,0);
            penLabel.getTextBounds(boxWithLabel.label,0,boxWithLabel.label.length(),labelSize);

            float fontSize = penLabel.getTextSize() * boxWithLabel.rect.width() /labelSize.width();
            if(fontSize<penLabel.getTextSize())
            {
                canvas.drawText(boxWithLabel.label, boxWithLabel.rect.left,boxWithLabel.rect.top+ labelSize.height(),penLabel);
            }
            getInputImageView().setImageBitmap(outputBitmap);

        }

    }
}
