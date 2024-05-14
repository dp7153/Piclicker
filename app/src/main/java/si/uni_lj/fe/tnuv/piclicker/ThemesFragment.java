package si.uni_lj.fe.tnuv.piclicker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ThemesFragment extends Fragment {

    public ThemesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);

        Button btnShowGameFragment = rootView.findViewById(R.id.GameButton);
        btnShowGameFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the GameFragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, new GameFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}