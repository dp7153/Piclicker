package si.uni_lj.fe.tnuv.piclicker;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FactsFragment extends Fragment {

    private TextView textViewFacts;

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
        //gf = gf.pass();
        int factNo = gf.getFactNo();
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
            // Add cases for other images
            default:
                return "Ni informacij";
        }
    }
}