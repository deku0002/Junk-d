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
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;

    private static final String TAG = "MainActivity";

    private ImageView imageView;
    private Button btnAnalyze;
    private ProgressBar loadingSpinner;
    private TextView tvCoinWallet;
    private Uri imageUri;
    private Bitmap selectedBitmap;
    private TFLiteWasteAnalyzer analyzer;

    private int userCoins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        tvCoinWallet = findViewById(R.id.tvCoinWallet);
        Button btnRewards = findViewById(R.id.btnViewRewards);
        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);

        imageView.setImageResource(R.drawable.placeholder_image);

        try {
            analyzer = new TFLiteWasteAnalyzer(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(this, "⚠️ Failed to load model", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Model load error", e);
        }

        btnCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });

        btnGallery.setOnClickListener(v -> openGallery());

        btnAnalyze.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
                return;
            }

            btnAnalyze.setEnabled(false);
            loadingSpinner.setVisibility(View.VISIBLE);

            try {
                TFLiteWasteAnalyzer.PredictionResult result = analyzer.analyze(selectedBitmap);

                displayResults(result.label, result.confidence);

                int earnedCoins = Math.max(1, (int)(result.confidence * 10));  // e.g. 95% = 9 coins
                userCoins += earnedCoins;

                if (tvCoinWallet != null) {
                    tvCoinWallet.setText("💰 Coins: " + userCoins);
                }

            } catch (Exception e) {
                Toast.makeText(this, "Analysis failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "AI Analysis error", e);
                showMockAnalysis();
            }

            loadingSpinner.setVisibility(View.GONE);
            btnAnalyze.setEnabled(true);
        });

        if (btnRewards != null) {
            btnRewards.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, CoinsRewardsActivity.class)));
        }

        if (btnLeaderboard != null) {
            btnLeaderboard.setOnClickListener(v ->
                    Toast.makeText(this, "🏆 Leaderboard clicked", Toast.LENGTH_SHORT).show());
        }
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

    private void showMockAnalysis() {
        findViewById(R.id.resultCard).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tvWasteType)).setText("Plastic");
        ((TextView) findViewById(R.id.tvSorted)).setText("Yes");
        ((TextView) findViewById(R.id.tvRecyclable)).setText("Yes");
        ((TextView) findViewById(R.id.tvScore)).setText("87%");
        ((ProgressBar) findViewById(R.id.scoreProgressBar)).setProgress(87);
        tvCoinWallet.setText("💰 Coins: " + (userCoins += 5));

        ScrollView scrollView = findViewById(R.id.mainScrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void displayResults(String label, float confidence) {
        findViewById(R.id.resultCard).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.tvWasteType)).setText(label);
        ((TextView) findViewById(R.id.tvSorted)).setText("Yes");

        boolean isRecyclable = label.equals("plastic") || label.equals("metal") || label.equals("glass") || label.equals("paper");
        ((TextView) findViewById(R.id.tvRecyclable)).setText(isRecyclable ? "Yes" : "No");

        int score = (int)(confidence * 100);
        ((TextView) findViewById(R.id.tvScore)).setText(score + "%");
        ((ProgressBar) findViewById(R.id.scoreProgressBar)).setProgress(score);

        ScrollView scrollView = findViewById(R.id.mainScrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == CAMERA_REQUEST_CODE) {
                    imageView.setImageURI(imageUri);
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    btnAnalyze.setEnabled(true);
                } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                    imageUri = data.getData();
                    imageView.setImageURI(imageUri);

                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedBitmap = BitmapFactory.decodeStream(imageStream);
                    btnAnalyze.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading image", e);
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
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
