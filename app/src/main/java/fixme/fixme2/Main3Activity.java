package fixme.fixme2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.SQLOutput;

public class Main3Activity extends AppCompatActivity {
    private String chosenTag = "";
    private static final int LOGIN_CODE = 420;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        startService(new Intent(this, AlertService.class));
        Dymkowacz.menu = this;

        setTitle("FixMe - Menu główne");
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        if(!LoginData.logged) {
            ((Button) findViewById(R.id.button3)).setEnabled(false);
            ((Button) findViewById(R.id.button15)).setEnabled(false);
            textView.setText("Niezalogowano");
        }
        else {

            textView.setText("Zalogowano jako " + LoginData.username);
        }

    }
    public void logowanie(View view) {
        Intent intent = new Intent(this, LoginInfoActivity.class);
        startActivity(intent);
    }

    public void pokaszZgloszenia(View view) {
        Intent intent = new Intent(this, AlertFilterActivity.class);
        startActivity(intent);
    }
    void logujeSie() {
        ((Button) findViewById(R.id.button3)).setEnabled(true);
        ((Button) findViewById(R.id.button15)).setEnabled(true);
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Zalogowano jako " + LoginData.username);
    }
    void wylogowujeSie() {
        ((Button) findViewById(R.id.button3)).setEnabled(false);
        ((Button) findViewById(R.id.button15)).setEnabled(false);
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Niezalogowano");
    }

    public void wysylajZgloszenia(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
