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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        replaceFragment(new GameFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.game) {
                replaceFragment(new GameFragment());
            }
            else if (itemId == R.id.account) {
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
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    public void selectGameMenuItem() {
        binding.bottomNavigationView.setSelectedItemId(R.id.game);
    }


}
