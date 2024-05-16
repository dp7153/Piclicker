package si.uni_lj.fe.tnuv.piclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ShopFragment extends Fragment {
    private OnCookieUpdateListener cookieUpdateListener;
    private TextView cookiesTextView;
    private TextView productionBeltCountTextView;
    private TextView workerCountTextView;
    private TextView factoryCountTextView;

    private int cookies = 0; // Initialize to 0 initially
    private int productionBeltCount = 0;
    private int workerCount = 0;
    private int factoryCount = 0;

    private Handler handler;
    private Runnable productionBeltRunnable;
    private Runnable workerRunnable;
    private Runnable factoryRunnable;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load data from SharedPreferences
        loadDataFromSharedPreferences();

        // Initialize handler
        handler = new Handler();

        // Initialize Runnables for each item
        productionBeltRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10000); // Run every 10 seconds
                incrementCookies(1 * productionBeltCount); // Increment by 1 * productionBeltCount
            }
        };

        workerRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 30000); // Run every 30 seconds
                incrementCookies(2 * workerCount); // Increment by 2 * workerCount
            }
        };

        factoryRunnable = new Runnable() {
            @Override
            public void run() {
                incrementCookies(8 * factoryCount); // Increment by 8 * factoryCount
                handler.postDelayed(this, 30000); // Run every 30 seconds
            }
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialize TextViews
        cookiesTextView = view.findViewById(R.id.cookiesTextView);
        productionBeltCountTextView = view.findViewById(R.id.productionBeltCountTextView);
        workerCountTextView = view.findViewById(R.id.workerCountTextView);
        factoryCountTextView = view.findViewById(R.id.factoryCountTextView);

        // Set initial counts
        updateUI();

        // Button to buy Production Belt
        Button btnProductionBelt = view.findViewById(R.id.btn_production_belt);
        btnProductionBelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookies >= 5) {
                    // Deduct 5 cookies for buying Production Belt
                    cookies -= 5;
                    productionBeltCount++;
                    updateUI();
                    // Save data to SharedPreferences
                    saveDataToSharedPreferences();
                    // Notify the user
                    Toast.makeText(getActivity(), "Production Belt purchased!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Not enough clicks cookies!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button to buy Worker
        Button btnWorker = view.findViewById(R.id.btn_worker);
        btnWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookies >= 200) {
                    // Deduct 200 cookies for buying Worker
                    cookies -= 200;
                    workerCount++;
                    updateUI();
                    // Save data to SharedPreferences
                    saveDataToSharedPreferences();
                    // Notify the user
                    Toast.makeText(getActivity(), "Worker hired!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Not enough clicks!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button to buy Factory
        Button btnFactory = view.findViewById(R.id.btn_factory);
        btnFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookies >= 500) {
                    // Deduct 1000 cookies for buying Factory
                    cookies -= 500;
                    factoryCount++;
                    updateUI();
                    // Save data to SharedPreferences
                    saveDataToSharedPreferences();
                    // Notify the user
                    Toast.makeText(getActivity(), "Factory built!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Not enough clicks!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start the incrementation Runnables
        handler.post(productionBeltRunnable);
        handler.post(workerRunnable);
        handler.post(factoryRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the incrementation Runnables
        handler.removeCallbacks(productionBeltRunnable);
        handler.removeCallbacks(workerRunnable);
        handler.removeCallbacks(factoryRunnable);
    }

    // Method to update the displayed UI
    private void updateUI() {
        cookiesTextView.setText("Cookies: " + cookies);
        productionBeltCountTextView.setText("Production Belt: " + productionBeltCount);
        workerCountTextView.setText("Worker: " + workerCount);
        factoryCountTextView.setText("Factory: " + factoryCount);
    }

    // Method to save data to SharedPreferences
    private void saveDataToSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(GameFragment.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(GameFragment.COOKIES_KEY, cookies);
        editor.putInt("productionBeltCount", productionBeltCount);
        editor.putInt("workerCount", workerCount);
        editor.putInt("factoryCount", factoryCount);
        editor.apply();
    }

    // Method to load data from SharedPreferences
    private void loadDataFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(GameFragment.PREFS_NAME, MODE_PRIVATE);
        cookies = preferences.getInt(GameFragment.COOKIES_KEY, 0);
        productionBeltCount = preferences.getInt("productionBeltCount", 0);
        workerCount = preferences.getInt("workerCount", 0);
        factoryCount = preferences.getInt("factoryCount", 0);
    }

    // Method to increment cookies
    public void incrementCookies(int amount) {
        cookies += amount;
        updateUI();
        saveDataToSharedPreferences();
    }


    public interface OnCookieUpdateListener {
        void onCookieUpdate(int newCookieCount);
    }
}
