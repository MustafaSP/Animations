package com.example.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation rotate, blink, bounce;
    ImageView welcomeLogo, imgvwLowWifiSignal, imgvwLowMobileSignal;
    TextView txtVwGithub;

    Handler rotateHandler = new Handler(Looper.getMainLooper());
    Handler internetandWifiHandler = new Handler(Looper.getMainLooper());
    Handler controlInternetHandler = new Handler(Looper.getMainLooper());

    Runnable rotateRunnable, internetandWifiRunnable,controlInternetRunnable;


    Boolean controlTag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        txtVwGithub = findViewById(R.id.txtVwGithub);
        txtVwGithub.setMovementMethod(LinkMovementMethod.getInstance());

        welcomeLogo = findViewById(R.id.welcomeLogo);
        imgvwLowWifiSignal = findViewById(R.id.imgvwLowWifiSignal);
        imgvwLowMobileSignal = findViewById(R.id.imgvwLowMobileSignal);

        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        CheckNetwork checkNetwork = new CheckNetwork();
        checkNetwork.isNetworkAvailable(getApplication());

        rotateRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("rotateRunnable", "Run");
                if (checkNetwork.isNetworkAvailable(getApplication())) {
                    Log.d("rotateRunnable", "if");
                    rotateHandler.removeCallbacks(rotateRunnable);
                    if (!controlTag)
                        controlInternetHandler.postDelayed(controlInternetRunnable,1500);
                }
                else {
                    Log.d("rotateRunnable", "else");
                    welcomeLogo.startAnimation(rotate);
                    rotateHandler.postDelayed(rotateRunnable, 2400);
                }
            }
        };

        internetandWifiRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("internetandWifiRunnable", "Run");
                if (checkNetwork.isNetworkAvailable(getApplication())) {
                    Log.d("internetandWifiRunnable", "if");
                    internetandWifiHandler.removeCallbacks(internetandWifiRunnable);

                    imgvwLowWifiSignal.setVisibility(View.INVISIBLE);
                    imgvwLowMobileSignal.setVisibility(View.INVISIBLE);

                    if (!controlTag)
                        controlInternetHandler.postDelayed(controlInternetRunnable,1500);

                } else {
                    Log.d("internetandWifiRunnable", "else");
                    imgvwLowWifiSignal.setVisibility(View.VISIBLE);
                    imgvwLowMobileSignal.setVisibility(View.VISIBLE);

                    txtVwGithub.startAnimation(bounce);
                    imgvwLowMobileSignal.startAnimation(blink);
                    imgvwLowWifiSignal.startAnimation(blink);
                    internetandWifiHandler.postDelayed(internetandWifiRunnable, 2100);
                }
            }
        };

        controlInternetRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("controlInternetRunnable", "Run");
                if (checkNetwork.isNetworkAvailable(getApplication())) {
                    Log.d("controlInternetRunnable", "If");
                    controlTag = true;
                    internetandWifiHandler.postDelayed(controlInternetRunnable, 1500);
                } else {
                    Log.d("controlInternetRunnable", "Else");
                    controlTag = false;


                    rotateHandler.postDelayed(rotateRunnable, 3000);
                    internetandWifiHandler.postDelayed(internetandWifiRunnable, 3000);
                    controlInternetHandler.removeCallbacks(controlInternetRunnable);
                }
            }
        };

        internetandWifiHandler.postDelayed(internetandWifiRunnable,3000);
        rotateHandler.postDelayed(rotateRunnable,3000);

    }
}