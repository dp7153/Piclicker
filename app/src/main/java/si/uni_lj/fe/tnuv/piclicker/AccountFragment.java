package si.uni_lj.fe.tnuv.piclicker;

import static si.uni_lj.fe.tnuv.piclicker.GameFragment.PREFS_NAME;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Button to reset SharedPreferences
        Button resetButton = rootView.findViewById(R.id.reset_progress);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog to confirm the reset action
                new AlertDialog.Builder(getActivity())
                        .setTitle("Reset Progress & Data")
                        .setMessage("Clicking OK will reset your game progress & data, are you sure you want to proceed?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Clear SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear(); // Clear all data
                                editor.apply();

                                // You can also reset specific values like this:
                                // editor.remove("your_key");
                                // editor.apply();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return rootView;
    }
}
