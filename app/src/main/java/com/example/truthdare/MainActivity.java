package com.example.truthdare;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashTextView = findViewById(R.id.tv_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashTextView.setVisibility(View.GONE);
                ivBottle.setVisibility(View.VISIBLE);
            }
        },3000);

        ivBottle = findViewById(R.id.iv_bottle);
        rlOptions = findViewById(R.id.rl_options);
        rlQuestions = findViewById(R.id.rl_questions);
        rlMain = findViewById(R.id.rl_main);
        btnTruth = findViewById(R.id.btn_truth);
        btnDare = findViewById(R.id.btn_dare);
        optionsCancel = findViewById(R.id.tv_option_cancel);
        questionsCancel = findViewById(R.id.tv_question_cancel);
        tvQuestions = findViewById(R.id.tv_display_questions);

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


}
