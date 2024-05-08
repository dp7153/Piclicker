package si.uni_lj.fe.tnuv.piclicker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int cookies = 0;
    private TextView textViewCookies;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String COOKIES_KEY = "cookies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClick = findViewById(R.id.btn_click);
        textViewCookies = findViewById(R.id.click);

        // Load cookies count from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        cookies = preferences.getInt(COOKIES_KEY, 0);
        updateCookiesDisplay();

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increase the number of cookies and update the TextView
                cookies++;
                updateCookiesDisplay();

                // Save cookies count to SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt(COOKIES_KEY, cookies);
                editor.apply();
            }
        });
    }

    // Update the TextView to display the current number of cookies
    private void updateCookiesDisplay() {
        textViewCookies.setText("Cookies: " + cookies);
    }
}
