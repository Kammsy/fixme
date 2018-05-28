package fixme.fixme2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class RejestrujActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestruj);
    }
    public void rejestruj(View view) {
        String name = ((EditText) findViewById(R.id.editText6)).getText().toString();
        String surname = ((EditText) findViewById(R.id.editText7)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText8)).getText().toString();
        String pass = ((EditText) findViewById(R.id.editText9)).getText().toString();
        String pass2 = ((EditText) findViewById(R.id.editText10)).getText().toString();
        if(!pass.equals(pass2)) {
            System.out.println("Hasła się nie pokrywają!");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("name", name + " " + surname);
        map.put("email", email);
        map.put("password", pass);
        new Thread(new SendPOST(Constant.API_REGISTER, map)).start();
        finish();
    }
}
