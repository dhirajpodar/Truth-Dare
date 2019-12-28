package com.example.truthdare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView ivBottle;
    private Random random = new Random();
    private int lastDir;
    private boolean isSpinning;
    private RelativeLayout rlOptions;
    private RelativeLayout rlQuestions;
    private RelativeLayout rlMain;
    private Button btnTruth;
    private Button btnDare;
    private TextView tvQuestions;
    private List<String> truthList;
    private List<String> dareList;
    private TextView questionsCancel;
    private TextView optionsCancel;
    private TextView splashTextView;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignId();
        startSplash();
        initApp();

        MobileAds.initialize(this,"ca-app-pub-5519005217801757~8970156166");
        loadBanner();
        loadIntertitial();
    }
    private void loadIntertitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5519005217801757/6124500978");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }

    private void initApp() {
        truthList = new ArrayList<>();
        dareList = new ArrayList<>();
        truthList = Arrays.asList(getResources().getStringArray(R.array.truthList));
        dareList = Arrays.asList(getResources().getStringArray(R.array.dareList));

        ivBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRotation();
            }
        });
    }

    private void assignId() {
        splashTextView = findViewById(R.id.tv_splash);
        ivBottle = findViewById(R.id.iv_bottle);
        rlOptions = findViewById(R.id.rl_options);
        rlQuestions = findViewById(R.id.rl_questions);
        rlMain = findViewById(R.id.rl_main);
        btnTruth = findViewById(R.id.btn_truth);
        btnDare = findViewById(R.id.btn_dare);
        optionsCancel = findViewById(R.id.tv_option_cancel);
        questionsCancel = findViewById(R.id.tv_question_cancel);
        tvQuestions = findViewById(R.id.tv_display_questions);
        adView = findViewById(R.id.adViewBanner);
    }

    private void startSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashTextView.setVisibility(View.GONE);
                ivBottle.setVisibility(View.VISIBLE);
            }
        },3000);
    }

    private void startRotation() {
        if(!isSpinning) {
            int newDir = random.nextInt(1800);
            float pivotX = ivBottle.getWidth() / 2;
            float pivotY = ivBottle.getHeight() / 2;

            Animation rotate = new RotateAnimation(lastDir, newDir, pivotX, pivotY);
            rotate.setDuration(2500);
            rotate.setFillAfter(true);

            rotate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isSpinning=true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isSpinning=false;
                    showOptions();
                    loadBanner();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            lastDir = newDir;
            ivBottle.startAnimation(rotate);
        }
    }

    private void showOptions() {
        rlOptions.setVisibility(View.VISIBLE);
        ivBottle.setEnabled(false);

        optionsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlOptions.setVisibility(View.GONE);
                ivBottle.setEnabled(true);
            }
        });
        btnTruth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestions(true);
            }
        });
        btnDare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestions(false);
            }
        });
    }

    private void showQuestions(boolean flag) {
        ivBottle.setVisibility(View.GONE);
        rlOptions.setVisibility(View.GONE);
        rlQuestions.setVisibility(View.VISIBLE);
        questionsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
                else {
                    Log.d("TAG","The intertitial was not loaded yet.");
                }
                loadIntertitial();
                rlQuestions.setVisibility(View.GONE);
                ivBottle.setVisibility(View.VISIBLE);
                ivBottle.setEnabled(true);
            }
        });

        if(flag){
            tvQuestions.setText(truthList.get(random.nextInt(truthList.size())));
        }else {
            tvQuestions.setText(dareList.get(random.nextInt(dareList.size())));
        }


    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                        }
                        else {
                            Log.d("TAG","The intertitial was not loaded yet.");
                        }
                        mInterstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                finish();
                            }
                        });
                        dialog.dismiss();
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }
}
