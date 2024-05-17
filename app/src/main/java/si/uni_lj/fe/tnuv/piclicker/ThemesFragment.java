package si.uni_lj.fe.tnuv.piclicker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;

public class ThemesFragment extends Fragment {

    public ThemesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);

        // Create arrays of button labels for each list
        String[] animals = {"Koala", "Bear"};
        String[] countries = {"Austria", "Slovenia"};
        String[] planets = {"Mars", "Pluto"};

        // Get the frame layouts to add buttons dynamically
        FrameLayout frameLayoutAnimals = rootView.findViewById(R.id.frame_layout_animals);
        FrameLayout frameLayoutCountries = rootView.findViewById(R.id.frame_layout_countries);
        FrameLayout frameLayoutPlanets = rootView.findViewById(R.id.frame_layout_planets);

        // Add buttons dynamically to the respective lists
        addButtonList(frameLayoutAnimals, animals);
        addButtonList(frameLayoutCountries, countries);
        addButtonList(frameLayoutPlanets, planets);

        return rootView;
    }

    private void addButtonList(FrameLayout frameLayout, String[] items) {
        // Define initial top margin
        int marginTop = 10;

        // Loop through the button labels and create buttons dynamically
        for (String label : items) {
            Button button = new Button(getActivity());
            button.setText(label);
            button.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));

            // Add margin to buttons except for the first one
            if (marginTop > 0) {
                ((FrameLayout.LayoutParams) button.getLayoutParams()).topMargin = marginTop;
            }

            // Increase top margin for the next button
            marginTop += 100;

            // Set onClickListener to replace the current fragment with GameFragment
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = ((Button) v).getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("selected_button", buttonText);
                    Log.d("ThemesFragment", "Bundle created with button text: " + buttonText); // Add this line for debug logging

                    GameFragment gameFragment = new GameFragment();
                    gameFragment.setArguments(bundle);

                    FactsFragment factsFragment = new FactsFragment();
                    factsFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, gameFragment)
                            .addToBackStack(null)
                            .commit();

                    // Select the "Game" item in the bottom navigation menu
                    ((MainActivity) getActivity()).selectGameMenuItem();
                }
            });

            // Add the button to the frame layout
            frameLayout.addView(button);
        }
    }
}
