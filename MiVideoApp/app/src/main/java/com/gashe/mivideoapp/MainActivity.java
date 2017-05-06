package com.gashe.mivideoapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Uri video_uri;
    File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        menu.add("SUBIR");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //toca botón subir
        Log.d(getClass().getCanonicalName(), "TOCA EL BOTÓN SUBIR");

        new SubirVideo().execute(video_uri);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        video_uri = crearFicheroVideo();

        if(video_uri != null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
            startActivityForResult(intent, 100);
        }else{
            Log.d(getClass().getCanonicalName(), "ERROR: ALGO NO FUE BIEN. INTENT NO LANZADO");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        Log.d(getClass().getCanonicalName(), "VOLVIÓ A GRABAR EL VIDEO");

        switch (resultCode){
            case RESULT_OK:

                video_uri = data.getData(); // recupero la uri del Intent
                VideoView videoView = (VideoView) findViewById(R.id.myVideo);

                // controles del videoView
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(mediaController);

                videoView.setMediaController(mediaController);
                videoView.setVideoURI(video_uri);
                videoView.start();

                break;
            case RESULT_CANCELED:
                Log.d(getClass().getCanonicalName(), "EL VIDEO NO SE HIZO");
                break;
            default: Log.d(getClass().getCanonicalName(), "EL VIDEO NO SE HIZO");
        }

    }


    // Mis funciones

    private Uri crearFicheroVideo(){
        Uri uri = null;

        String nombre_fichero = "video4.mp4";
        String ruta_completa_video = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + nombre_fichero;
        Log.d(getClass().getCanonicalName(), "RUTA VIDEO: " + ruta_completa_video);

        file = new File(ruta_completa_video);
        try{

            boolean creado = file.createNewFile();
            if(creado){
                uri = Uri.fromFile(file);
                Log.d(getClass().getCanonicalName(), "FICHERO CREADO CON ÉXITO");
                Log.e(getClass().getCanonicalName(), "URI VIDEO: " + uri.toString());
            }else{
                Log.d(getClass().getCanonicalName(), "FICHERO NO CREADO");
            }

        }catch (Exception e){
            Log.e(getClass().getCanonicalName(), "ERROR: ", e);
            e.printStackTrace();
        }



        return uri;
    }

}
