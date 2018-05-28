package fixme.fixme2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Main4Activity.this.setTitle("Logowanie");

    }
    public void logujKlik(View view) {
        String login  = ((EditText) findViewById(R.id.editText4)).getText().toString();
        String pass = ((EditText) findViewById(R.id.editText5)).getText().toString();
        if(!login.isEmpty() && !pass.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("login", login);
            resultIntent.putExtra("password", pass);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
