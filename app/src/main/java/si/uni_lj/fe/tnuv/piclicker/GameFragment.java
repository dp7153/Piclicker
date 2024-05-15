package si.uni_lj.fe.tnuv.piclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class GameFragment extends Fragment {

    private int cookies = 0;
    private TextView textViewCookies;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String COOKIES_KEY = "cookies";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        ImageButton btnClick = rootView.findViewById(R.id.btn_click);
        textViewCookies = rootView.findViewById(R.id.click);

        // Load cookies count from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        cookies = preferences.getInt(COOKIES_KEY, 0);
        updateCookiesDisplay();

        btnClick.setOnClickListener(v -> {
            // Increase the number of cookies and update the TextView
            cookies++;
            updateCookiesDisplay();

            // Save cookies count to SharedPreferences
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt(COOKIES_KEY, cookies);
            editor.apply();
        });

        // Retrieve the selected button information
        Bundle bundle = getArguments();
        if (bundle != null) {
            String selectedButton = bundle.getString("selected_button");
            int imageResource = getImageResource(selectedButton);

            // Update the ImageButton with the selected image
            btnClick.setImageResource(imageResource);
        }

        return rootView;
    }

    // Method to map button text to image resource
    private int getImageResource(String buttonText) {
        switch (buttonText) {
            case "Koala":
                return R.drawable.koala;
            case "Slovenia":
                return R.drawable.slovenia;
            case "Pluto":
                return R.drawable.pluto;
            case "Mars":
                return R.drawable.mars;
            case "Bear":
                return R.drawable.bear;
            case "Austria":
                return R.drawable.austria;

            default:
                return R.drawable.koala; // Default image
        }
    }

    private void updateCookiesDisplay() {
        textViewCookies.setText("Clicks: " + cookies);
    }
}
