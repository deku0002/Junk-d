package com.example.junk_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CoinsRewardsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "wallet_prefs";
    private static final String KEY_COIN_BALANCE = "coin_balance";

    private TextView coinBalanceText;
    private SharedPreferences sharedPreferences;
    private int coinBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coins_rewards);

        coinBalanceText = findViewById(R.id.coinBalance);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load balance (default 10 coins)
        coinBalance = sharedPreferences.getInt(KEY_COIN_BALANCE, 10);
        updateCoinUI();

        // 🟢 Coupon 1 – 5 Coins
        Button redeem1 = findViewById(R.id.redeemCoupon1);
        redeem1.setOnClickListener(v -> redeem(5, "₹20 Cafe Coupon"));

        // 🟠 Coupon 2 – 10 Coins
        Button redeem2 = findViewById(R.id.redeemCoupon2);
        redeem2.setOnClickListener(v -> redeem(10, "Free Tote Bag"));
    }

    private void updateCoinUI() {
        coinBalanceText.setText("💰 Coins: " + coinBalance);
    }

    private void redeem(int cost, String rewardName) {
        if (coinBalance >= cost) {
            coinBalance -= cost;
            sharedPreferences.edit().putInt(KEY_COIN_BALANCE, coinBalance).apply();
            updateCoinUI();
            Toast.makeText(this, "🎉 Redeemed: " + rewardName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Not enough coins!", Toast.LENGTH_SHORT).show();
        }
    }
}
