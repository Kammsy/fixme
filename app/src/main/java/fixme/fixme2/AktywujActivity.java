package fixme.fixme2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class AktywujActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktywuj);
    }
    public void aktywuj(View view) {
        String email = ((EditText) findViewById(R.id.editText11)).getText().toString();
        String pass = ((EditText) findViewById(R.id.editText12)).getText().toString();
        String kod = ((EditText) findViewById(R.id.editText13)).getText().toString();
        if(kod.length() != 4) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", pass);
        map.put("token", kod);
        new Thread(new SendPOST(Constant.API_ACTIVATE, map)).start();
        finish();
    }
}
