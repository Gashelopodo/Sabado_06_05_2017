package com.gashe.mivideoapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cice on 6/5/17.
 */

public class SubirVideo extends AsyncTask<Uri, Void, String> {

    @Override
    protected String doInBackground(Uri... params) {

        URL serverUrl = null;
        HttpURLConnection httpURLConnection = null;

        try{

            serverUrl = new URL("http://192.168.3.68:8080/SubirVideo/UploadVideo?fichero=videorl");
            httpURLConnection = (HttpURLConnection) serverUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");

            File file = new File(params[0].getPath());
            FileInputStream fileInputStream = new FileInputStream(file);

            byte [] videoBloque = new byte[1024*4];

            OutputStream outputStream = httpURLConnection.getOutputStream();

            int escritos = 0;

            while((escritos = fileInputStream.read(videoBloque)) != -1){
                outputStream.write(videoBloque, 0, escritos);
            }

            int cod_resp = httpURLConnection.getResponseCode(); // si no se lanza el responseCode no se lanza la petición.

            fileInputStream.close();
            outputStream.close();

            Log.d(getClass().getCanonicalName(), ""+cod_resp);

        }catch (Exception e){
            Log.e(getClass().getCanonicalName(), "Error;", e);
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }


        return "OK";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
