package com.example.junk_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkHelper {

    private static final String TAG = "NetworkHelper";

    // ✅ Your Flask server endpoint
    private static final String BASE_URL = "http://192.168.29.252:5000/predict";

    private final OkHttpClient client;
    private final Context context;

    public NetworkHelper(Context context) {
        this.context = context;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // ✅ FIXED: Send image as multipart/form-data
    public void analyzeWaste(Bitmap bitmap, AnalysisCallback callback) {
        Log.d(TAG, "Starting waste analysis...");

        // Convert bitmap to JPEG byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        // Create multipart request body with image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg",
                        RequestBody.create(imageBytes, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)  // should be just /predict
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Network request failed", e);
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response: " + responseBody);

                    if (response.isSuccessful()) {
                        WasteAnalysisResult result = parseResponse(responseBody);
                        callback.onSuccess(result);
                    } else {
                        callback.onError("Server error: " + response.code());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }
        });
    }

    // ✅ Parse JSON returned by Flask
    private WasteAnalysisResult parseResponse(String responseBody) throws JSONException {
        JSONObject json = new JSONObject(responseBody);

        WasteAnalysisResult result = new WasteAnalysisResult();
        result.wasteType = json.optString("wasteType", "Unknown");
        result.sorted = json.optBoolean("sorted", false);
        result.recyclableDetected = json.optBoolean("recyclableDetected", false);
        result.score = json.optInt("score", 0);
        result.message = json.optString("message", "");
        result.coinsEarned = json.optInt("coinsEarned", 0);
        result.totalCoins = json.optInt("totalCoins", 0);
        result.ranking = json.optInt("ranking", 0);
        result.success = json.optBoolean("success", false);

        return result;
    }

    // ✅ (Optional) Get leaderboard (not used if no backend)
    public void getLeaderboard(LeaderboardCallback callback) {
        Request request = new Request.Builder()
                .url("http://192.168.29.252:5000/waste/leaderboard") // use correct route if available
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        callback.onSuccess(responseBody);
                    } else {
                        callback.onError("Server error: " + response.code());
                    }
                } catch (Exception e) {
                    callback.onError("Error: " + e.getMessage());
                }
            }
        });
    }

    // ✅ (Optional) Get user stats
    public void getUserStats(String userId, StatsCallback callback) {
        Request request = new Request.Builder()
                .url("http://192.168.29.252:5000/waste/user/" + userId + "/stats")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        callback.onSuccess(responseBody);
                    } else {
                        callback.onError("Server error: " + response.code());
                    }
                } catch (Exception e) {
                    callback.onError("Error: " + e.getMessage());
                }
            }
        });
    }

    // ✅ Response object
    public static class WasteAnalysisResult {
        public String wasteType;
        public boolean sorted;
        public boolean recyclableDetected;
        public int score;
        public String message;
        public int coinsEarned;
        public int totalCoins;
        public int ranking;
        public boolean success;

        @Override
        public String toString() {
            return "WasteAnalysisResult{" +
                    "wasteType='" + wasteType + '\'' +
                    ", sorted=" + sorted +
                    ", recyclableDetected=" + recyclableDetected +
                    ", score=" + score +
                    ", message='" + message + '\'' +
                    ", coinsEarned=" + coinsEarned +
                    ", totalCoins=" + totalCoins +
                    ", ranking=" + ranking +
                    ", success=" + success +
                    '}';
        }
    }

    // ✅ Callback interfaces
    public interface AnalysisCallback {
        void onSuccess(WasteAnalysisResult result);
        void onError(String error);
    }

    public interface LeaderboardCallback {
        void onSuccess(String leaderboardJson);
        void onError(String error);
    }

    public interface StatsCallback {
        void onSuccess(String statsJson);
        void onError(String error);
    }
}
