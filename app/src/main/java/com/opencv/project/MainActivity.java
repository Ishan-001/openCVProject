package com.opencv.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {

    Button select, gray;
    ImageView img;
    Bitmap grayBitmap, imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=findViewById(R.id.imageView);

        OpenCVLoader.initDebug();

        select=findViewById(R.id.button);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        gray=findViewById(R.id.button2);
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToGray();
            }
        });
    }

    void openGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK && data!=null){
            Uri imageURI=data.getData();

            try {
                imageBitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }

            img.setImageBitmap(imageBitmap);
        }
    }

    void convertToGray(){
        Mat rgba=new Mat();
        Mat grayMat=new Mat();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inDither=false;
        options.inSampleSize=4;

        int width=imageBitmap.getWidth();
        int height=imageBitmap.getHeight();

        grayBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        Utils.bitmapToMat(imageBitmap, rgba);
        Imgproc.cvtColor(rgba, grayMat, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(grayMat, grayBitmap);

        img.setImageBitmap(grayBitmap);
    }
}
