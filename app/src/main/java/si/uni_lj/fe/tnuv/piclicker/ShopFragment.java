package si.uni_lj.fe.tnuv.piclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ShopFragment extends Fragment {

    private TextView cookiesTextView;
    private int cookies = 0; // Initialize to 0 initially

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load cookies count from SharedPreferences
        GameFragment gf = new GameFragment();
        String prefname = gf.getPrefsName();
        String cookie = gf.getCookiesKey();
        SharedPreferences preferences = getActivity().getSharedPreferences(prefname, MODE_PRIVATE);
        cookies = preferences.getInt(cookie, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialize TextView
        cookiesTextView = view.findViewById(R.id.cookiesTextView);

        // Set the text of the TextView to display cookies
        cookiesTextView.setText("Clicks: " + cookies);

        return view;
    }
}
