package fixme.fixme2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class AlertFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GetResultable {
    String chosenTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_filter);

        Spinner spinner = findViewById(R.id.spinner3);
        spinner.setOnItemSelectedListener(this);
        setTitle("Zgłoszenia");
        new GETAsyncTask(this).execute(Constant.API_TAGS);
    }
    private void showError() {
        Context context = getApplicationContext();
        CharSequence text = "Błąd! Tylko zalogowani użytkownicy mogą to robić!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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
        chosenTag = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void ustawTagi(String[] tablica) {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tablica);
        Spinner spinner = findViewById(R.id.spinner3);
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
}
