package com.gashe.programarservicio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.Calendar;

public class MyService extends Service {

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // BINDED SERVICE
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // si necesitase inicializar el servicio la primera vez que se ejecuta / invoca
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // STARTED SERVICE
        Log.d(getClass().getCanonicalName(), "Se ha ejecutado el servicio");
        //TODO código de lo que quieres hacer
        // llamar asynctask, leer data base, etc
        stopSelf(startId);

        return super.onStartCommand(intent, flags, startId);
    }

    private void programarAlarma(){
        // no hace falta hacer referencia a context pq SERVICE ya es context
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance(); // momento actual
        long actual_time = calendar.getTimeInMillis();
        long alarm_time = actual_time + +10000;

        Log.d(getClass().getCanonicalName(), "Tiempo actual: " + actual_time);
        Log.d(getClass().getCanonicalName(), "Tiempo alarma: " + alarm_time);

        //creamos el pending intent para cuando termine el servicio alarma y ejecute el receivers
        Intent intent_receiver = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent_receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_time, pendingIntent);

    }

    @Override
    public void onDestroy() {
        programarAlarma();
        super.onDestroy();
    }
}
