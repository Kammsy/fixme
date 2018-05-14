package fixme.fixme2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        startService(new Intent(this, AlertService.class));
    }
    public void wysylajZgloszenia(View view) {
        System.out.println("Kliknięto guzik");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void pokazTabelke(View view) {
        Intent intent = new Intent(this, TabelkaActivity.class);
        intent.putExtra("link", "students.mimuw.edu.pl"); // tutaj ląduje link do ostatnich zgłoszeń
        startActivity(intent);
    }
}
