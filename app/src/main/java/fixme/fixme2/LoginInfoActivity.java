package fixme.fixme2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class LoginInfoActivity extends AppCompatActivity {
    private static final int LOGIN_CODE = 420;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);
        setTitle("Menu logowania");
        if(LoginData.logged) {
            ((Button) findViewById(R.id.button10)).setEnabled(false);
            ((Button) findViewById(R.id.button13)).setEnabled(false);
            ((Button) findViewById(R.id.button14)).setEnabled(false);
        }
        else {
            ((Button) findViewById(R.id.button11)).setEnabled(false);
        }

    }
    public void wyloguj(View view) {
        LoginData.logged = false;
        ((Button) findViewById(R.id.button11)).setEnabled(false);
        ((Button) findViewById(R.id.button10)).setEnabled(true);
        ((Button) findViewById(R.id.button13)).setEnabled(true);
        ((Button) findViewById(R.id.button14)).setEnabled(true);
        Dymkowacz.wypiszDymek("Wylogowuję");
        Dymkowacz.menu.wylogowujeSie();
    }
    public void rejestruj(View view) {
        Intent intent = new Intent(this, RejestrujActivity.class);
        startActivity(intent);
    }
    public void aktywuj(View view) {
        Intent intent = new Intent(this, AktywujActivity.class);
        startActivity(intent);
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
                    new POSTLoginTask().execute(data.getStringExtra("login"), data.getStringExtra("password"));
                }
                break;
            }
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
                HttpClient client = new DefaultHttpClient();
                URI website = new URI(Constant.API_USER_VALIDATION);
                HttpGet request = new HttpGet();
                request.setURI(website);

                request.setHeader("Username", login);
                request.setHeader("Password", pass);
                HttpResponse response = client.execute(request);

                String result = convertInputStreamToString(response.getEntity().getContent());


                if(result.equals("valid")) {

                    LoginData.logged = true;
                    LoginData.username = login;
                    LoginData.password = pass;
                    Dymkowacz.menu.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Dymkowacz.menu.logujeSie();

                            ((Button) findViewById(R.id.button11)).setEnabled(true);
                            ((Button) findViewById(R.id.button10)).setEnabled(false);
                            ((Button) findViewById(R.id.button13)).setEnabled(false);
                            ((Button) findViewById(R.id.button14)).setEnabled(false);
                        }
                    });



                    Dymkowacz.wypiszDymek("Witaj " + LoginData.username + "!");
                }
                else {
                    Dymkowacz.wypiszDymek("Złe dane logowania!");

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
