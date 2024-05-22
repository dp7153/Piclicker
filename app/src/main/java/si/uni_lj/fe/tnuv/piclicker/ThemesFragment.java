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
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class ThemesFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String PREFS_NAME = "MyPrefs";
    public static final String CUSTOM_IMAGES_KEY = "custom_images";

    private Set<String> customImages;
    private LruCache<String, Bitmap> memoryCache;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        addImageButton.setOnClickListener(v -> onAddImageButtonClick());
        clearImagesButton.setOnClickListener(v -> onClearImagesButtonClick(frameLayoutCustom));

        return rootView;
    }

    private void onAddImageButtonClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void onClearImagesButtonClick(FrameLayout frameLayoutCustom) {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CUSTOM_IMAGES_KEY);
        editor.apply();

        customImages.clear();
        frameLayoutCustom.removeAllViews();
        Toast.makeText(getActivity(), "All custom images removed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            saveImageToAppStorage(selectedImageUri);
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

    private void saveImageToAppStorage(Uri imageUri) {
        new Thread(() -> {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                String filename = "image_" + System.currentTimeMillis() + ".png";
                FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

                getActivity().runOnUiThread(() -> {
                    customImages.add(filename);
                    SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putStringSet(CUSTOM_IMAGES_KEY, customImages);
                    editor.apply();

                    FrameLayout frameLayoutCustom = getView().findViewById(R.id.frame_layout_custom);
                    addCustomImageButton(frameLayoutCustom, filename);

                    Toast.makeText(getActivity(), "Image added successfully", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show());
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
