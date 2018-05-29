package fixme.fixme2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayNotificationsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notifications);

        // Capture the layout's TableLayout and set the list of notifications as its rows
        TableLayout tableLayout = findViewById(R.id.notificationsTable);
        //TODO tutaj trzeba stworzyć wiersze
        //TODO przykład: https://technotzz.wordpress.com/2011/11/04/android-dynamically-add-rows-to-table-layout/
        TableRow tr = new TableRow(this);
        TextView label_place = new TextView(this);
        label_place.setText("3130");
        TextView label_tag = new TextView(this);
        label_tag.setText("Hydraulika");
        TextView label_description = new TextView(this);
        label_description.setText("Pękła rura i cieknie woda");
        tr.addView(label_place);
        tr.addView(label_tag);
        tr.addView(label_description);
        tableLayout.addView(tr);
    }
}
