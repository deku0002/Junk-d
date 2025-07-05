package com.example.junk_app;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;

    private ImageView imageView;
    private Button btnAnalyze;
    private ProgressBar loadingSpinner;
    private TextView tvWasteType, tvSorted, tvRecyclable, tvScore;
    private ProgressBar scoreProgressBar;
    private Uri imageUri;
    private Bitmap selectedBitmap;
    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();

        // Initialize network helper
        networkHelper = new NetworkHelper(this);

        // Set default placeholder image
        imageView.setImageResource(R.drawable.placeholder_image);
    }

    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);
        ImageView dropdownMenu = findViewById(R.id.dropdownMenu);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        // Result views
        tvWasteType = findViewById(R.id.tvWasteType);
        tvSorted = findViewById(R.id.tvSorted);
        tvRecyclable = findViewById(R.id.tvRecyclable);
        tvScore = findViewById(R.id.tvScore);
        scoreProgressBar = findViewById(R.id.scoreProgressBar);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnCamera).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });

        findViewById(R.id.btnGallery).setOnClickListener(v -> openGallery());

        btnAnalyze.setOnClickListener(v -> analyzeWaste());

        findViewById(R.id.dropdownMenu).setOnClickListener(this::showPopupMenu);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void analyzeWaste() {
        if (selectedBitmap == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        btnAnalyze.setEnabled(false);
        loadingSpinner.setVisibility(View.VISIBLE);
        hideResults();

        Log.d(TAG, "Starting waste analysis...");

        // Call network helper to analyze waste
        networkHelper.analyzeWaste(selectedBitmap, new NetworkHelper.AnalysisCallback() {
            @Override
            public void onSuccess(NetworkHelper.WasteAnalysisResult result) {
                Log.d(TAG, "Analysis successful: " + result.toString());

                // Hide loading state
                loadingSpinner.setVisibility(View.GONE);
                btnAnalyze.setEnabled(true);

                // Display results
                displayResults(result);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Analysis failed: " + error);

                // Hide loading state
                loadingSpinner.setVisibility(View.GONE);
                btnAnalyze.setEnabled(true);

                // Show error message
                Toast.makeText(MainActivity.this, "Analysis failed: " + error, Toast.LENGTH_LONG).show();

                // Show mock results for demo purposes
                showMockAnalysis();
            }
        });
    }

    private void displayResults(NetworkHelper.WasteAnalysisResult result) {
        // Update UI with real results
        tvWasteType.setText(result.wasteType);
        tvSorted.setText(result.sorted ? "Yes" : "No");
        tvRecyclable.setText(result.recyclableDetected ? "Yes" : "No");
        tvScore.setText(result.score + "%");
        scoreProgressBar.setProgress(result.score);

        // Set colors based on results
        tvSorted.setTextColor(result.sorted ?
                ContextCompat.getColor(this, android.R.color.holo_green_light) :
                ContextCompat.getColor(this, android.R.color.holo_red_light));

        tvRecyclable.setTextColor(result.recyclableDetected ?
                ContextCompat.getColor(this, android.R.color.holo_green_light) :
                ContextCompat.getColor(this, android.R.color.holo_orange_light));

        // Show results card
        findViewById(R.id.resultCard).setVisibility(View.VISIBLE);

        // Auto-scroll to result
        ScrollView scrollView = findViewById(R.id.mainScrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));

        // Show success message if available
        if (!result.message.isEmpty()) {
            Toast.makeText(this, result.message, Toast.LENGTH_LONG).show();
        }
    }

    private void showMockAnalysis() {
        // Fallback mock analysis for demo
        findViewById(R.id.resultCard).setVisibility(View.VISIBLE);
        tvWasteType.setText("Plastic");
        tvSorted.setText("Yes");
        tvRecyclable.setText("Yes");
        tvScore.setText("87%");
        scoreProgressBar.setProgress(87);

        // Auto-scroll to result
        ScrollView scrollView = findViewById(R.id.mainScrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void hideResults() {
        findViewById(R.id.resultCard).setVisibility(View.GONE);
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenu().add("🏆 Leaderboard");
        popup.getMenu().add("🪙 Coins & Rewards");

        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        });

        popup.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == CAMERA_REQUEST_CODE) {
                    // Load image from camera
                    selectedBitmap = loadBitmapFromUri(imageUri);
                    imageView.setImageBitmap(selectedBitmap);
                    btnAnalyze.setEnabled(true);

                } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                    // Load image from gallery
                    imageUri = data.getData();
                    selectedBitmap = loadBitmapFromUri(imageUri);
                    imageView.setImageBitmap(selectedBitmap);
                    btnAnalyze.setEnabled(true);
                }

                hideResults();

            } catch (Exception e) {
                Log.e(TAG, "Error loading image", e);
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap loadBitmapFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (inputStream != null) {
            inputStream.close();
        }
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}