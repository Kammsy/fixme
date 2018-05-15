package fixme.fixme2;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TabelkaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabelka);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        System.out.println("Link = " + link);
        new HttpAsyncTask().execute(link);
    }
    private static String wiersz(JSONObject obj) throws JSONException {
        String res = obj.getString("title");
        res += "\nlokalizacja: " + obj.getString("location");
        res += "\ntagi: ";
        JSONArray ja = obj.getJSONArray("tags");
        for (int i = 0; i < ja.length(); ++i) {
            res += ja.getString(i);
            if (i + 1 == ja.length())
                res += "\n";
            else
                res += ", ";
        }
        res += obj.getString("description");
        return res;
    }
    private void parsujJSON(String text) {
        //dodać parsowanie JSON
        try {
            JSONArray ja = new JSONArray(text);
            String[] tablica = new String[ja.length()];
            for(int i = 0; i < tablica.length; ++i)
                tablica[i] = wiersz(ja.getJSONObject(i));

            ListView listview = (ListView) findViewById(R.id.list_view);
            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tablica);
            listview.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            parsujJSON(result);
        }
    }
}
