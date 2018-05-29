package fixme.fixme2;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GetResultable {

    public static final String ZGŁOSZENIE1 = "Zgłoszono usterkę.\n\nMiejsce usterki: ";
    public static final String ZGŁOSZENIE2 = "\n\nOpis usterki: ";
    public static final String EXTRA_MESSAGE = "";
    private String chosenTag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = findViewById(R.id.tags);
        spinner.setOnItemSelectedListener(this);

        setTitle("Wyślij zgłoszenie");

        new GETAsyncTask(this).execute(Constant.API_TAGS);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        System.out.println("Wybieram item");
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

        notificationManager.notify(10, mBuilder.build());
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void ustawTagi(String[] tablica) {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tablica);
        Spinner spinner = findViewById(R.id.tags);
        spinner.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Uzyskano jakiś wynik");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView img = (ImageView) findViewById(R.id.imageView2);
            img.setImageBitmap(imageBitmap);
            System.out.println("Zrobiono zdjęcie");
        }
    }
    public void sendMessage(View view) throws JSONException {
        notyfikuj();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String message = ZGŁOSZENIE1 + editText.getText().toString() + ZGŁOSZENIE2 + editText2.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        //Tools.sendEmail(message);

        Map<String, String> map = new HashMap<>();
        map.put("title", "DUPADUPA");
        map.put("description", editText2.getText().toString());
        map.put("location", editText.getText().toString());
        map.put("tags[]", chosenTag);

        new Thread(new SendPOST(Constant.API_SEND_REPORT, map)).start();

        //startActivity(intent);
        finish();

/*
        JSONObject obj = new JSONObject();
        obj.put("Miejsce", editText.getText().toString());
        obj.put("Opis", editText2.getText().toString());
        obj.put("Tag", chosenTag);
        Log.d("Json", obj.toString()); */

    }



    @Override
    public void ProcessResults(String res) {
        System.out.println("Odebrano " + res);
        try {
            JSONArray ja = new JSONArray(res);

            String[] tablica = new String[ja.length()];
            for(int i = 0; i < ja.length(); ++i)
                tablica[i] = ja.get(i).toString();
            ustawTagi(tablica);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}




