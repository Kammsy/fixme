package fixme.fixme2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AlertService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        System.out.println("Stworzono usługę");
    }
    private void showNotification() {
        System.out.println("Próbuję pokazać powiadomienie");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "dffs")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ewakuacja")
                .setContentText("Pan dziekan Strzelecki ogłasza EWAKUACJĘ!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(10, mBuilder.build());
    }
    private void evacuation() {
       // return true;

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://students.mimuw.edu.pl:9000/api/hello";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: "+ response);
                        if(response.equals("uciekać"))
                            showNotification();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
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
