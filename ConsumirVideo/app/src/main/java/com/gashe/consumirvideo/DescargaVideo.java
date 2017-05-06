package com.gashe.consumirvideo;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cice on 6/5/17.
 */

public class DescargaVideo extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {

        URL url  = null;
        HttpURLConnection httpURLConnection = null;
        String ruta_video = null;

        try{

            url = new URL("http://192.168.3.68:8080/SubirVideo/UploadVideo?fichero=videorl");
            httpURLConnection = (HttpURLConnection)url.openConnection();

            if(httpURLConnection.getResponseCode() == 200){
                InputStream inputStream = httpURLConnection.getInputStream();
                byte[] bloque_lectura = new byte [1024 * 5];
                File file_destino = crearFicheroVideo();
                FileOutputStream fileOutputStream = new FileOutputStream(file_destino);
                ruta_video = file_destino.getPath();

                int bleidos = -1;

                while((bleidos = inputStream.read(bloque_lectura)) != -1){
                    fileOutputStream.write(bloque_lectura, 0, bleidos);
                }

                inputStream.close();
                fileOutputStream.close();
                httpURLConnection.disconnect();

            }

        }catch (Exception e){
            Log.e(getClass().getCanonicalName(), "ERROR", e);
        }


        return ruta_video;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(getClass().getCanonicalName(), "VIDEO GUARDADO EN " + s);
        super.onPostExecute(s);
    }

    // Mis funciones

    private File crearFicheroVideo(){

        File file = null;

        String ruta_video = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/vdescarga.mp4";
        file = new File(ruta_video);

        try{
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }


        return file;

    }

}
