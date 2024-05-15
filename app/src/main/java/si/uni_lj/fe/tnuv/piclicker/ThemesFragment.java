package si.uni_lj.fe.tnuv.piclicker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class ThemesFragment extends Fragment {

    public ThemesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);

        // Create a list of button labels
        String[] buttonLabels = {"Koala", "Slovenija", "Pluton"};

        // Get the frame layout to add buttons dynamically
        FrameLayout frameLayout = rootView.findViewById(R.id.frame_layout);

        // Define initial top margin
        int marginTop = 10;

        // Loop through the button labels and create buttons dynamically
        for (String label : buttonLabels) {
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

                    GameFragment gameFragment = new GameFragment();
                    gameFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, gameFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            // Add the button to the frame layout
            frameLayout.addView(button);
        }

        return rootView;
    }
}
