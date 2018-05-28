package fixme.fixme2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Main3Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GetResultable {
    private String chosenTag = "";
    private static final int LOGIN_CODE = 420;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //startService(new Intent(this, AlertService.class));

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        new GETAsyncTask(this).execute(Constant.API_TAGS);
    }
    public void loguj(View view) {
        Intent intent = new Intent(this, Main4Activity.class);
        startActivityForResult(intent, LOGIN_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (LOGIN_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    //System.out.println("Login = " + data.getStringExtra("login") + " Hasło = " + data.getStringExtra("password"));
                    new POSTLoginTask().execute(data.getStringExtra("login"), data.getStringExtra("password"));
                }
                break;
            }
        }
    }
    private void showError() {
        Context context = getApplicationContext();
        CharSequence text = "Błąd! Tylko zalogowani użytkownicy mogą to robić!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    public void wysylajZgloszenia(View view) {
        System.out.println("Kliknięto guzik");
        if(!LoginData.logged) {
            showError();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void pokazTabelke(View view) {
        if(!LoginData.logged) {
            showError();
            return;
        }
        Intent intent = new Intent(this, TabelkaActivity.class);
        intent.putExtra("link", Constant.API_REPORTS); // tutaj ląduje link do ostatnich zgłoszeń
        startActivity(intent);
    }
    public void rejestruj(View view) {
        Intent intent = new Intent(this, RejestrujActivity.class);
        startActivity(intent);
    }
    public void aktywuj(View view) {
        Intent intent = new Intent(this, AktywujActivity.class);
        startActivity(intent);
    }
    public void pokazZTagiem(View view) {
        if(!LoginData.logged) {
            showError();
            return;
        }
        Intent intent = new Intent(this, TabelkaActivity.class);
        intent.putExtra("link", Constant.API_REPORTS + "?tag=" + chosenTag); // tutaj ląduje link do ostatnich zgłoszeń
        startActivity(intent);
    }
    public void pokazWOkolicy(View view) {
        if(!LoginData.logged) {
            showError();
            return;
        }
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

    @Override
    public void ProcessResults(String res) {
        try {
            JSONArray ja = new JSONArray(res);

            String[] tablica = new String[ja.length()];
            for(int i = 0; i < ja.length(); ++i)
                tablica[i] = ja.get(i).toString();
            ustawTagi(tablica);
        } catch (JSONException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sprawdź połączenie z internetem", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            e.printStackTrace();
        }
    }
    private class POSTLoginTask  extends AsyncTask<String, Void, String> {
        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        @Override
        protected String doInBackground(String... strings) {
            String login = strings[0];
            String pass = strings[1];

            try {
                //URL url = new URL(Constant.API_USER_VALIDATION);
                //HttpURLConnection client = (HttpURLConnection) url.openConnection();
                //client.setRequestMethod("GET");
                //client.setRequestProperty("Username", login);
                //client.setRequestProperty("Password", pass);
                HttpClient client = new DefaultHttpClient();
                URI website = new URI(Constant.API_USER_VALIDATION);
                HttpGet request = new HttpGet();
                request.setURI(website);

                request.setHeader("Username", login);
                request.setHeader("Password", pass);
                HttpResponse response = client.execute(request);
                //response.getEntity().toString();
                String result = convertInputStreamToString(response.getEntity().getContent());


                if(result.equals("valid")) {

                    LoginData.logged = true;
                    LoginData.username = login;
                    LoginData.password = pass;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "Witaj " + LoginData.username + "!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "Zle dane logowania", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });


                }
                return result;
            }
            catch ( Exception e) {
                e.printStackTrace();
            }

            return "NIEOK";
        }
        @Override
        protected void onPostExecute(String res) {
            System.out.println("Logowanie: " + res);
        }
    }
}
