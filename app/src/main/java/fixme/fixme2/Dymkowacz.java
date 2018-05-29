package fixme.fixme2;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Dymkowacz {
    public static Main3Activity menu = null;
    public static void wypiszDymek(final String message) {
        menu.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = menu.getApplicationContext();
                CharSequence text = message;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }
    public static void loguj() {
        menu.logujeSie();
    }
    public static void wyloguj() {
        menu.wylogowujeSie();
    }
}
