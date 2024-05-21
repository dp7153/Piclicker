package si.uni_lj.fe.tnuv.piclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    private int productionBeltCount = 0;
    private int workerCount = 0;
    private int factoryCount = 0;
    private TextView textViewCookies;
    private Handler handler;
    private Runnable incrementCookiesRunnable;
    public static final String FACT_NO_KEY = "fact_no";

    public static final String PREFS_NAME = "MyPrefs";
    public static final String COOKIES_KEY = "cookies";

    public static final String SELECTED_BUTTON_KEY = "selected_button";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("Stevilka facta:", "Fact" + String.valueOf(SELECTED_BUTTON_KEY));
        // Load data from SharedPreferences
        loadDataFromSharedPreferences();

        // Initialize handler
        handler = new Handler();

        // Initialize Runnable to increment cookies based on owned items
        incrementCookiesRunnable = new Runnable() {
            @Override
            public void run() {
                int incrementAmount = calculateIncrement();
                incrementCookies(incrementAmount);
                handler.postDelayed(this, 10000); // Run every 10 second
            }
        };
    }

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

        // Retrieve the last selected button information from SharedPreferences
        String lastSelectedButton = preferences.getString(SELECTED_BUTTON_KEY, "Koala");
        int lastSelectedImage = getImageResource(lastSelectedButton);
        // Set the ImageButton with the last selected image
        btnClick.setImageResource(lastSelectedImage);

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

            // Save the selected button to SharedPreferences
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(SELECTED_BUTTON_KEY, selectedButton);
            editor.apply();
        }


        return rootView;
    }

    public int factNo = 0;

    private void setFactNo(int index) {
        this.factNo = index;
        Log.d("facts", "setter: " + factNo);

        // Save factNo to SharedPreferences
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(FACT_NO_KEY, factNo);
        editor.apply();
    }

    // Method to map button text to image resource
    private int getImageResource(String buttonText) {
        switch (buttonText) {
            case "Koala":
                setFactNo(1);
                return R.drawable.koala;
            case "Slovenia":
                setFactNo(2);
                return R.drawable.slovenia;
            case "Pluto":
                setFactNo(3);
                return R.drawable.pluto;
            case "Mars":
                setFactNo(4);
                return R.drawable.mars;
            case "Bear":
                setFactNo(5);
                return R.drawable.bear;
            case "Austria":
                setFactNo(6);
                return R.drawable.austria;

            default:
                setFactNo(1);
                return R.drawable.koala; // Default image
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start the incrementation Runnable
        handler.postDelayed(incrementCookiesRunnable, 10000); // Start after 10 seconds
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the incrementation Runnable
        handler.removeCallbacks(incrementCookiesRunnable);
    }

    // Calculate the increment based on owned items
    private int calculateIncrement() {
        // Add up the total increment from owned items


        return productionBeltCount * 1 + workerCount * 2 + factoryCount * 8;
    }

    // Update the cookies count and display
    private void incrementCookies(int amount) {
        cookies += amount;
        updateCookiesDisplay();
        saveDataToSharedPreferences();
    }

    // Update the cookies display
    private void updateCookiesDisplay() {
        textViewCookies.setText("Clicks: " + cookies);
    }

    // Save data to SharedPreferences
    private void saveDataToSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COOKIES_KEY, cookies);
        editor.apply();
    }

    // Load data from SharedPreferences
    private void loadDataFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        cookies = preferences.getInt(COOKIES_KEY, 0);
        productionBeltCount = preferences.getInt("productionBeltCount", 0);
        workerCount = preferences.getInt("workerCount", 0);
        factoryCount = preferences.getInt("factoryCount", 0);
        factNo = preferences.getInt(FACT_NO_KEY, 1);
    }
}
