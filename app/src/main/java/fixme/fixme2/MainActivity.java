package fixme.fixme2;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String ZGŁOSZENIE1 = "Zgłoszono usterkę.\n\nMiejsce usterki: ";
    public static final String ZGŁOSZENIE2 = "\n\nOpis usterki: ";
    public static final String EXTRA_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, AlertService.class));

    }

    private void notyfikuj() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "dffs")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ewakuacja")
                .setContentText("Pan dziekan Strzelecki ogłasza EWAKUACJĘ!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

//        Notification.Builder mBuilder = new Notification.Builder(MainActivity.this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Ewakuacja")
//                .setContentText("Pan dziekan Strzelecki ogłasza EWAKUACJĘ!")
//                .setPriority(Notification.PRIORITY_HIGH);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(10, mBuilder.build());
    }

    public void sendMessage(View view) {
        notyfikuj();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String message = ZGŁOSZENIE1 + editText.getText().toString() + ZGŁOSZENIE2 + editText2.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        Tools.sendEmail(message);
        startActivity(intent);
    }
}
