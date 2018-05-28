package fixme.fixme2;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class TabelkaActivity extends AppCompatActivity implements GetResultable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabelka);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        new GETAsyncTask(this).execute(link);
    }
    private void showError() {
        Context context = getApplicationContext();
        CharSequence text = "Błąd! Sprawdź połączenie z internetem!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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

    // parsuje jsona i dodaje wiersze do tabelki
    private void parsujJSON(String text) {
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
            showError();
        }

    }

    @Override
    public void ProcessResults(String res) {

        parsujJSON(res);
    }

}
