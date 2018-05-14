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

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

        // tutaj ląduje link do tagów
        new HttpAsyncTask().execute("http://students.mimuw.edu.pl/~lk385775/tagi.get");
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
        Tools.sendEmail(message);
        startActivity(intent);

        JSONObject obj = new JSONObject();
        obj.put("Miejsce", editText.getText().toString());
        obj.put("Opis", editText2.getText().toString());
        obj.put("Tag", chosenTag);
        Log.d("Json", obj.toString());

    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }
        public String GET(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;
        }
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            System.out.println("Odebrano " + result);
            try {
                JSONObject json = new JSONObject(result);
                JSONArray ja = json.getJSONArray("tagi");
                String[] tablica = new String[ja.length()];
                for(int i = 0; i < ja.length(); ++i)
                    tablica[i] = ja.get(i).toString();
                ustawTagi(tablica);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}



