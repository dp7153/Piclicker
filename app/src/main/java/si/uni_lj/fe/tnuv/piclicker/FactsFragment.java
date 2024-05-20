package si.uni_lj.fe.tnuv.piclicker;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FactsFragment extends Fragment {

    private TextView textViewFacts;
    private int factNo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFactNoFromSharedPreferences();
    }
    private void loadFactNoFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(GameFragment.PREFS_NAME, MODE_PRIVATE);
        factNo = preferences.getInt(GameFragment.FACT_NO_KEY, 1); // Default to 1 if not found
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d("FactsFragment", "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_facts, container, false);
        textViewFacts = rootView.findViewById(R.id.textViewFacts);

        /*Bundle bundle = getArguments();
        if(bundle == null){
            Log.d("FactsFragment", "not work"); // Debug logging
        }
        if (bundle != null) {
            String selectedButton = bundle.getString("selected_button");
            //
            String factsForImage = getFactsForImage(selectedButton);
            textViewFacts.setText(factsForImage);
        }*/
        GameFragment gf = new GameFragment();
        Log.d("Facts", String.valueOf(factNo));
        textViewFacts.setText(getFactsForImage(factNo));

        return rootView;
    }


    private String getFactsForImage(int factNo) {
        switch (factNo) {
            case 1:
                return "Koale so mnoge živali";
            case 2:
                return "Slovenija od kod lepote tvoje";
            case 3:
                return "Pluto je najmanši planet";
            case 4:
                return "Mars je rdeči planet";
            case 5:
                return "Medved je velika zver";
            case 6:
                return "Avstrija je velika dežela";
            default:
                return "Ni informacij";
        }
    }
}