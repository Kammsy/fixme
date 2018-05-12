package fixme.fixme2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Tools {
    public static void sendEmail(final String message) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("usuwacz.awarii@onet.pl",
                            "mimuwFTW123");
                    sender.sendMail("Zgłoszenie o awarii", message,
                            "usuwacz.awarii@onet.pl", "sz383558@students.mimuw.edu.pl");
                    //lk385775
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();

    }
}