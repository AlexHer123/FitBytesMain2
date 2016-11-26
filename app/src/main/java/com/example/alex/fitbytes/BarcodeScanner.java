package com.example.alex.fitbytes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);


        final SurfaceView cameraView = (SurfaceView)findViewById(R.id.camera_view);
        TextView barcodeInfo = (TextView)findViewById(R.id.code_info);

        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E)
                        .build();


        final CameraSource cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    }catch (IOException ie){
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }

                } catch (SecurityException se) {
                    Log.e("CAMERA SOURCE", se.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

//        if(!detector.isOperational()){
//            Toast.makeText( getApplicationContext(), "Could not open barcode scanner.", Toast.LENGTH_SHORT).show();
//            return;
//        }
    }

}
