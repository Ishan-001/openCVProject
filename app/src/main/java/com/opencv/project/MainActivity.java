package com.opencv.project;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

    Button select, filter;
    ImageView img;
    Bitmap styleBitmap, imageBitmap;
    AlertDialog.Builder builder;

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

        filter=findViewById(R.id.button2);
        //createDialogBuilder();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBitmap!=null) {
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose a filter");
                    String[] items = {"None", "Style 1", "Style 2", "Style 3", "Style 4", "Style 5", "Style 6", "Style 7", "Style 8"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setStyle(which, imageBitmap);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                    Toast.makeText(MainActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    void setStyle(int position, Bitmap imageBitmap){
        Mat rgba=new Mat();
        Mat styleMat=new Mat();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inDither=false;
        options.inSampleSize=4;

        int width=imageBitmap.getWidth();
        int height=imageBitmap.getHeight();
        styleBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        Utils.bitmapToMat(imageBitmap, rgba);

        switch (position){
            case 0: styleMat=rgba;
                    break;
            case 1: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGB2YUV);
                break;
            case 2: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGB2GRAY);
                break;
            case 3: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGB2BGR);
                break;
            case 4: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_BGR2XYZ);
                break;
            case 5: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGB2YCrCb);
                break;
            case 6: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGBA2BGRA);
                break;
            case 7: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGB2Lab);
                break;
            case 8: Imgproc.cvtColor(rgba, styleMat, Imgproc.COLOR_RGB2Luv);
                break;

        }

        Utils.matToBitmap(styleMat, styleBitmap);

        img.setImageBitmap(styleBitmap);
    }
}
