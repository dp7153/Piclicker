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
import android.text.Html;

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
        textViewFacts.setText(Html.fromHtml(factsForImage));

        // Show the add fact button if the custom theme is selected
        if (selectedButton != null && !factsMap.containsKey(selectedButton)) {
            addFactButton.setVisibility(View.VISIBLE);
            editTextNewFact.setVisibility(View.VISIBLE);

            addFactButton.setOnClickListener(v -> {
                String newFact = editTextNewFact.getText().toString().trim();
                if (!newFact.isEmpty()) {
                    addCustomFact(selectedButton, newFact);
                    textViewFacts.setText(Html.fromHtml(getFactsForImage(selectedButton)));
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

        factsMap.put("Koala", Arrays.asList("<b>What do I eat?</b>", "Koalas love eating eucalyptus.", "<b>Where do I live?</b>", "Koalas live in Australia and South Asia.", "<b>Anything interesting?</b>", "They sleep for up to 12 hours a day!"));
        factsMap.put("Slovenia", Arrays.asList("<b>How big is Slovenia?</b>", "Its surface is about 20.272 square kilometers.", "<b>What's the capital?</b>", "Ljubljana", "<b>Anything interesting?</b>", "Slovenia has over 50 different dialects!"));
        factsMap.put("Pluto", Arrays.asList("<b>Is Pluto a star or a planet?</b>", "Officially, it is a 'minor planet'.", "<b>Can humans survive on Pluto?</b>", "No, it is too cold.", "<b>Anything interesting?</b>", "Pluto has five moons!"));
        factsMap.put("Mars", Arrays.asList("<b>Is Mars a star or a planet?</b>","A planet", "<b>Can humans survive on Mars?</b>", "Not yet, but its gravity is most similar to Earth's", "<b>Anything interesting?</b>", "There are signs of water-residue on the surface!"));
        factsMap.put("Bear", Arrays.asList("<b>What do I eat?</b>", "Bears love fish and fruits, but they eat pretty much everything.", "<b>Where do I live?</b>", "Northern Hemisphere", "<b>Anything interesting?</b>", "I can run for up to 80 km/h!"));
        factsMap.put("Austria", Arrays.asList("<b>How big is Austria?</b>", "Its surface is around 82.823 square kilometers.", "<b>What is the capital?</b>", "Vienna", "<b>Anything interesting?</b>", "The country is land-locked and has no access to sea!"));

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
            return "No information";
        }

        List<String> facts = factsMap.get(selectedButton);
        if (facts == null || facts.isEmpty()) {
            return "No information";
        }

        StringBuilder factsBuilder = new StringBuilder();
        for (String fact : facts) {
            factsBuilder.append(fact).append("<br>"); // Use <br> for line breaks
        }
        return factsBuilder.toString().trim();
    }
}
