package fixme.fixme2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main3Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String chosenTag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        startService(new Intent(this, AlertService.class));

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        new HttpAsyncTask().execute(Constant.API_TAGS);
    }
    public void wysylajZgloszenia(View view) {
        System.out.println("Kliknięto guzik");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void pokazTabelke(View view) {
        Intent intent = new Intent(this, TabelkaActivity.class);
        intent.putExtra("link", Constant.API_REPORTS); // tutaj ląduje link do ostatnich zgłoszeń
        startActivity(intent);
    }
    public void pokazZTagiem(View view) {
        Intent intent = new Intent(this, TabelkaActivity.class);
        intent.putExtra("link", Constant.API_REPORTS + "?tags=" + chosenTag); // tutaj ląduje link do ostatnich zgłoszeń
        startActivity(intent);
    }
    public void pokazWOkolicy(View view) {
        Intent intent = new Intent(this, TabelkaActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText3);
        intent.putExtra("link", Constant.API_REPORTS + "?location=" + editText.getText().toString()); // tutaj ląduje link do ostatnich zgłoszeń
        startActivity(intent);
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

    private void ustawTagi(String[] tablica) {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tablica);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
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
            System.out.println("Odbieram z adresu " + urls[0]);
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            System.out.println("Odebrano " + result);
            try {
                JSONArray ja = new JSONArray(result);

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
