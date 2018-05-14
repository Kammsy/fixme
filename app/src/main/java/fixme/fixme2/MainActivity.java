package fixme.fixme2;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String ZGŁOSZENIE1 = "Zgłoszono usterkę.\n\nMiejsce usterki: ";
    public static final String ZGŁOSZENIE2 = "\n\nOpis usterki: ";
    public static final String EXTRA_MESSAGE = "";
    private String chosenTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, AlertService.class));

        Spinner spinner = (Spinner) findViewById(R.id.tags);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        chosenTag = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView img = (ImageView) findViewById(R.id.imageView2);
            img.setImageBitmap(imageBitmap);
            Log.d("dupa", "Zrobiono zdjęcie!!");
        }
    }
    public void sendMessage(View view) throws JSONException {
        notyfikuj();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String message = ZGŁOSZENIE1 + editText.getText().toString() + ZGŁOSZENIE2 + editText2.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        Tools.sendEmail(message);
        startActivity(intent);

        JSONObject obj = new JSONObject();
        obj.put("Miejsce", editText.getText().toString());
        obj.put("Opis", editText2.getText().toString());
        Log.d("Json", obj.toString());
    }
}



