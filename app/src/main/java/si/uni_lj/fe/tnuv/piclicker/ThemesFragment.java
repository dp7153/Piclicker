package si.uni_lj.fe.tnuv.piclicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;

import static android.app.Activity.RESULT_OK;

import com.google.android.material.snackbar.Snackbar;

public class ThemesFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String PREFS_NAME = "MyPrefs";
    public static final String CUSTOM_IMAGES_KEY = "custom_images";

    private Set<String> customImages;
    private LruCache<String, Bitmap> memoryCache;

    private String imageName;

    public ThemesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the memory cache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        // Load custom images from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        customImages = preferences.getStringSet(CUSTOM_IMAGES_KEY, new HashSet<>());
    }

    // Create arrays of button labels for each list
    private String[] animals = {"Koala", "Bear"};
    private String[] countries = {"Austria", "Slovenia"};
    private String[] planets = {"Mars", "Pluto"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);



        // Get the frame layouts to add buttons dynamically
        FrameLayout frameLayoutAnimals = rootView.findViewById(R.id.frame_layout_animals);
        FrameLayout frameLayoutCountries = rootView.findViewById(R.id.frame_layout_countries);
        FrameLayout frameLayoutPlanets = rootView.findViewById(R.id.frame_layout_planets);
        FrameLayout frameLayoutCustom = rootView.findViewById(R.id.frame_layout_custom);

        // Add buttons dynamically to the respective lists
        addButtonList(frameLayoutAnimals, animals);
        addButtonList(frameLayoutCountries, countries);
        addButtonList(frameLayoutPlanets, planets);

        // Add custom images to the custom frame layout
        addCustomImageButtons(frameLayoutCustom);

        // Set up the "Add Image" and "Clear Images" buttons
        Button addImageButton = rootView.findViewById(R.id.button_add_image);
        Button clearImagesButton = rootView.findViewById(R.id.button_clear_images);
        //addImageButton.setOnClickListener(v -> showImageNameDialog());
        addImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        clearImagesButton.setOnClickListener(v -> onClearImagesButtonClick(frameLayoutCustom));

        return rootView;
    }

    private void showImageNameDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_image_name, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editTextImageName = dialogView.findViewById(R.id.edit_text_image_name);
        Button buttonConfirm = dialogView.findViewById(R.id.button_confirm);

        buttonConfirm.setOnClickListener(v -> {
            imageName = editTextImageName.getText().toString().trim();
            if (!imageName.isEmpty()) {
                dialog.dismiss();
                saveImageToAppStorage(imageUri);
            } else {
                Toast.makeText(getActivity(), "Please enter an image name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void onClearImagesButtonClick(FrameLayout frameLayoutCustom) {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CUSTOM_IMAGES_KEY);
        // Check if the currently selected button is not in the predetermined values list
        String selectedButton = preferences.getString("selected_button", "");
        if (!Arrays.asList(animals).contains(selectedButton) &&
                !Arrays.asList(countries).contains(selectedButton) &&
                !Arrays.asList(planets).contains(selectedButton)) {
            // Change the selected_button value to a default value, e.g., "Koala"
            editor.putString("selected_button", "Koala");
        }
        editor.apply();

        customImages.clear();
        frameLayoutCustom.removeAllViews();
        Toast.makeText(getActivity(), "All custom images removed", Toast.LENGTH_SHORT).show();

        //changes back to koala and goes to GameFragment
        //onThemeButtonClick("Koala");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            showImageNameDialog(selectedImageUri);
        }
    }


    /*private void saveImageToAppStorage(Uri imageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            String filename = "image_" + System.currentTimeMillis() + ".png";
            FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            bitmap.recycle();

            customImages.add(filename);
            SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(CUSTOM_IMAGES_KEY, customImages);
            editor.apply();

            FrameLayout frameLayoutCustom = getView().findViewById(R.id.frame_layout_custom);
            addCustomImageButton(frameLayoutCustom, filename);

            Toast.makeText(getActivity(), "Image added successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private AlertDialog loadingDialog;

    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /*private void saveImageToAppStorage(Uri imageUri) {
        showLoadingDialog(); // Show the loading dialog
        new Thread(() -> {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                String filename = imageName;
                FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

                getActivity().runOnUiThread(() -> {
                    // Check if the fragment is still attached to the activity
                    if (isAdded()) {
                        customImages.add(filename);
                        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putStringSet(CUSTOM_IMAGES_KEY, customImages);
                        editor.apply();

                        FrameLayout frameLayoutCustom = getView().findViewById(R.id.frame_layout_custom);
                        addCustomImageButton(frameLayoutCustom, filename);

                        Toast.makeText(getActivity(), "Image added successfully", Toast.LENGTH_SHORT).show();
                        onThemeButtonClick(filename);
                    }
                    hideLoadingDialog(); // Hide the loading dialog
                });
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    if (isAdded()) {
                        Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                    hideLoadingDialog(); // Hide the loading dialog in case of error
                });
            }
        }).start();
    }*/

    private void saveImageToAppStorage(Uri imageUri) {
        showLoadingDialog(); // Show the loading dialog
        new Thread(() -> {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                String filename = imageName;

                // Check if the filename already exists in predetermined themes or custom themes
                if (!Arrays.asList(animals).contains(filename) &&
                        !Arrays.asList(countries).contains(filename) &&
                        !Arrays.asList(planets).contains(filename) &&
                        !customImages.contains(filename)) {
                    FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();

                    getActivity().runOnUiThread(() -> {
                        // Check if the fragment is still attached to the activity
                        if (isAdded()) {
                            customImages.add(filename);
                            SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putStringSet(CUSTOM_IMAGES_KEY, customImages);
                            editor.apply();

                            FrameLayout frameLayoutCustom = getView().findViewById(R.id.frame_layout_custom);
                            addCustomImageButton(frameLayoutCustom, filename);

                            Toast.makeText(getActivity(), "Image added successfully", Toast.LENGTH_SHORT).show();
                            onThemeButtonClick(filename);
                        }
                        hideLoadingDialog(); // Hide the loading dialog
                    });
                } else {
                    getActivity().runOnUiThread(() -> {
                        // Show a toast message indicating that the image name already exists
                        Toast.makeText(getActivity(), "The image name already exists, please choose a different name.", Toast.LENGTH_SHORT).show();
                        hideLoadingDialog(); // Hide the loading dialog
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    if (isAdded()) {
                        Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                    hideLoadingDialog(); // Hide the loading dialog in case of error
                });
            }
        }).start();
    }



    private void addButtonList(FrameLayout frameLayout, String[] items) {
        int marginTop = 10;
        for (String label : items) {
            Button button = new Button(getActivity());
            button.setText(label);
            button.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));

            if (marginTop > 0) {
                ((FrameLayout.LayoutParams) button.getLayoutParams()).topMargin = marginTop;
            }

            marginTop += 100;

            button.setOnClickListener(v -> onThemeButtonClick(label));

            frameLayout.addView(button);
        }
    }

    private void onThemeButtonClick(String buttonText) {
        Bundle bundle = new Bundle();
        bundle.putString("selected_button", buttonText);

        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, gameFragment)
                .addToBackStack(null)
                .commit();

        ((MainActivity) getActivity()).selectGameMenuItem();
    }

    private void addCustomImageButtons(FrameLayout frameLayoutCustom) {
        for (String imageName : customImages) {
            addCustomImageButton(frameLayoutCustom, imageName);
        }
    }

    /*private void addCustomImageButton(FrameLayout frameLayoutCustom, String imageName) {
        File imageFile = new File(getActivity().getFilesDir(), imageName);
        if (imageFile.exists()) {
            ImageButton button = new ImageButton(getActivity());
            button.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));
            Bitmap bitmap = getBitmapFromMemCache(imageName);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                addBitmapToMemoryCache(imageName, bitmap);
            }
            button.setImageBitmap(bitmap);

            button.setOnClickListener(v -> onThemeButtonClick(imageName));

            frameLayoutCustom.addView(button);
        }
    }*/

    private void addCustomImageButton(FrameLayout frameLayoutCustom, String imageName) {
        Button button = new Button(getActivity());
        button.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        button.setText(imageName);

        // Calculate and set the top margin for this button
        int buttonCount = frameLayoutCustom.getChildCount();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
        params.topMargin = buttonCount * 100; // Adjust based on the required spacing
        button.setLayoutParams(params);

        button.setOnClickListener(v -> onThemeButtonClick(imageName));

        frameLayoutCustom.addView(button);
    }



    // Add bitmap to memory cache
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    // Get bitmap from memory cache
    private Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}
