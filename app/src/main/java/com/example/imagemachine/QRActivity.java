package com.example.imagemachine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        textView = (TextView) findViewById(R.id.tvResult);
        surfaceView = (SurfaceView) findViewById(R.id.svQR);

        initDetection();
    }

    //initiate the barcode detector and camera
    private void initDetection() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            //Start the camera if the permission as allowed
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return;
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            //Stops the camera when surface is closed
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            //Handles when qr code is scanned and contains 1 of the barcode machine in database
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if(qrCodes.size() != 0){
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);

                    Intent intent = new Intent(QRActivity.this, EditActivity.class);

                    MachineDB db = MachineDB.getInstance(QRActivity.this);

                    Machine machine = db.getMachine(Integer.parseInt(qrCodes.valueAt(0).displayValue));

                    Bundle bundle = new Bundle();

                    bundle.putInt("machineID", machine.ID);
                    bundle.putString("machineName", machine.name);
                    bundle.putString("machineType", machine.type);
                    bundle.putInt("machineQR", machine.qr);
                    bundle.putString("machineDate", machine.date);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    //stop the camera
    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    //start the barcode detection and camera
    @Override
    protected void onResume() {
        super.onResume();
        initDetection();
    }
}
