package com.example.junk_app;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private ImageView imageView;
    private Button btnAnalyze;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);
        ImageView dropdownMenu = findViewById(R.id.dropdownMenu);

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

        btnAnalyze.setOnClickListener(v -> showMockAnalysis());

        dropdownMenu.setOnClickListener(this::showPopupMenu);
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

    private void showMockAnalysis() {
        findViewById(R.id.resultCard).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tvWasteType)).setText("Plastic");
        ((TextView) findViewById(R.id.tvSorted)).setText("Yes");
        ((TextView) findViewById(R.id.tvRecyclable)).setText("Yes");
        ((TextView) findViewById(R.id.tvScore)).setText("87%");
        ((ProgressBar) findViewById(R.id.scoreProgressBar)).setProgress(87);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                imageView.setImageURI(imageUri);
                btnAnalyze.setEnabled(true);
            } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
                btnAnalyze.setEnabled(true);
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
