package si.uni_lj.fe.tnuv.piclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import si.uni_lj.fe.tnuv.piclicker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private int cookies = 0;
    private TextView textViewCookies;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String COOKIES_KEY = "cookies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        replaceFragment(new AccountFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.account) {
                replaceFragment(new AccountFragment());
            }
            else if (itemId == R.id.facts) {
                replaceFragment(new FactsFragment());
            }
            else if (itemId == R.id.shop) {
                replaceFragment(new ShopFragment());
            }
            else if (itemId == R.id.themes) {
                replaceFragment(new ThemesFragment());
            }
            return true;
        });

        ImageButton btnClick = findViewById(R.id.btn_click);
        textViewCookies = findViewById(R.id.click);

        // Load cookies count from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        cookies = preferences.getInt(COOKIES_KEY, 0);
        updateCookiesDisplay();

        btnClick.setOnClickListener(v -> {
            // Increase the number of cookies and update the TextView
            cookies++;
            updateCookiesDisplay();

            // Save cookies count to SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt(COOKIES_KEY, cookies);
            editor.apply();
        });
    }

    // Update the TextView to display the current number of cookies
    private void updateCookiesDisplay() {
        textViewCookies.setText("Clicks: " + cookies);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
