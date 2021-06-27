package com.sommelier.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.sommelier.R;


public class SplashScreenActivity extends AppCompatActivity {

    private long ms = 0;
    private final long splashTime = 1200;
    private final boolean splashActive = true;
    private final boolean paused = false;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        setStatusBarColor();

        constraintLayout = findViewById(R.id.cl);

        Thread thread = new Thread() {
            public void run() {
                try {
                    while (splashActive && ms < splashTime) {
                        if (!paused)
                            ms = ms + 100;
                        sleep(100);
                    }
                } catch (Exception ignored) {

                } finally {

                    if (!isOnline()) {
                        errorSnackbar();
                    } else {
                        goHomeMainActivity();
                    }
                }
            }
        };
        thread.start();
    }

    private void errorSnackbar() {
        Snackbar snackbar = Snackbar.make(constraintLayout, "Відсутній інтернет", Snackbar.LENGTH_INDEFINITE)
                .setAction("ПОВТОРИТИ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recreate();
                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(SplashScreenActivity.this, R.color.colorError));
        View snackbarLayout = snackbar.getView();
        snackbar.setBackgroundTint(Color.BLACK);
        TextView textView = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.laboratory_ic_error, 0, 0, 0);
        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
        snackbar.show();
    }

    private void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void goHomeMainActivity(){
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
