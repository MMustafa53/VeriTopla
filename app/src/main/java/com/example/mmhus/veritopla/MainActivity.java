package com.example.mmhus.veritopla;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback ,SensorEventListener {

    Button baslat, kaydet;
    TextView bilgitv;
    boolean zaman;
    private SurfaceView svYuzey;
    private SurfaceHolder sh;
    private Camera cmr;
    MediaRecorder mr;
    private boolean isRecording = false;

    public static final int MEDIA_TYPE_VIDEO =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baslat = findViewById(R.id.baslat);
        kaydet = findViewById(R.id.kaydet);
        svYuzey =findViewById(R.id.svYuzey);
        sh = svYuzey.getHolder();
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cmr = getCameraInstance();
        //sh.setFixedSize(300,300);
        /*Toast.makeText(getApplicationContext(),"BASLADI",Toast.LENGTH_SHORT).show();

        zaman = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"BASIIIIILDI",Toast.LENGTH_SHORT).show();
                baslat.performClick();
            }
        },10000);*/
    }

    public void setBaslat(View V) {

    }

    public void setKaydet(View v){
        if(isRecording){
            mr.stop();
            releaseMediaRecorder();
            cmr.lock();
            Toast.makeText(getApplicationContext(),"Video Kayıt Durduruldu",Toast.LENGTH_LONG).show();
            kaydet.setText(R.string.kaydetbtn);
            isRecording = false;
        }
        else{
            if(prepareVideoRecorder()){
                mr.start();
                Toast.makeText(getApplicationContext(),"Video Kayıt Başladı",Toast.LENGTH_LONG).show();
                kaydet.setText("DURDUR");
                isRecording= true;
            }
            else{
                releaseMediaRecorder();
            }
        }
    }

    private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),"Video Kayıt");
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.e("dosyaa","Dosya olultutlamadı");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath()+File.separator+"VID"+timeStamp+".mp4");
        return mediaFile;
    }

    private void releaseMediaRecorder() {
        if(mr != null){
            mr.reset();
            mr.release();
            mr = null;
            cmr.lock();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }

    private void releaseCamera(){
        if(cmr!=null){
            cmr.release();
            cmr = null;
        }
    }

    public static  Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }catch (Exception e){

        }
        return c;
    }

    private boolean prepareVideoRecorder(){
        mr = new MediaRecorder();
        cmr.unlock();
        mr.setCamera(cmr);
        mr.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mr.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mr.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//        mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mr.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
        mr.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        mr.setPreviewDisplay(svYuzey.getHolder().getSurface());
        try {
            mr.prepare();
        }catch (IllegalStateException e){
            Log.e("asd",e.getMessage());
            releaseMediaRecorder();
            return false;

        }catch (IOException e){
            Log.e("IOOO",e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {

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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
