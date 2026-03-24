package com.example.starsgallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.starsgallery.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        // Animation initiale : Rotation et zoom
        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.setAlpha(0f);

        logo.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .rotation(1080f)
                .alpha(1f)
                .setDuration(2000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    // Animation finale : Petit rebond puis départ
                    logo.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(500)
                            .withEndAction(() -> {
                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    startActivity(new Intent(SplashActivity.this, ListActivity.class));
                                    finish();
                                }, 1000);
                            })
                            .start();
                })
                .start();
    }
}