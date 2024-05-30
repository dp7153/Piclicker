package si.uni_lj.fe.tnuv.piclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FactsFragment extends Fragment {

    private TextView textViewFacts;
    private Button addFactButton;
    private EditText editTextNewFact;
    private LinkedHashMap<String, List<String>> factsMap = new LinkedHashMap<>();
    private static final String CUSTOM_FACTS_KEY = "custom_facts";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDefaultFacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facts, container, false);
        textViewFacts = rootView.findViewById(R.id.textViewFacts);
        addFactButton = rootView.findViewById(R.id.buttonAddFact);
        editTextNewFact = rootView.findViewById(R.id.editTextNewFact);

        SharedPreferences preferences = getActivity().getSharedPreferences(GameFragment.PREFS_NAME, MODE_PRIVATE);
        String selectedButton = preferences.getString(GameFragment.SELECTED_BUTTON_KEY, "Koala");

        Log.d("FactsFragment", "Selected Button: " + selectedButton);

        // Display the facts for the selected button
        String factsForImage = getFactsForImage(selectedButton);
        textViewFacts.setText(factsForImage);

        // Show the add fact button if the custom theme is selected
        if (selectedButton != null && !factsMap.containsKey(selectedButton)) {
            addFactButton.setVisibility(View.VISIBLE);
            editTextNewFact.setVisibility(View.VISIBLE);

            addFactButton.setOnClickListener(v -> {
                String newFact = editTextNewFact.getText().toString().trim();
                if (!newFact.isEmpty()) {
                    addCustomFact(selectedButton, newFact);
                    textViewFacts.setText(getFactsForImage(selectedButton));
                    editTextNewFact.setText("");
                }
            });
        } else {
            addFactButton.setVisibility(View.GONE);
            editTextNewFact.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void initializeDefaultFacts() {
        if (!factsMap.isEmpty()) {
            return; // Prevent reinitialization
        }

        factsMap.put("Koala", Arrays.asList("Koale so mnoge živali", "Koale jedo listje evkaliptusa", "Koale so lenobe"));
        factsMap.put("Slovenia", Arrays.asList("Slovenija od kod lepote tvoje", "Ljubljana je glavno mesto Slovenije", "Triglav je najvišji vrh"));
        factsMap.put("Pluto", Arrays.asList("Pluto je najmanši planet", "Pluto ima srce v svoji ledeni površini", "Pluto je del Kuiperjevega pasu"));
        factsMap.put("Mars", Arrays.asList("Mars je rdeči planet", "Mars ima dve luni", "Mars je dom najvišje gore v Osončju"));
        factsMap.put("Bear", Arrays.asList("Medved je velika zver", "Medvedi hibernirajo pozimi", "Medvedi so vsejedi"));
        factsMap.put("Austria", Arrays.asList("Avstrija je velika dežela", "Dunaj je glavno mesto Avstrije", "Avstrija ima veliko zgodovinskih znamenitosti"));

        // Load custom facts from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences(GameFragment.PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith(CUSTOM_FACTS_KEY)) {
                String customKey = entry.getKey().substring(CUSTOM_FACTS_KEY.length() + 1);
                List<String> customFacts = new ArrayList<>(Arrays.asList(((String) entry.getValue()).split("\\|")));
                factsMap.put(customKey, customFacts);
            }
        }

        Log.d("FactsFragment", "Order of insertion: " + new ArrayList<>(factsMap.keySet()));
    }

    private void addCustomFact(String theme, String fact) {
        SharedPreferences preferences = getActivity().getSharedPreferences(GameFragment.PREFS_NAME, MODE_PRIVATE);
        List<String> customFacts = factsMap.getOrDefault(theme, new ArrayList<>());
        customFacts.add(fact);
        factsMap.put(theme, customFacts);

        // Save the updated custom facts to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        String customFactsString = String.join("|", customFacts);
        editor.putString(CUSTOM_FACTS_KEY + "_" + theme, customFactsString);
        editor.apply();
    }

    private String getFactsForImage(String selectedButton) {
        if (selectedButton == null) {
            return "Ni informacij";
        }

        List<String> facts = factsMap.get(selectedButton);
        if (facts == null || facts.isEmpty()) {
            return "Ni informacij";
        }

        StringBuilder factsBuilder = new StringBuilder();
        for (String fact : facts) {
            factsBuilder.append(fact).append("\n");
        }
        return factsBuilder.toString().trim();
    }
}
