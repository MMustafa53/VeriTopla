package com.example.mmhus.veritopla;

import android.hardware.Camera;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    Button baslat, kaydet;
    TextView bilgitv;
    boolean zaman;
    private SurfaceView svYuzey;
    private SurfaceHolder sh;
    private android.hardware.Camera cmr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baslat = findViewById(R.id.baslat);
        bilgitv = findViewById(R.id.bilgitv);
        kaydet = findViewById(R.id.kaydet);
        svYuzey =findViewById(R.id.svYuzey);
        sh = svYuzey.getHolder();
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sh.setFixedSize(300,300);
        /*Toast.makeText(getApplicationContext(),"BASLADI",Toast.LENGTH_SHORT).show();

        zaman = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"BASIIIIILDI",Toast.LENGTH_SHORT).show();
                baslat.performClick();
            }
        },10000);*/
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            Toast.makeText(getApplicationContext(),"BASLADI",Toast.LENGTH_SHORT).show();

            cmr = Camera.open();
            cmr.setPreviewDisplay(sh);
            cmr.setDisplayOrientation(90);
            cmr.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        cmr.stopPreview();
        cmr.release();
    }
}
