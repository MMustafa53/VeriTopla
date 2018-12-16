package com.example.mmhus.veritopla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class girisEkrani extends AppCompatActivity {

    TextView tdate;
    String testAdi,timeStamp;
    Button baslat;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);
        tdate = findViewById(R.id.saattv);
        baslat = findViewById(R.id.testBaslabtn);
        et = findViewById(R.id.testadiet);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh-mm-ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    public void setTestBasla(View V){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
        }
        testAdi = et.getText().toString();
        testAdi += "//";
        timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date());
        testAdi += timeStamp;
        File klasor = new File(Environment.getExternalStoragePublicDirectory("Veri Topla"), testAdi);
        if (!klasor.exists()) {
            if (!klasor.mkdirs()) {
                Log.e("dosyaa", "Dosya oluluşturulamadı");
                }
            else
            {
                Log.e("dosya","mkdir var");
            }
        }
        else{
                Log.e("dosya","exists var");
                Toast.makeText(getApplicationContext(),"Bu isimde bir test kalsörü bulunmakta",Toast.LENGTH_LONG).show();
            }

        Intent aktivite = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(aktivite);
        finish();

    }
}
