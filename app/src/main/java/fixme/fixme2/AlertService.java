package fixme.fixme2;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AlertService extends Service {
    private String bylo = "";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {

    }
    private void showNotification() {
        System.out.println("Próbuję pokazać powiadomienie");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "dffs")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ewakuacja")
                .setContentText("Ogłoszona została EWAKUACJA!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        mBuilder.setSound(Settings.System.DEFAULT_ALARM_ALERT_URI);
        //mBuilder.setVibrate(Settings.System)
// notificationId is a unique int for each notification that you must define
        Notification not = mBuilder.build();
        //not.defaults |= Notification.DEFAULT;
        //not.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(10, not);
    }
    private void evacuation() {
       // return true;

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.API_ALERT;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("notyfikacje","Response is: "+ response);
                        if(!response.equals("no evacuation\n") && !response.equals(bylo)) {
                            bylo = response;
                            showNotification();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Nie ma połączenia z internetem!");
                Context context = getApplicationContext();
                CharSequence text = "Błąd! Sprawdź połączenie z internetem!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        (new Thread() {
            @Override
            public void run(){
                while(true) {
                    try {
                        Thread.sleep(5000);
                    } catch(Exception e) {
                        System.out.println("Coś się zepsuło");
                    }
                    evacuation();
                }
            }
        }).start();

        return START_STICKY;
    }
}
